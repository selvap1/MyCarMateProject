package com.mycarmate.controllers;

import com.mycarmate.dao.CarDAO;
import com.mycarmate.dao.InsuranceDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class InsurancePageController {

    @FXML
    private ComboBox<String> carSelector;
    @FXML
    private TextField providerNameField, policyNumberField, coverageAmountField;
    @FXML
    private ComboBox<String> startMonthPicker, endMonthPicker;
    @FXML
    private ComboBox<Integer> startYearPicker, endYearPicker;
    @FXML
    private TableView<InsuranceRecord> insuranceTable;
    @FXML
    private TableColumn<InsuranceRecord, String> carColumn, providerNameColumn, policyNumberColumn, startDateColumn, endDateColumn;
    @FXML
    private TableColumn<InsuranceRecord, Double> coverageAmountColumn;
    @FXML
    private TableColumn<InsuranceRecord, Void> actionsColumn;

    private int loggedInUserId;
    private ObservableList<InsuranceRecord> insuranceRecords = FXCollections.observableArrayList();

    public void setLoggedInUserId(int userId) {
        this.loggedInUserId = userId;
        populateCarSelector();
        loadInsuranceRecords();
    }

    @FXML
    private void initialize() {
        carColumn.setCellValueFactory(cellData -> cellData.getValue().carNameProperty());
        providerNameColumn.setCellValueFactory(cellData -> cellData.getValue().providerNameProperty());
        policyNumberColumn.setCellValueFactory(cellData -> cellData.getValue().policyNumberProperty());
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        coverageAmountColumn.setCellValueFactory(cellData -> cellData.getValue().coverageAmountProperty().asObject());

        actionsColumn.setCellFactory(col -> {
            TableCell<InsuranceRecord, Void> cell = new TableCell<>() {
                private final Button editButton = new Button("Edit");
                private final Button deleteButton = new Button("Delete");
                private final HBox container = new HBox(5, editButton, deleteButton);

                {
                    editButton.getStyleClass().add("edit-button");
                    deleteButton.getStyleClass().add("delete-button");

                    editButton.setOnAction(event -> handleEdit(getTableView().getItems().get(getIndex())));
                    deleteButton.setOnAction(event -> handleDelete(getTableView().getItems().get(getIndex())));
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : container);
                }
            };
            return cell;
        });

        insuranceTable.setItems(insuranceRecords);
    }

    private void handleEdit(InsuranceRecord record) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditInsuranceModal.fxml"));
            Parent root = loader.load();

            EditInsuranceController controller = loader.getController();
            controller.setInsuranceRecord(record);
            controller.setInsurancePageController(this);

            Stage stage = new Stage();
            stage.setTitle("Edit Insurance Record");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error opening Edit Insurance Modal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete(InsuranceRecord record) {
        if (record == null) {
            showAlert("No Record Selected", "Please select a record to delete.");
            return;
        }

        try {
            InsuranceDAO insuranceDAO = new InsuranceDAO();
            insuranceDAO.deleteInsurance(record.getInsuranceId());

            insuranceRecords.remove(record);
        } catch (Exception e) {
            System.err.println("Error deleting insurance: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void populateCarSelector() {
        try {
            CarDAO carDAO = new CarDAO();
            List<String> userCars = carDAO.fetchCarNamesForUser(loggedInUserId);
            carSelector.setItems(FXCollections.observableArrayList(userCars));
        } catch (Exception e) {
            System.err.println("Error populating car selector: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddInsurance() {
        try {
            String carName = carSelector.getValue();
            String providerName = providerNameField.getText();
            String policyNumber = policyNumberField.getText();
            String startMonth = startMonthPicker.getValue();
            int startYear = startYearPicker.getValue();
            String endMonth = endMonthPicker.getValue();
            int endYear = endYearPicker.getValue();
            double coverageAmount = Double.parseDouble(coverageAmountField.getText());

            if (carName == null || providerName.isEmpty() || policyNumber.isEmpty() ||
                    startMonth == null || endMonth == null) {
                throw new IllegalArgumentException("All fields are required.");
            }

            String formattedStartDate = formatDateToFirstDay(startMonth, startYear);
            String formattedEndDate = formatDateToFirstDay(endMonth, endYear);

            CarDAO carDAO = new CarDAO();
            int carId = carDAO.fetchCarIdByName(carName, loggedInUserId);

            InsuranceDAO insuranceDAO = new InsuranceDAO();
            insuranceDAO.addInsurance(loggedInUserId, carId, providerName, policyNumber,
                    formattedStartDate, formattedEndDate, coverageAmount);

            loadInsuranceRecords();
        } catch (Exception e) {
            System.err.println("Error adding insurance: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String formatDateToFirstDay(String month, int year) {
        Month parsedMonth = Month.valueOf(month.toUpperCase());
        LocalDate date = LocalDate.of(year, parsedMonth.getValue(), 1);
        return date.toString();
    }

    public void loadInsuranceRecords() {
        try {
            InsuranceDAO insuranceDAO = new InsuranceDAO();
            List<InsuranceRecord> records = insuranceDAO.fetchInsuranceRecordsForUser(loggedInUserId);

            insuranceRecords.clear();
            insuranceRecords.addAll(records);

            insuranceTable.setItems(insuranceRecords); // Bind the table to the ObservableList
        } catch (Exception e) {
            System.err.println("Error loading insurance records: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
