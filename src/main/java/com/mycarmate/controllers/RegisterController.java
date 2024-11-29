package com.mycarmate.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class RegisterController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text errorText;
    @FXML
    private Text successText;
    @FXML
    private Text loadingText;
    @FXML
    private ImageView logoImageView;
    @FXML
    public void initialize() {
        // Set logo image
        Image logoImage = new Image("file:src/main/resources/assets/mycarmate-high-resolution-logo-transparent.png");
        logoImageView.setImage(logoImage);
    }

    @FXML
    private void handleRegister() {
        // Clear previous messages
        errorText.setVisible(false);
        successText.setVisible(false);
        loadingText.setVisible(true);

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("All fields are required!");
            return;
        }

        try {
            // Mock registration logic (replace with your backend logic)
            boolean registrationSuccess = registerUser(firstName, lastName, username, email, password);

            if (registrationSuccess) {
                showSuccess("User registered successfully!");
                clearFields();
            } else {
                showError("Failed to register user.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred during registration.");
        } finally {
            loadingText.setVisible(false);
        }
    }

    @FXML
    private void goToLogin() {
        System.out.println("Navigating to Login page...");
        // Implement navigation to LoginPage
    }

    private boolean registerUser(String firstName, String lastName, String username, String email, String password) {
        // Replace this with actual backend registration logic
        System.out.printf("Registering user: %s %s (%s, %s)%n", firstName, lastName, username, email);
        return true; // Mock successful registration
    }

    private void showError(String message) {
        errorText.setText(message);
        errorText.setVisible(true);
    }

    private void showSuccess(String message) {
        successText.setText(message);
        successText.setVisible(true);
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
    }
}
