package com.mycarmate.controllers;

import com.mycarmate.dao.CarDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.IntStream;

public class AddCarController {

    @FXML
    private TextField makeField;
    @FXML
    private TextField modelField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField vinField;
    @FXML
    private TextField mileageField;
    @FXML
    private ComboBox<String> registrationMonthPicker;
    @FXML
    private ComboBox<Integer> registrationYearPicker;
    @FXML
    private ComboBox<String> inspectionMonthPicker;
    @FXML
    private ComboBox<Integer> inspectionYearPicker;

    private int loggedInUserId;

    public void setLoggedInUserId(int userId) {
        this.loggedInUserId = userId;
    }

    @FXML
    private void initialize() {
        // Populate month and year ComboBoxes
        populateMonthPicker(registrationMonthPicker);
        populateYearPicker(registrationYearPicker);

        populateMonthPicker(inspectionMonthPicker);
        populateYearPicker(inspectionYearPicker);
    }

    private void populateMonthPicker(ComboBox<String> monthPicker) {
        monthPicker.getItems().addAll(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
    }

    private void populateYearPicker(ComboBox<Integer> yearPicker) {
        int currentYear = LocalDate.now().getYear();
        IntStream.rangeClosed(currentYear - 20, currentYear + 20).forEach(yearPicker.getItems()::add);
    }

    @FXML
    private void handleSave() {
        try {
            // Validate inputs
            validateInputs();

            String make = makeField.getText().trim();
            String model = modelField.getText().trim();
            int year = Integer.parseInt(yearField.getText().trim());
            String vin = vinField.getText().trim();
            int mileage = Integer.parseInt(mileageField.getText().trim());

            // Format dates to the first day of the selected month
            String formattedRegDate = formatDateToFirstDay(registrationMonthPicker.getValue(), registrationYearPicker.getValue());
            String formattedInspDate = formatDateToFirstDay(inspectionMonthPicker.getValue(), inspectionYearPicker.getValue());

            // Add the car to the database
            CarDAO carDAO = new CarDAO();
            carDAO.addCar(loggedInUserId, make, model, year, vin, mileage, formattedRegDate, formattedInspDate);

            // Show success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Car added successfully!");
            successAlert.showAndWait();

            // Close the popup
            Stage stage = (Stage) makeField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to add car");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void validateInputs() throws IllegalArgumentException {
        if (makeField.getText().trim().isEmpty() ||
                modelField.getText().trim().isEmpty() ||
                yearField.getText().trim().isEmpty() ||
                vinField.getText().trim().isEmpty() ||
                mileageField.getText().trim().isEmpty() ||
                registrationMonthPicker.getValue() == null ||
                registrationYearPicker.getValue() == null ||
                inspectionMonthPicker.getValue() == null ||
                inspectionYearPicker.getValue() == null) {
            throw new IllegalArgumentException("All fields are required.");
        }

        try {
            Integer.parseInt(yearField.getText().trim());
            Integer.parseInt(mileageField.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Year and mileage must be numeric.");
        }
    }

    private String formatDateToFirstDay(String month, int year) {
        Month monthEnum = Month.valueOf(month.toUpperCase());
        LocalDate date = LocalDate.of(year, monthEnum, 1);
        return date.toString(); // Formats as YYYY-MM-DD
    }


    @FXML
    private void handleCancel() {
        // Close the popup without saving
        Stage stage = (Stage) makeField.getScene().getWindow();
        stage.close();
    }
}
