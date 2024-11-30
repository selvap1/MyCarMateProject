package com.mycarmate.controllers;

import javafx.beans.property.*;

public class MaintenanceRecord {

    private final IntegerProperty recordId = new SimpleIntegerProperty();
    private final IntegerProperty carId = new SimpleIntegerProperty();
    private final StringProperty date = new SimpleStringProperty();
    private final StringProperty serviceType = new SimpleStringProperty();
    private final DoubleProperty cost = new SimpleDoubleProperty();
    private final IntegerProperty mileage = new SimpleIntegerProperty();
    private final StringProperty createdAt = new SimpleStringProperty();

    // Constructor
    public MaintenanceRecord(int recordId, int carId, String date, String serviceType, double cost, int mileage, String createdAt) {
        this.recordId.set(recordId);
        this.carId.set(carId);
        this.date.set(date);
        this.serviceType.set(serviceType);
        this.cost.set(cost);
        this.mileage.set(mileage);
        this.createdAt.set(createdAt);
    }

    // Getters and setters with properties

    public int getRecordId() {
        return recordId.get();
    }

    public void setRecordId(int recordId) {
        this.recordId.set(recordId);
    }

    public IntegerProperty recordIdProperty() {
        return recordId;
    }

    public int getCarId() {
        return carId.get();
    }

    public void setCarId(int carId) {
        this.carId.set(carId);
    }

    public IntegerProperty carIdProperty() {
        return carId;
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public StringProperty dateProperty() {
        return date;
    }

    public String getServiceType() {
        return serviceType.get();
    }

    public void setServiceType(String serviceType) {
        this.serviceType.set(serviceType);
    }

    public StringProperty serviceTypeProperty() {
        return serviceType;
    }

    public double getCost() {
        return cost.get();
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }

    public DoubleProperty costProperty() {
        return cost;
    }

    public int getMileage() {
        return mileage.get();
    }

    public void setMileage(int mileage) {
        this.mileage.set(mileage);
    }

    public IntegerProperty mileageProperty() {
        return mileage;
    }

    public String getCreatedAt() {
        return createdAt.get();
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt.set(createdAt);
    }

    public StringProperty createdAtProperty() {
        return createdAt;
    }

    // toString for debugging purposes
    @Override
    public String toString() {
        return "MaintenanceRecord{" +
                "recordId=" + getRecordId() +
                ", carId=" + getCarId() +
                ", date='" + getDate() + '\'' +
                ", serviceType='" + getServiceType() + '\'' +
                ", cost=" + getCost() +
                ", mileage=" + getMileage() +
                ", createdAt='" + getCreatedAt() + '\'' +
                '}';
    }
}
