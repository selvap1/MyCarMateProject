package com.mycarmate.controllers;

import com.mycarmate.dao.MaintenanceDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditMaintenanceRecordController {

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

    private MaintenanceRecord currentRecord; // Holds the selected maintenance record
    private MaintenanceDAO maintenanceDAO = new MaintenanceDAO();

    public void setCurrentRecord(MaintenanceRecord record) {
        this.currentRecord = record;

        // Populate fields with current record data
        dateOfServicePicker.setValue(LocalDate.parse(record.getDate()));
        serviceTypeDropdown.setValue(record.getServiceType());
        costField.setText(String.valueOf(record.getCost()));
        mileageField.setText(String.valueOf(record.getMileage()));

        // Show custom service type field if necessary
        if (!serviceTypeDropdown.getItems().contains(record.getServiceType())) {
            serviceTypeDropdown.setValue("Other");
            customServiceTypeField.setText(record.getServiceType());
        }
    }

    @FXML
    private void initialize() {
        // Clear any existing items to avoid duplicates
        serviceTypeDropdown.getItems().clear();

        // Set service type dropdown options
        serviceTypeDropdown.getItems().addAll("Inspection", "Registration", "Oil Change", "Other");

        // Add listener to show custom service type field if "Other" is selected
        serviceTypeDropdown.setOnAction(event -> {
            if ("Other".equals(serviceTypeDropdown.getValue())) {
                customServiceTypeField.setVisible(true);
            } else {
                customServiceTypeField.setVisible(false);
            }
        });

        // Ensure custom service type field is hidden by default
        customServiceTypeField.setVisible(false);
    }


    @FXML
    private void handleSave() {
        try {
            // Validate and fetch updated fields
            LocalDate dateOfService = dateOfServicePicker.getValue();
            if (dateOfService == null) {
                throw new IllegalArgumentException("Date of service is required.");
            }

            String serviceType = serviceTypeDropdown.getValue();
            if ("Other".equals(serviceType)) {
                serviceType = customServiceTypeField.getText();
            }

            double cost = Double.parseDouble(costField.getText());
            int mileage = Integer.parseInt(mileageField.getText());

            // Update the record in the database
            maintenanceDAO.updateMaintenanceRecord(currentRecord.getRecordId(), dateOfService, serviceType, cost, mileage);

            // Close the popup
            Stage stage = (Stage) dateOfServicePicker.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to update maintenance record");
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
}
