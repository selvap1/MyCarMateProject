package com.mycarmate.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableColumn<MaintenanceRecord, Double> costColumn;

    @FXML
    private TableColumn<MaintenanceRecord, String> dateOfServiceColumn, serviceTypeColumn;

    @FXML
    private TableColumn<MaintenanceRecord, Integer> mileageColumn;

    private ObservableList<MaintenanceRecord> maintenanceRecords = FXCollections.observableArrayList();

    public void initialize() {
        // Set up service type dropdown
        serviceTypeDropdown.setItems(FXCollections.observableArrayList("Inspection", "Registration", "Oil Change", "Other"));
        customServiceTypeField.setVisible(false);

        serviceTypeDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
            customServiceTypeField.setVisible("Other".equals(newVal));
        });

        // Set up table columns
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        dateOfServiceColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfService"));
        serviceTypeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileageAtService"));

        // Load data into table
        maintenanceTable.setItems(maintenanceRecords);

        // Load cars (this would normally come from a database or API)
        loadCars();
    }

    @FXML
    private void handleAddRecord() {
        String car = carDropdown.getValue();
        String costText = costField.getText();
        String dateOfService = dateOfServicePicker.getValue() != null ? dateOfServicePicker.getValue().toString() : "";
        String serviceType = serviceTypeDropdown.getValue();
        String customServiceType = customServiceTypeField.getText();
        String mileageText = mileageAtServiceField.getText();

        // Validate input
        if (car == null || costText.isEmpty() || dateOfService.isEmpty() || serviceType == null || mileageText.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        if ("Other".equals(serviceType) && customServiceType.isEmpty()) {
            showAlert("Error", "Please specify a custom service type.");
            return;
        }

        double cost;
        int mileage;

        try {
            cost = Double.parseDouble(costText);
            mileage = Integer.parseInt(mileageText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Cost and mileage must be numeric values.");
            return;
        }

        // Create and add a new maintenance record
        MaintenanceRecord record = new MaintenanceRecord(
                0, // recordId (to be set by the database)
                car,
                cost,
                dateOfService,
                "Other".equals(serviceType) ? customServiceType : serviceType,
                mileage
        );

        maintenanceRecords.add(record);

        // Clear the form
        clearForm();

        // Save the record to the database (TODO: Add backend API call)
        saveRecordToDatabase(record);
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
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadCars() {
        // Simulate fetching cars from the database or an API
        carDropdown.setItems(FXCollections.observableArrayList(
                "Toyota Corolla (2015)",
                "Honda Civic (2018)",
                "Ford Focus (2020)"
        ));
    }

    private void saveRecordToDatabase(MaintenanceRecord record) {
        // TODO: Implement API call to save the record to the backend
        System.out.println("Saving record to database: " + record);
    }
}
