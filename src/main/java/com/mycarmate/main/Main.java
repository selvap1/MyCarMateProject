package com.mycarmate.main;

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


            primaryStage.setTitle("MyCarMate");
            primaryStage.setScene(scene); // Attach the scene to the stage
            primaryStage.show(); // Display the stage
        }

    public static void main(String[] args) {
        // Set the path to the service account key
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "src/main/resources/service-account-key.json");

        // Initialize Firebase
        FirebaseConnection.initialize();


        // Test the database connection
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connected to the database successfully!");
            } else {
                System.out.println("Failed to make a connection!");
            }
        } catch (Exception e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
            // Optionally exit the program if the connection fails
            System.exit(1);
        }

        // Launch the JavaFX application
        launch(args);
    }
    };
