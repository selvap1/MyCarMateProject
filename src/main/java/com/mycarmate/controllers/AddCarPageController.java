package com.mycarmate.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AddCarPageController {

    @FXML
    private TextField makeField, modelField, yearField, vinField, mileageField;

    @FXML
    private DatePicker registrationExpDatePicker, inspectionDatePicker;

    @FXML
    private VBox container;

    @FXML
    private void handleSubmit() {
        String make = makeField.getText();
        String model = modelField.getText();
        String year = yearField.getText();
        String vin = vinField.getText();
        String mileage = mileageField.getText();
        String registrationExpDate = registrationExpDatePicker.getValue() != null ? registrationExpDatePicker.getValue().toString() : "";
        String inspectionDate = inspectionDatePicker.getValue() != null ? inspectionDatePicker.getValue().toString() : "";

        if (make.isEmpty() || model.isEmpty() || year.isEmpty() || vin.isEmpty() || mileage.isEmpty() || registrationExpDate.isEmpty() || inspectionDate.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        System.out.println("Car Details Submitted:");
        System.out.println("Make: " + make);
        System.out.println("Model: " + model);
        System.out.println("Year: " + year);
        System.out.println("VIN: " + vin);
        System.out.println("Mileage: " + mileage);
        System.out.println("Registration Exp. Date: " + registrationExpDate);
        System.out.println("Inspection Date: " + inspectionDate);

        // TODO: Send these details to your backend server
    }

    @FXML
    private void navigateToDashboard() {
        System.out.println("Navigating back to the Dashboard...");
        // TODO: Implement navigation logic
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
