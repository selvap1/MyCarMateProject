package com.mycarmate.controllers;


import com.mycarmate.dao.CarDAO;
import com.mycarmate.dao.MaintenanceDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddMaintenanceRecordController {

    @FXML
    private DatePicker dateOfServicePicker;

    @FXML
    private ComboBox<String> serviceTypeDropdown;

    @FXML
    private TextField customServiceTypeField;

    @FXML
    private TextField costField;

    @FXML
    private TextField mileageField;

    private int carId;

    public void setCarId(int carId) {
        this.carId = carId;
    }

    @FXML
    private void initialize() {

        // Add a listener to handle "Other" selection
        serviceTypeDropdown.setOnAction(event -> {
            String selectedType = serviceTypeDropdown.getValue();
            if ("Other".equals(selectedType)) {
                customServiceTypeField.setVisible(true);
            } else {
                customServiceTypeField.setVisible(false);
            }
        });
    }

    @FXML
    private void handleSave() {
        try {
            String serviceType = serviceTypeDropdown.getValue();
            if ("Other".equals(serviceType)) {
                serviceType = customServiceTypeField.getText(); // Get custom input for "Other"
            }

            String costString = costField.getText();
            String mileageString = mileageField.getText();

            // Validate input fields
            if (dateOfServicePicker.getValue() == null || serviceType.isEmpty() || costString.isEmpty() || mileageString.isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled.");
            }

            // Parse input values
            LocalDate dateOfService = dateOfServicePicker.getValue();
            double cost = Double.parseDouble(costString);
            int mileage = Integer.parseInt(mileageString);

            // Add record to the database
            MaintenanceDAO maintenanceDAO = new MaintenanceDAO();
            maintenanceDAO.addMaintenanceRecord(carId, dateOfService, serviceType, cost, mileage);

            // Check if the service type requires updating the cars table
            LocalDate updatedDate;
            CarDAO carDAO = new CarDAO();

            if ("Inspection".equalsIgnoreCase(serviceType) || "Registration".equalsIgnoreCase(serviceType)) {
                if ("Inspection".equalsIgnoreCase(serviceType)) {
                    updatedDate = dateOfService.plusYears(1); // Add 1 year for Inspection
                    updatedDate = updatedDate.withDayOfMonth(1); // Set to the first of the month
                    carDAO.updateInspectionDate(carId, updatedDate);
                } else { // Registration
                    updatedDate = dateOfService.plusYears(2); // Add 2 years for Registration
                    updatedDate = updatedDate.withDayOfMonth(1); // Set to the first of the month
                    carDAO.updateRegistrationDate(carId, updatedDate);
                }
            }

            // Update car mileage if the new mileage is greater than the current mileage
            int currentMileage = carDAO.fetchCarMileage(carId);
            if (mileage > currentMileage) {
                carDAO.updateCarMileage(carId, mileage);
            }

            // Close the popup
            Stage stage = (Stage) dateOfServicePicker.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to add maintenance record");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }





    @FXML
    private void handleCancel() {
        // Close the popup without saving
        Stage stage = (Stage) dateOfServicePicker.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
