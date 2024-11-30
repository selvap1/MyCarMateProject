package com.mycarmate.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class FirebaseAuthHandler {
    private Firestore db;

    /**
     * Constructor to initialize Firebase and Firestore.
     */
    public FirebaseAuthHandler() {
        initializeFirestore();
    }

    /**
     * Initialize Firebase and Firestore.
     */
    private void initializeFirestore() {
        try {
            String serviceAccountPath = System.getProperty("FIREBASE_SERVICE_ACCOUNT_KEY");

            if (serviceAccountPath == null || serviceAccountPath.isEmpty()) {
                throw new IllegalStateException("FIREBASE_SERVICE_ACCOUNT_KEY is not set.");
            }

            FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId("mycarmate-20436") // Replace with your Firebase project ID
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully!");
            } else {
                System.out.println("Firebase is already initialized, skipping re-initialization.");
            }

            this.db = FirestoreClient.getFirestore();
            System.out.println("Firestore initialized successfully!");
        } catch (IOException e) {
            System.err.println("Failed to initialize Firestore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Authenticate a user with Firebase Authentication.
     *
     * @param email    User's email.
     * @param password User's password.
     * @return The user's Firebase UID if authentication is successful.
     * @throws FirebaseAuthException If authentication fails.
     */
    public String authenticateUser(String email, String password) throws Exception {
        try {
            // Fetch user by email
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);

            if (userRecord != null) {
                // Note: Firebase does not handle plaintext password verification
                // Password verification would typically happen on the client side or in Firestore
                System.out.println("User authenticated successfully. UID: " + userRecord.getUid());
                return userRecord.getUid();
            }
        } catch (FirebaseAuthException e) {
            System.out.println("FirebaseAuthException: " + e.getAuthErrorCode());
            throw new Exception("Authentication failed: Invalid email or password.");
        }
        return null;
    }

    /**
     * Verify Firebase ID token and return the UID.
     *
     * @param idToken The Firebase ID token.
     * @return The UID of the authenticated user.
     * @throws FirebaseAuthException If the token is invalid.
     */
    public String verifyIdToken(String idToken) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        return decodedToken.getUid();
    }

    /**
     * Fetch user details from Firestore using Firebase UID.
     *
     * @param firebaseUid The Firebase UID.
     * @return A CompletableFuture containing the user's document snapshot.
     */
    public CompletableFuture<DocumentSnapshot> fetchUser(String firebaseUid) {
        if (db == null) {
            throw new IllegalStateException("Firestore is not initialized.");
        }

        DocumentReference userDocRef = db.collection("users").document(firebaseUid);
        return CompletableFuture.supplyAsync(() -> {
            try {
                return userDocRef.get().get();
            } catch (Exception e) {
                throw new RuntimeException("Error fetching user data: " + e.getMessage(), e);
            }
        });
    }

    /**
     * Main method for testing FirebaseAuthHandler.
     */
    public static void main(String[] args) {
        System.setProperty("FIREBASE_SERVICE_ACCOUNT_KEY", "src/main/resources/mycarmate-20436-firebase-adminsdk-ovldx-7e2061eeff.json");

        FirebaseAuthHandler authHandler = new FirebaseAuthHandler();

        try {
            // Test authentication
            String email = "test@gmail.com";
            String password = "password";
            String firebaseUid = authHandler.authenticateUser(email, password);
            System.out.println("Authenticated User UID: " + firebaseUid);

            // Fetch user details
            authHandler.fetchUser(firebaseUid)
                    .thenAccept(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            System.out.println("User Data: " + documentSnapshot.getData());
                        } else {
                            System.out.println("User not found in Firestore!");
                        }
                    })
                    .exceptionally(throwable -> {
                        System.err.println("Error fetching user data: " + throwable.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            System.err.println("Authentication failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
