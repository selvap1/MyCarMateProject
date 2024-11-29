package com.mycarmate.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddCarController {
    @FXML
    private TextField makeField;

    @FXML
    private TextField modelField;

    public void saveCar() {
        String make = makeField.getText();
        String model = modelField.getText();
        System.out.println("Saving car: " + make + " " + model);
    }
}
