package com.mycarmate.models;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConnection {
    private static Firestore db;

    public static void initializeFirestore() {
        try {
            String serviceAccountPath = System.getProperty("FIREBASE_SERVICE_ACCOUNT_KEY");

            if (serviceAccountPath == null || serviceAccountPath.isEmpty()) {
                throw new IllegalStateException("FIREBASE_SERVICE_ACCOUNT_KEY is not set");
            }

            FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId("mycarmate-20436")
                    .build();

            // Initialize Firebase only if not already initialized
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully!");
            }

            // Initialize Firestore
            db = FirestoreClient.getFirestore();
            System.out.println("Firestore initialized successfully!");
        } catch (IOException e) {
            System.err.println("Failed to initialize Firestore: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
