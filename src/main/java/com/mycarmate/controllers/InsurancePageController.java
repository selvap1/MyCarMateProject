package com.mycarmate.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class InsurancePageController {

    @FXML
    private ComboBox<String> carDropdown;

    @FXML
    private TextField providerNameField, policyNumberField, coverageAmountField;

    @FXML
    private DatePicker startDatePicker, endDatePicker;

    @FXML
    private TableView<InsuranceRecord> insuranceTable;

    @FXML
    private TableColumn<InsuranceRecord, String> carColumn, providerColumn, policyNumberColumn;

    @FXML
    private TableColumn<InsuranceRecord, String> startColumn, endColumn;

    @FXML
    private TableColumn<InsuranceRecord, String> coverageColumn;

    @FXML
    private TableColumn<InsuranceRecord, Void> actionsColumn;

    private ObservableList<InsuranceRecord> insuranceRecords = FXCollections.observableArrayList();

    public void initialize() {
        // Set up table columns
        carColumn.setCellValueFactory(new PropertyValueFactory<>("car"));
        providerColumn.setCellValueFactory(new PropertyValueFactory<>("provider"));
        policyNumberColumn.setCellValueFactory(new PropertyValueFactory<>("policyNumber"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        coverageColumn.setCellValueFactory(new PropertyValueFactory<>("coverageAmount"));

        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(e -> handleEditInsurance(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(e -> handleDeleteInsurance(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        // Load initial data
        insuranceTable.setItems(insuranceRecords);
    }

    @FXML
    private void handleAddInsurance() {
        String car = carDropdown.getValue();
        String provider = providerNameField.getText();
        String policyNumber = policyNumberField.getText();
        String startDate = startDatePicker.getValue() != null ? startDatePicker.getValue().toString() : "";
        String endDate = endDatePicker.getValue() != null ? endDatePicker.getValue().toString() : "";
        String coverageAmount = coverageAmountField.getText();

        if (car == null || provider.isEmpty() || policyNumber.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || coverageAmount.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        InsuranceRecord record = new InsuranceRecord(car, provider, policyNumber, startDate, endDate, coverageAmount);
        insuranceRecords.add(record);

        clearForm();
    }

    private void handleEditInsurance(InsuranceRecord record) {
        System.out.println("Editing insurance: " + record);
        // Implement edit logic
    }

    private void handleDeleteInsurance(InsuranceRecord record) {
        System.out.println("Deleting insurance: " + record);
        insuranceRecords.remove(record);
    }

    private void clearForm() {
        carDropdown.setValue(null);
        providerNameField.clear();
        policyNumberField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        coverageAmountField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
