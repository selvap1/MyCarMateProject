package com.mycarmate.main;

import com.mycarmate.firebase.FirebaseAuthHandler;
import com.mycarmate.models.DatabaseConnection;
import com.mycarmate.models.FirebaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.Connection;

public class Main extends Application {
        @Override
        public void start(Stage primaryStage) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("LoginPage.css").toExternalForm());
            StackPane root = new StackPane(); // Root node for the scene
            Scene scene = new Scene(root, 800, 600); // Create the scene with width and height

    public static void main(String[] args) {
        // Set the path to the service account key for Google Cloud SQL
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "src/main/java/resources/service-account-key.json");


            primaryStage.setTitle("MyCarMate");
            primaryStage.setScene(scene); // Attach the scene to the stage
            primaryStage.show(); // Display the stage
        }

        // Set the path to the service account key for Firebase
        System.setProperty("FIREBASE_SERVICE_ACCOUNT_KEY", "src/main/java/resources/firebase-key.json");


        // Initialize Firestore
        FirebaseConnection.initializeFirestore();

        // Initialize FirebaseAuthHandler
        FirebaseAuthHandler handler = new FirebaseAuthHandler();


        // Test Google Cloud SQL database connection
        System.out.println("Testing Google Cloud SQL Database Connection...");
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connected to the Google Cloud SQL database successfully!");
            } else {
                System.out.println("Failed to connect to the Google Cloud SQL database!");
            }
        } catch (Exception e) {
            System.err.println("Google Cloud SQL Database connection error: " + e.getMessage());
            e.printStackTrace();
        }

        // Launch the JavaFX application
        System.out.println("Launching JavaFX Application...");
        launch(args);
    }
}
