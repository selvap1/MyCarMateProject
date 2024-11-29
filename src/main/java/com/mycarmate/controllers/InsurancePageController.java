package com.mycarmate.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class InsurancePageController {
    @FXML private TableColumn<InsuranceRecord, String> carColumn;
    @FXML private TableColumn<InsuranceRecord, String> providerColumn;
    @FXML private TableColumn<InsuranceRecord, String> startColumn;
    @FXML private TableColumn<InsuranceRecord, String> endColumn;
    @FXML private TableColumn<InsuranceRecord, String> coverageColumn;
    @FXML private TableColumn<InsuranceRecord, Void> actionsColumn; // For buttons like Edit/Delete

    @FXML
    private TableView<InsuranceRecord> insuranceTable;

    @FXML
    private TableColumn<InsuranceRecord, String> providerNameColumn, policyNumberColumn, startDateColumn, endDateColumn, coverageAmountColumn, carDetailsColumn;

    private final ObservableList<InsuranceRecord> insuranceRecords = FXCollections.observableArrayList();

    public void initialize() {
        setupInsuranceTable();
    }

    private void setupInsuranceTable() {
        providerNameColumn.setCellValueFactory(data -> data.getValue().providerNameProperty());
        policyNumberColumn.setCellValueFactory(data -> data.getValue().policyNumberProperty());
        startDateColumn.setCellValueFactory(data -> data.getValue().startDateProperty());
        endDateColumn.setCellValueFactory(data -> data.getValue().endDateProperty());
        coverageAmountColumn.setCellValueFactory(data -> data.getValue().coverageAmountProperty());
        carDetailsColumn.setCellValueFactory(data -> data.getValue().carDetailsProperty());

        // Load data into table (Example data)
        insuranceRecords.add(new InsuranceRecord("Provider A", "PN12345", "2023-01-01", "2024-01-01", "$500", "Toyota Corolla (2020)"));
        insuranceTable.setItems(insuranceRecords);
    }
}
