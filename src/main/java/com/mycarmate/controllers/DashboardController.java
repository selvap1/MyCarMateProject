package com.mycarmate.controllers;

import com.mycarmate.controllers.Car;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import javafx.util.Callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

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

    private ObservableList<Car> carList = FXCollections.observableArrayList();
    private ObservableList<String> reminderList = FXCollections.observableArrayList();

    private final String BASE_URL = "http://localhost:5050";

    public void initialize() {
        // Set up default profile image
        profileImage.setImage(new Image("/assets/account_circle.png"));

        // Initialize tables
        setupCarsTable();
        setupRemindersTable();

        // Load data from backend
        loadCarData();
        loadReminderData();
    }

    private void setupCarsTable() {
        // Set up columns
        makeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMake()));
        modelColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModel()));
        yearColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getYear())));

        actionsColumn.setCellFactory(new Callback<TableColumn<Car, Void>, TableCell<Car, Void>>() {
            @Override
            public TableCell<Car, Void> call(TableColumn<Car, Void> param) {
                return new TableCell<Car, Void>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");

                    {
                        editButton.setOnAction(e -> editCar((Car) getTableView().getItems().get(getIndex())));
                        deleteButton.setOnAction(e -> deleteCar((Car) getTableView().getItems().get(getIndex())));
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
                };
            }
        });


        carsTable.setItems(carList);
    }

    private void setupRemindersTable() {
        reminderColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        remindersTable.setItems(reminderList);
    }

    private void loadCarData() {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "/cars?firebase_uid=USER_FIREBASE_UID"); // Replace USER_FIREBASE_UID dynamically
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    Scanner scanner = new Scanner(url.openStream());
                    StringBuilder jsonResponse = new StringBuilder();
                    while (scanner.hasNext()) {
                        jsonResponse.append(scanner.nextLine());
                    }
                    scanner.close();

                    Type listType = new TypeToken<List<Car>>() {}.getType();
                    List<Car> cars = new Gson().fromJson(jsonResponse.toString(), listType);

                    Platform.runLater(() -> {
                        carList.clear();
                        carList.addAll(cars);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void loadReminderData() {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "/reminders?firebase_uid=USER_FIREBASE_UID"); // Replace USER_FIREBASE_UID dynamically
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    Scanner scanner = new Scanner(url.openStream());
                    StringBuilder jsonResponse = new StringBuilder();
                    while (scanner.hasNext()) {
                        jsonResponse.append(scanner.nextLine());
                    }
                    scanner.close();

                    Type listType = new TypeToken<List<String>>() {}.getType();
                    List<String> reminders = new Gson().fromJson(jsonResponse.toString(), listType);

                    Platform.runLater(() -> {
                        reminderList.clear();
                        reminderList.addAll(reminders);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void editCar(Car car) {
        System.out.println("Editing car: " + car);
        // Implement car editing logic
    }

    private void deleteCar(Car car) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "/deleteCar/" + car.getCarId());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    Platform.runLater(() -> {
                        carList.remove(car);
                        System.out.println("Car deleted successfully: " + car);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
