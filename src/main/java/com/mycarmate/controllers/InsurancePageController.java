package com.mycarmate.controllers;

import com.mycarmate.dao.CarDAO;
import com.mycarmate.dao.InsuranceDAO;
import com.mycarmate.dao.SessionManager;
import com.mycarmate.dao.UserDAO;
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
import java.text.DecimalFormat;
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
    private TableColumn<InsuranceRecord, String> coverageAmountColumn;
    @FXML
    private TableColumn<InsuranceRecord, Void> actionsColumn;
    @FXML
    private Button backToDashboardButton;

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
        coverageAmountColumn.setCellValueFactory(cellData -> {
            double amount = cellData.getValue().getCoverageAmount();
            String formattedAmount = String.format("%,.2f", amount);
            return new SimpleStringProperty(formattedAmount);
        });

        insuranceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


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


        // Populate pickers
        populateMonthPicker(startMonthPicker);
        populateYearPicker(startYearPicker);
        populateMonthPicker(endMonthPicker);
        populateYearPicker(endYearPicker);

        // Add focus listener to format coverage amount
        coverageAmountField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // On losing focus
                formatCoverageField();
            }
        });

        insuranceTable.setItems(insuranceRecords);
    }


    private void populateMonthPicker(ComboBox<String> monthPicker) {
        monthPicker.getItems().addAll(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
    }

    private void populateYearPicker(ComboBox<Integer> yearPicker) {
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 20; i <= currentYear + 20; i++) {
            yearPicker.getItems().add(i);
        }
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
            double coverageAmount = parseCoverageAmount(coverageAmountField.getText());

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

    private double parseCoverageAmount(String coverageText) throws NumberFormatException {
        return Double.parseDouble(coverageText.replace(",", ""));
    }

    private void formatCoverageField() {
        try {
            String text = coverageAmountField.getText();
            if (!text.isEmpty()) {
                double value = parseCoverageAmount(text);
                coverageAmountField.setText(formatCoverageAmount(value));
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for the coverage amount.");
        }
    }

    private String formatCoverageAmount(double coverageAmount) {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        return formatter.format(coverageAmount);
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

    @FXML
    private void navigateToDashboard() {
        try {
            // Retrieve firebase_uid from user_id
            int userId = SessionManager.getLoggedInUserId();
            UserDAO userDAO = new UserDAO();
            String firebaseUid = userDAO.fetchFirebaseUidByUserId(userId);

            if (firebaseUid != null) {
                // Load the DashboardPage.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DashboardPage.fxml"));
                Parent root = loader.load();

                // Get the controller and initialize with firebaseUid
                DashboardController dashboardController = loader.getController();
                dashboardController.initializeDashboard(firebaseUid);

                // Switch to the Dashboard scene
                Stage stage = (Stage) backToDashboardButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Dashboard");
            } else {
                System.err.println("Unable to navigate: Firebase UID is null for User ID: " + userId);
            }
        } catch (IOException e) {
            System.err.println("Error navigating to Dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
