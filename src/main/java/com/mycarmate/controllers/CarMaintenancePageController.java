package com.mycarmate.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class CarMaintenancePageController {

    @FXML
    private ComboBox<String> carDropdown;

    @FXML
    private TextField costField, mileageAtServiceField, customServiceTypeField;

    @FXML
    private DatePicker dateOfServicePicker;

    @FXML
    private ComboBox<String> serviceTypeDropdown;

    @FXML
    private TableView<MaintenanceRecord> maintenanceTable;

    @FXML
    private TableColumn<MaintenanceRecord, String> costColumn, dateOfServiceColumn, serviceTypeColumn, mileageColumn;

    private ObservableList<MaintenanceRecord> maintenanceRecords = FXCollections.observableArrayList();

    public void initialize() {
        // Set up service type dropdown
        serviceTypeDropdown.setItems(FXCollections.observableArrayList("Inspection", "Registration", "Oil Change", "Other"));
        serviceTypeDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
            customServiceTypeField.setVisible("Other".equals(newVal));
        });

        // Set up table columns
        costColumn.setCellValueFactory(data -> data.getValue().costProperty());
        dateOfServiceColumn.setCellValueFactory(data -> data.getValue().dateOfServiceProperty());
        serviceTypeColumn.setCellValueFactory(data -> data.getValue().serviceTypeProperty());
        mileageColumn.setCellValueFactory(data -> data.getValue().mileageProperty());

        // Load data into table
        maintenanceTable.setItems(maintenanceRecords);
    }

    @FXML
    private void handleAddRecord() {
        String car = carDropdown.getValue();
        String cost = costField.getText();
        String dateOfService = dateOfServicePicker.getValue() != null ? dateOfServicePicker.getValue().toString() : "";
        String serviceType = serviceTypeDropdown.getValue();
        String customServiceType = customServiceTypeField.getText();
        String mileage = mileageAtServiceField.getText();

        if (car == null || cost.isEmpty() || dateOfService.isEmpty() || serviceType.isEmpty() || mileage.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        if ("Other".equals(serviceType) && customServiceType.isEmpty()) {
            showAlert("Error", "Please specify a custom service type.");
            return;
        }

        MaintenanceRecord record = new MaintenanceRecord(cost, dateOfService, "Other".equals(serviceType) ? customServiceType : serviceType, mileage);
        maintenanceRecords.add(record);

        clearForm();
    }

    private void clearForm() {
        costField.clear();
        mileageAtServiceField.clear();
        customServiceTypeField.clear();
        dateOfServicePicker.setValue(null);
        serviceTypeDropdown.setValue(null);
        carDropdown.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
