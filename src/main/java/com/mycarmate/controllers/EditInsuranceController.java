package com.mycarmate.controllers;

import com.mycarmate.dao.InsuranceDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.IntStream;

public class EditInsuranceController {

    @FXML
    private TextField providerField;
    @FXML
    private TextField policyField;
    @FXML
    private ComboBox<String> startMonthPicker;
    @FXML
    private ComboBox<Integer> startYearPicker;
    @FXML
    private ComboBox<String> endMonthPicker;
    @FXML
    private ComboBox<Integer> endYearPicker;
    @FXML
    private TextField coverageField;

    private InsuranceRecord insuranceRecord;
    private InsurancePageController insurancePageController;

    @FXML
    private void initialize() {
        // Populate month and year pickers
        populateMonthPicker(startMonthPicker);
        populateYearPicker(startYearPicker);
        populateMonthPicker(endMonthPicker);
        populateYearPicker(endYearPicker);
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

    public void setInsuranceRecord(InsuranceRecord record) {
        this.insuranceRecord = record;

        // Populate fields with existing data
        providerField.setText(record.getProviderName());
        policyField.setText(record.getPolicyNumber());

        LocalDate startDate = LocalDate.parse(record.getStartDate());
        startMonthPicker.setValue(startDate.getMonth().toString().substring(0, 1).toUpperCase() +
                startDate.getMonth().toString().substring(1).toLowerCase());
        startYearPicker.setValue(startDate.getYear());

        LocalDate endDate = LocalDate.parse(record.getEndDate());
        endMonthPicker.setValue(endDate.getMonth().toString().substring(0, 1).toUpperCase() +
                endDate.getMonth().toString().substring(1).toLowerCase());
        endYearPicker.setValue(endDate.getYear());

        coverageField.setText(String.valueOf(record.getCoverageAmount()));
    }

    public void setInsurancePageController(InsurancePageController controller) {
        this.insurancePageController = controller;
    }

    @FXML
    private void handleSave() {
        try {
            String provider = providerField.getText();
            String policy = policyField.getText();
            String startDate = formatDateToFirstDay(startMonthPicker.getValue(), startYearPicker.getValue());
            String endDate = formatDateToFirstDay(endMonthPicker.getValue(), endYearPicker.getValue());
            double coverage = Double.parseDouble(coverageField.getText());

            // Validate fields
            if (provider.isEmpty() || policy.isEmpty() || startMonthPicker.getValue() == null ||
                    endMonthPicker.getValue() == null || startYearPicker.getValue() == null ||
                    endYearPicker.getValue() == null || coverageField.getText().isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled out.");
            }

            // Update the insurance record in the database
            InsuranceDAO insuranceDAO = new InsuranceDAO();
            insuranceDAO.updateInsuranceRecord(insuranceRecord.getInsuranceId(), provider, policy, startDate, endDate, coverage);

            // Refresh the table
            insurancePageController.loadInsuranceRecords();

            // Close the modal
            ((Stage) providerField.getScene().getWindow()).close();
        } catch (Exception e) {
            System.err.println("Error saving insurance record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String formatDateToFirstDay(String month, int year) {
        Month monthEnum = Month.valueOf(month.toUpperCase());
        return LocalDate.of(year, monthEnum, 1).toString();
    }

    @FXML
    private void handleCancel() {
        ((Stage) providerField.getScene().getWindow()).close();
    }
}
