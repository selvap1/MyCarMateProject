package com.mycarmate.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML
    private ImageView profileImage;

    @FXML
    private Button editProfileButton, addCarButton, viewMaintenanceButton, viewInsuranceButton;

    @FXML
    private Label dashboardHeading, welcomeMessage;

    @FXML
    private TableView<Car> carsTable;

    @FXML
    private TableView<String> remindersTable;

    @FXML
    private TableColumn<Car, String> makeColumn, modelColumn, yearColumn;

    @FXML
    private TableColumn<Car, Void> actionsColumn;

    @FXML
    private TableColumn<String, String> reminderColumn;

    public void initialize() {
        // Set up default profile image
        profileImage.setImage(new Image("/assets/account_circle.png"));

        // Initialize tables
        setupCarsTable();
        setupRemindersTable();
    }

    private void setupCarsTable() {
        // Set up columns
        makeColumn.setCellValueFactory(data -> data.getValue().makeProperty());
        modelColumn.setCellValueFactory(data -> data.getValue().modelProperty());
        yearColumn.setCellValueFactory(data -> data.getValue().yearProperty());

        actionsColumn.setCellFactory(col -> new TableCell<Car, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(e -> editCar(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(e -> deleteCar(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(5, editButton, deleteButton);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void setupRemindersTable() {
        // Set up reminders table
        reminderColumn.setCellValueFactory(data -> data.getValue());
    }

    @FXML
    private void openProfileModal() {
        System.out.println("Opening profile modal...");
        // Implement profile modal logic here
    }

    @FXML
    private void navigateToAddCar() {
        System.out.println("Navigating to Add Car...");
        // Implement navigation logic
    }

    @FXML
    private void navigateToMaintenance() {
        System.out.println("Navigating to Maintenance Records...");
        // Implement navigation logic
    }

    @FXML
    private void navigateToInsurance() {
        System.out.println("Navigating to Insurance Information...");
        // Implement navigation logic
    }

    private void editCar(Car car) {
        System.out.println("Editing car: " + car);
        // Implement car editing logic
    }

    private void deleteCar(Car car) {
        System.out.println("Deleting car: " + car);
        // Implement car deletion logic
    }
}
