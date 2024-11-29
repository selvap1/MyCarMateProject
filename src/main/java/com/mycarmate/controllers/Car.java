package com.mycarmate.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Car {

    private final SimpleStringProperty carId = new SimpleStringProperty();
    private final SimpleStringProperty make = new SimpleStringProperty();
    private final SimpleStringProperty model = new SimpleStringProperty();
    private final SimpleIntegerProperty year = new SimpleIntegerProperty();
    private final SimpleStringProperty vin = new SimpleStringProperty();
    private final SimpleIntegerProperty mileage = new SimpleIntegerProperty();
    private final SimpleStringProperty registrationExpDate = new SimpleStringProperty();
    private final SimpleStringProperty inspectionDate = new SimpleStringProperty();

    // Default Constructor
    public Car() {
    }

    // Parameterized Constructor
    public Car(String carId, String make, String model, int year, String vin, int mileage, String registrationExpDate, String inspectionDate) {
        this.carId.set(carId);
        this.make.set(make);
        this.model.set(model);
        this.year.set(year);
        this.vin.set(vin);
        this.mileage.set(mileage);
        this.registrationExpDate.set(registrationExpDate);
        this.inspectionDate.set(inspectionDate);
    }

    // Getters and Setters with JavaFX Properties

    public String getCarId() {
        return carId.get();
    }

    public void setCarId(String carId) {
        this.carId.set(carId);
    }

    public SimpleStringProperty carIdProperty() {
        return carId;
    }

    public String getMake() {
        return make.get();
    }

    public void setMake(String make) {
        this.make.set(make);
    }

    public SimpleStringProperty makeProperty() {
        return make;
    }

    public String getModel() {
        return model.get();
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public SimpleStringProperty modelProperty() {
        return model;
    }

    public int getYear() {
        return year.get();
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public SimpleIntegerProperty yearProperty() {
        return year;
    }

    public String getVin() {
        return vin.get();
    }

    public void setVin(String vin) {
        this.vin.set(vin);
    }

    public SimpleStringProperty vinProperty() {
        return vin;
    }

    public int getMileage() {
        return mileage.get();
    }

    public void setMileage(int mileage) {
        this.mileage.set(mileage);
    }

    public SimpleIntegerProperty mileageProperty() {
        return mileage;
    }

    public String getRegistrationExpDate() {
        return registrationExpDate.get();
    }

    public void setRegistrationExpDate(String registrationExpDate) {
        this.registrationExpDate.set(registrationExpDate);
    }

    public SimpleStringProperty registrationExpDateProperty() {
        return registrationExpDate;
    }

    public String getInspectionDate() {
        return inspectionDate.get();
    }

    public void setInspectionDate(String inspectionDate) {
        this.inspectionDate.set(inspectionDate);
    }

    public SimpleStringProperty inspectionDateProperty() {
        return inspectionDate;
    }

    // Utility Methods

    /**
     * Validates the Car object to ensure all required fields are filled.
     *
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return !make.get().isEmpty() && !model.get().isEmpty() && year.get() > 0 && !vin.get().isEmpty();
    }

    /**
     * Compares two Car objects for equality based on VIN.
     *
     * @param other the other Car to compare
     * @return true if equal, false otherwise
     */
    public boolean equals(Car other) {
        return this.vin.get().equalsIgnoreCase(other.getVin());
    }

    /**
     * Formats the car details into a readable string.
     *
     * @return formatted car details
     */
    @Override
    public String toString() {
        return String.format("%s %s (%d)", make.get(), model.get(), year.get());
    }
}
