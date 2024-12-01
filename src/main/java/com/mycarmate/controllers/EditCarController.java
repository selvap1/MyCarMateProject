package com.mycarmate.controllers;

import com.mycarmate.dao.CarDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.Month;

public class EditCarController {

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

    private Car car;

    @FXML
    private void initialize() {
        populateMonthPicker(registrationMonthPicker);
        populateYearPicker(registrationYearPicker);
        populateMonthPicker(inspectionMonthPicker);
        populateYearPicker(inspectionYearPicker);
    }

    /**
     * Populate month picker with months.
     */
    private void populateMonthPicker(ComboBox<String> monthPicker) {
        monthPicker.getItems().addAll(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
    }

    /**
     * Populate year picker with a range of years.
     */
    private void populateYearPicker(ComboBox<Integer> yearPicker) {
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 10; i <= currentYear + 10; i++) {
            yearPicker.getItems().add(i);
        }
    }

    /**
     * Set car details and populate the fields.
     *
     * @param car The car to edit.
     */
    public void setCarDetails(Car car) {
        this.car = car;

        // Populate the fields with the car's details
        makeField.setText(car.getMake());
        modelField.setText(car.getModel());
        yearField.setText(String.valueOf(car.getYear()));
        vinField.setText(car.getVin());
        mileageField.setText(String.valueOf(car.getMileage()));

        // Populate registration and inspection expiration date pickers
        if (car.getRegistrationExpDate() != null) {
            LocalDate regDate = LocalDate.parse(car.getRegistrationExpDate());
            registrationMonthPicker.setValue(capitalizeFirstLetter(regDate.getMonth().toString().toLowerCase()));
            registrationYearPicker.setValue(regDate.getYear());
        }

        if (car.getInspectionDate() != null) {
            LocalDate inspDate = LocalDate.parse(car.getInspectionDate());
            inspectionMonthPicker.setValue(capitalizeFirstLetter(inspDate.getMonth().toString().toLowerCase()));
            inspectionYearPicker.setValue(inspDate.getYear());
        }
    }

    // Utility method to capitalize the first letter of a string
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }


    @FXML
    private void handleSave() {
        try {
            // Update the car object with new values
            car.setMake(makeField.getText());
            car.setModel(modelField.getText());
            car.setYear(Integer.parseInt(yearField.getText()));
            car.setVin(vinField.getText());
            car.setMileage(Integer.parseInt(mileageField.getText()));

            // Update registration expiration date
            String regMonth = registrationMonthPicker.getValue();
            int regYear = registrationYearPicker.getValue();
            car.setRegistrationExpDate(LocalDate.of(regYear, Month.valueOf(regMonth.toUpperCase()), 1).toString());

            // Update inspection expiration date
            String inspMonth = inspectionMonthPicker.getValue();
            int inspYear = inspectionYearPicker.getValue();
            car.setInspectionDate(LocalDate.of(inspYear, Month.valueOf(inspMonth.toUpperCase()), 1).toString());

            // Update the car in the database
            CarDAO carDAO = new CarDAO();
            carDAO.updateCar(car);

            // Close the modal
            Stage stage = (Stage) makeField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.err.println("Error saving car details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        // Close the modal without saving changes
        Stage stage = (Stage) makeField.getScene().getWindow();
        stage.close();
    }
}
