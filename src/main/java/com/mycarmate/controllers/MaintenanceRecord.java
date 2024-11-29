package com.mycarmate.controllers;

import javafx.beans.property.*;

public class MaintenanceRecord {

    private final SimpleIntegerProperty recordId = new SimpleIntegerProperty();
    private final SimpleStringProperty carId = new SimpleStringProperty();
    private final SimpleDoubleProperty cost = new SimpleDoubleProperty();
    private final SimpleStringProperty dateOfService = new SimpleStringProperty();
    private final SimpleStringProperty serviceType = new SimpleStringProperty();
    private final SimpleIntegerProperty mileageAtService = new SimpleIntegerProperty();

    // Default Constructor
    public MaintenanceRecord() {
    }

    // Parameterized Constructor
    public MaintenanceRecord(int recordId, String carId, double cost, String dateOfService, String serviceType, int mileageAtService) {
        this.recordId.set(recordId);
        this.carId.set(carId);
        this.cost.set(cost);
        this.dateOfService.set(dateOfService);
        this.serviceType.set(serviceType);
        this.mileageAtService.set(mileageAtService);
    }

    // Getters and Setters with JavaFX Properties

    public int getRecordId() {
        return recordId.get();
    }

    public void setRecordId(int recordId) {
        this.recordId.set(recordId);
    }

    public SimpleIntegerProperty recordIdProperty() {
        return recordId;
    }

    public String getCarId() {
        return carId.get();
    }

    public void setCarId(String carId) {
        this.carId.set(carId);
    }

    public SimpleStringProperty carIdProperty() {
        return carId;
    }

    public double getCost() {
        return cost.get();
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }

    public SimpleDoubleProperty costProperty() {
        return cost;
    }

    public String getDateOfService() {
        return dateOfService.get();
    }

    public void setDateOfService(String dateOfService) {
        this.dateOfService.set(dateOfService);
    }

    public SimpleStringProperty dateOfServiceProperty() {
        return dateOfService;
    }

    public String getServiceType() {
        return serviceType.get();
    }

    public void setServiceType(String serviceType) {
        this.serviceType.set(serviceType);
    }

    public SimpleStringProperty serviceTypeProperty() {
        return serviceType;
    }

    public int getMileageAtService() {
        return mileageAtService.get();
    }

    public void setMileageAtService(int mileageAtService) {
        this.mileageAtService.set(mileageAtService);
    }

    public SimpleIntegerProperty mileageAtServiceProperty() {
        return mileageAtService;
    }

    // toString() for Debugging and Display
    @Override
    public String toString() {
        return "MaintenanceRecord{" +
                "recordId=" + recordId.get() +
                ", carId='" + carId.get() + '\'' +
                ", cost=" + cost.get() +
                ", dateOfService='" + dateOfService.get() + '\'' +
                ", serviceType='" + serviceType.get() + '\'' +
                ", mileageAtService=" + mileageAtService.get() +
                '}';
    }
}