package com.mycarmate.main;

import com.mycarmate.firebase.FirebaseAuthHandler;
import com.mycarmate.models.DatabaseConnection;
import com.mycarmate.models.FirebaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginPage.fxml"));
            Parent root = loader.load();

            // Set initial dimensions for the LoginPage
            double initialWidth = 800;  // Adjust this value as needed
            double initialHeight = 600; // Adjust this value as needed

            // Create the scene with specified dimensions and apply styles
            Scene scene = new Scene(root, initialWidth, initialHeight);
            scene.getStylesheets().add(getClass().getResource("/styles/LoginPage.css").toExternalForm());

            // Set the stage properties
            primaryStage.setTitle("MyCarMate");
            primaryStage.setScene(scene);

            // Center the stage on the screen
            primaryStage.centerOnScreen();

            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Error loading FXML or CSS: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        // Set the path to the service account key for Google Cloud SQL
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "src/main/java/resources/service-account-key.json");

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
