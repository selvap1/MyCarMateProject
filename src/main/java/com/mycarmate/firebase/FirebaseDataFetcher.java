package com.mycarmate.firebase;

import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;

public class FirebaseDataFetcher {

    public void fetchData() {
        Firestore db = FirestoreClient.getFirestore();

        db.collection("your-collection-name").get().thenAccept(querySnapshot -> {
            querySnapshot.getDocuments().forEach(document -> {
                System.out.println("Document: " + document.getId());
                System.out.println("Data: " + document.getData());
            });
        }).exceptionally(e -> {
            System.err.println("Error fetching data: " + e.getMessage());
            return null;
        });
    }
}
