package com.mycarmate.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.mycarmate.controllers.InsuranceRecord;

public class InsuranceRecord {

    private final StringProperty providerName;
    private final StringProperty policyNumber;
    private final StringProperty startDate;
    private final StringProperty endDate;
    private final StringProperty coverageAmount;
    private final StringProperty carDetails; // e.g., "Make Model (Year)"

    // Constructor
    public InsuranceRecord(String providerName, String policyNumber, String startDate, String endDate, String coverageAmount, String carDetails) {
        this.providerName = new SimpleStringProperty(providerName);
        this.policyNumber = new SimpleStringProperty(policyNumber);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.coverageAmount = new SimpleStringProperty(coverageAmount);
        this.carDetails = new SimpleStringProperty(carDetails);
    }

    // Getters for Properties
    public StringProperty providerNameProperty() {
        return providerName;
    }

    public StringProperty policyNumberProperty() {
        return policyNumber;
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    public StringProperty endDateProperty() {
        return endDate;
    }

    public StringProperty coverageAmountProperty() {
        return coverageAmount;
    }

    public StringProperty carDetailsProperty() {
        return carDetails;
    }

    // Getters for Values
    public String getProviderName() {
        return providerName.get();
    }

    public String getPolicyNumber() {
        return policyNumber.get();
    }

    public String getStartDate() {
        return startDate.get();
    }

    public String getEndDate() {
        return endDate.get();
    }

    public String getCoverageAmount() {
        return coverageAmount.get();
    }

    public String getCarDetails() {
        return carDetails.get();
    }

    // Setters
    public void setProviderName(String providerName) {
        this.providerName.set(providerName);
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber.set(policyNumber);
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    public void setCoverageAmount(String coverageAmount) {
        this.coverageAmount.set(coverageAmount);
    }

    public void setCarDetails(String carDetails) {
        this.carDetails.set(carDetails);
    }
}
