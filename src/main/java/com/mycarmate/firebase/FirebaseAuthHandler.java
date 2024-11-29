package com.mycarmate.firebase;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class FirebaseAuthHandler {
    private Firestore db;

    public FirebaseAuthHandler() {
        initializeFirestore();
    }

    private void initializeFirestore() {
        try {
            // Get the service account key path from system properties
            String serviceAccountPath = System.getProperty("FIREBASE_SERVICE_ACCOUNT_KEY");

            if (serviceAccountPath == null || serviceAccountPath.isEmpty()) {
                throw new IllegalStateException("FIREBASE_SERVICE_ACCOUNT_KEY is not set. Ensure you set the key before initialization.");
            }

            FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId("mycarmate-20436")
                    .build();

            // Check if FirebaseApp is already initialized
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully!");
            } else {
                System.out.println("Firebase is already initialized, skipping re-initialization.");
            }

            // Initialize Firestore
            this.db = FirestoreClient.getFirestore();
            System.out.println("Firestore initialized successfully!");
        } catch (IOException e) {
            System.err.println("Failed to initialize Firestore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public CompletableFuture<DocumentSnapshot> fetchUser(String firebaseUid) {
        // Validate Firestore instance
        if (db == null) {
            throw new IllegalStateException("Firestore is not initialized. Ensure initializeFirestore() is called first.");
        }

        // Reference to the users collection
        DocumentReference userDocRef = db.collection("users").document(firebaseUid);

        // Firestore query to get user details
        ApiFuture<DocumentSnapshot> queryFuture = userDocRef.get();

        // Convert ApiFuture to CompletableFuture for easier handling
        return toCompletableFuture(queryFuture);
    }

    public static <T> CompletableFuture<T> toCompletableFuture(ApiFuture<T> apiFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
            try {
                completableFuture.complete(apiFuture.get());
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        }).start();
        return completableFuture;
    }

    public static void main(String[] args) {
        // Set the path to the Firebase service account key file
        System.setProperty("FIREBASE_SERVICE_ACCOUNT_KEY", "src/main/resources/mycarmate-20436-firebase-adminsdk-ovldx-7e2061eeff.json");

        // Test the FirebaseAuthHandler
        FirebaseAuthHandler authHandler = new FirebaseAuthHandler();
        String testFirebaseUid = "O8suq7AaIoRrIo7wNVcLEtT39eo1";

        authHandler.fetchUser(testFirebaseUid)
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
    }
}
