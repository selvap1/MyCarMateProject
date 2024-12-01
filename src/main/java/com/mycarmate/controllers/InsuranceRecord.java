package com.mycarmate.controllers;

import javafx.beans.property.*;

import java.util.Objects;

public class InsuranceRecord {

    private final IntegerProperty insuranceId;
    private final StringProperty carName;
    private final StringProperty providerName;
    private final StringProperty policyNumber;
    private final StringProperty startDate;
    private final StringProperty endDate;
    private final DoubleProperty coverageAmount;

    public InsuranceRecord(int insuranceId, String carName, String providerName, String policyNumber,
                           String startDate, String endDate, double coverageAmount) {
        this.insuranceId = new SimpleIntegerProperty(insuranceId);
        this.carName = new SimpleStringProperty(carName);
        this.providerName = new SimpleStringProperty(providerName);
        this.policyNumber = new SimpleStringProperty(policyNumber);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.coverageAmount = new SimpleDoubleProperty(coverageAmount);
    }

    public int getInsuranceId() {
        return insuranceId.get();
    }

    public IntegerProperty insuranceIdProperty() {
        return insuranceId;
    }

    public String getCarName() {
        return carName.get();
    }

    public StringProperty carNameProperty() {
        return carName;
    }

    public String getProviderName() {
        return providerName.get();
    }

    public StringProperty providerNameProperty() {
        return providerName;
    }

    public String getPolicyNumber() {
        return policyNumber.get();
    }

    public StringProperty policyNumberProperty() {
        return policyNumber;
    }

    public String getStartDate() {
        return startDate.get();
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    public String getEndDate() {
        return endDate.get();
    }

    public StringProperty endDateProperty() {
        return endDate;
    }

    public double getCoverageAmount() {
        return coverageAmount.get();
    }

    public DoubleProperty coverageAmountProperty() {
        return coverageAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsuranceRecord that = (InsuranceRecord) o;
        return insuranceId.get() == that.insuranceId.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(insuranceId);
    }
}