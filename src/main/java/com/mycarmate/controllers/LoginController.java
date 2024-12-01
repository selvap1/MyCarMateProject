package com.mycarmate.controllers;

import com.google.firebase.auth.FirebaseAuthException;
import com.mycarmate.dao.UserDAO;
import com.mycarmate.firebase.FirebaseAuthHandler;
import com.mycarmate.dao.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    @FXML
    private Label errorLabel;


    private FirebaseAuthHandler firebaseAuthHandler;

    @FXML
    public void initialize() {
        firebaseAuthHandler = new FirebaseAuthHandler(); // Initialize FirebaseAuthHandler
    }


    @FXML
    private void handleLogin() {
        try {
            String email = emailField.getText();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }

            // Authenticate user (ensure this calls FirebaseAuthHandler appropriately)
            FirebaseAuthHandler authHandler = new FirebaseAuthHandler();
            String firebaseUid = authHandler.authenticateUser(email, password);

            if (firebaseUid != null) {
                // Fetch userId from the database using firebaseUid
                int userId = UserDAO.fetchUserIdFromFirebaseUid(firebaseUid);

                // Set the userId in SessionManager
                SessionManager.setLoggedInUserId(userId);
                System.out.println("Logged-in user ID stored in SessionManager: " + userId);

                // Navigate to the dashboard
                loadDashboardScene(firebaseUid);
            } else {
                errorLabel.setText("Invalid email or password.");
            }
        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
            errorLabel.setText("An error occurred. Please try again.");
        }
    }




    /**
     * Handle the sign-up button action to navigate to the registration page.
     */
    @FXML
    private void handleSignUp() {
        try {
            // Load the RegisterPage.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RegisterPage.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) signUpButton.getScene().getWindow();

            // Set the scene with the Register Page
            stage.setScene(new Scene(root));
            stage.setTitle("Register");
            stage.sizeToScene(); // Optional

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void loadDashboardScene(String firebaseUid) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DashboardPage.fxml"));
            Parent dashboardRoot = loader.load();

            // Pass the logged-in user ID to the DashboardController
            DashboardController dashboardController = loader.getController();
            dashboardController.initializeDashboard(firebaseUid);

            Stage primaryStage = (Stage) loginButton.getScene().getWindow();
            Scene dashboardScene = new Scene(dashboardRoot);
            primaryStage.setScene(dashboardScene);
        } catch (Exception e) {
            System.err.println("Error loading dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
