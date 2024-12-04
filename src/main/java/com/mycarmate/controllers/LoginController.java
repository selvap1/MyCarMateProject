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
import javafx.scene.image.Image;

import javax.swing.text.html.ImageView;
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

    @FXML
    private javafx.scene.image.ImageView logoImage;



    private FirebaseAuthHandler firebaseAuthHandler;

    @FXML
    public void initialize() {
        firebaseAuthHandler = new FirebaseAuthHandler(); // Initialize FirebaseAuthHandler

        // Set the logo image
        Image logo = new Image(getClass().getResourceAsStream("/assets/mycarmate-high-resolution-logo-transparent.png"));
        logoImage.setImage(logo);
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DashboardPage.fxml"));
                Parent dashboardRoot = loader.load();

                // Pass the Firebase UID to the DashboardController
                DashboardController dashboardController = loader.getController();
                dashboardController.initializeDashboard(firebaseUid);

                Stage primaryStage = (Stage) emailField.getScene().getWindow();
                primaryStage.setScene(new Scene(dashboardRoot));
                primaryStage.setTitle("Dashboard");

                // Center the window on the screen
                primaryStage.centerOnScreen();

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

            // Set fixed dimensions for the Register Page
            double registerSceneWidth = 800;  // Adjust width as needed
            double registerSceneHeight = 600; // Adjust height as needed

            // Create the new scene with specified dimensions
            Scene registerScene = new Scene(root, registerSceneWidth, registerSceneHeight);

            // Apply styling for the Register Page
            registerScene.getStylesheets().clear();
            registerScene.getStylesheets().add(getClass().getResource("/styles/RegisterPage.css").toExternalForm());

            // Get the current stage (window)
            Stage stage = (Stage) signUpButton.getScene().getWindow();

            // Set the scene with the Register Page
            stage.setScene(registerScene);
            stage.setTitle("Register");
            stage.centerOnScreen(); // Center the stage on the screen
            stage.show(); // Ensure the stage is updated
        } catch (IOException e) {
            System.err.println("Error navigating to Register Page: " + e.getMessage());
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

    @FXML
    private void handleForgotPassword() {
        try {
            String email = emailField.getText(); // Retrieve the email from the email input field

            if (email.isEmpty()) {
                errorLabel.setText("Please enter your email to reset your password.");
                return;
            }

            // Call the custom FirebaseAuthHandler method
            FirebaseAuthHandler authHandler = new FirebaseAuthHandler();
            authHandler.sendPasswordResetEmail(email);

            // Provide feedback to the user
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Reset");
            alert.setHeaderText(null);
            alert.setContentText("A password reset link has been sent to " + email + ".");
            alert.showAndWait();

        } catch (Exception e) {
            System.err.println("Error sending password reset email: " + e.getMessage());
            errorLabel.setText("Failed to send reset email. Please try again.");
            e.printStackTrace();
        }
    }

}
