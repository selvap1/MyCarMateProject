package com.mycarmate.controllers;

import com.mycarmate.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class EditProfileController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField profilePictureField;

    private int loggedInUserId;
    private String profilePicturePath = "/assets/account_circle.png"; // Default profile picture

    public void setLoggedInUserDetails(int userId, String firstName, String lastName, String username, String profilePicture) {
        this.loggedInUserId = userId;

        firstNameField.setText(firstName);
        lastNameField.setText(lastName);
        usernameField.setText(username);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            profilePictureField.setText(profilePicture); // Set the ImageURL field
        }
    }



    @FXML
    private void handleSave() {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String profilePicture = profilePictureField.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty()) {
                throw new IllegalArgumentException("All fields are required.");
            }

            // Update user profile in the database
            UserDAO userDAO = new UserDAO();
            userDAO.updateUserProfile(loggedInUserId, firstName, lastName, username, profilePicture);

            // Close the modal
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to update profile");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    private void handleCancel() {
        // Close the popup without saving
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }
}
