package com.mycarmate.controllers;

import com.google.firebase.auth.FirebaseAuthException;
import com.mycarmate.dao.UserDAO;
import com.mycarmate.firebase.FirebaseAuthHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private Label statusLabel;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;


    @FXML
    private Button goToLoginButton;

    @FXML
    private void handleRegister() {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please fill in all fields.");
                return;
            }


            // Registration logic goes here
            boolean registrationSuccessful = registerUser(firstName, lastName, email, username, password);

            if (registrationSuccessful) {
                // Show success message
                statusLabel.setText("Registration successful!");

                // Wait for a moment and redirect to Login Page
                redirectToLogin();
            } else {
                statusLabel.setText("Registration failed. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("An error occurred during registration.");
        }
    }

    private boolean registerUser(String firstName, String lastName, String email, String username, String password) {
        try {
            // Create user in Firebase and get the UID
            FirebaseAuthHandler firebaseAuthHandler = new FirebaseAuthHandler();
            String firebaseUid = firebaseAuthHandler.createUser(email, password);

            // Insert user into the database
            UserDAO userDAO = new UserDAO();
            userDAO.insertUser(firebaseUid, firstName, lastName, username, email);

            return true;
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            statusLabel.setText("Failed to create user in Firebase.");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Database error: Could not save user.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginPage.fxml"));
            Parent root = loader.load();

            // Create a new scene for the Login Page
            Scene loginScene = new Scene(root);

            // Add the LoginPage.css stylesheet
            loginScene.getStylesheets().add(getClass().getResource("/styles/LoginPage.css").toExternalForm());

            // Get the current stage and set the Login Page scene
            Stage stage = (Stage) goToLoginButton.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login");
            stage.sizeToScene(); // Optional: Adjust the window size if needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void redirectToLogin() {
        try {
            // Show an alert informing the user of successful registration
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your registration is complete. You will now be redirected to the login page.");
            alert.showAndWait();

            // Load the LoginPage.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginPage.fxml"));
            Parent root = loader.load();

            // Create a new scene for the Login Page
            Scene loginScene = new Scene(root);

            // Add the LoginPage.css stylesheet
            loginScene.getStylesheets().add(getClass().getResource("/styles/LoginPage.css").toExternalForm());

            // Get the current stage and set the Login Page scene
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login");
            stage.sizeToScene(); // Optional: Adjust the window size if needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
