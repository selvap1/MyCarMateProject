package com.mycarmate.controllers;

import com.mycarmate.dao.CarDAO;
import com.mycarmate.dao.MaintenanceDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class MaintenanceRecordsController {

    @FXML
    private ComboBox<String> carSelector;

    @FXML
    private TableView<MaintenanceRecord> recordsTable;

    @FXML
    private TableColumn<MaintenanceRecord, String> dateColumn;

    @FXML
    private TableColumn<MaintenanceRecord, String> serviceTypeColumn;

    @FXML
    private TableColumn<MaintenanceRecord, Double> costColumn;

    @FXML
    private TableColumn<MaintenanceRecord, Integer> mileageColumn;

    private ObservableList<MaintenanceRecord> records = FXCollections.observableArrayList();
    private int loggedInUserId;

    /**
     * Sets the logged-in user ID and populates the car selector.
     * @param userId The user ID of the logged-in user.
     */
    public void setLoggedInUserId(int userId) {
        this.loggedInUserId = userId;
        populateCarSelector();
    }

    /**
     * Initializes the maintenance records page.
     */
    @FXML
    private void initialize() {
        // Initialize table columns
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        serviceTypeColumn.setCellValueFactory(cellData -> cellData.getValue().serviceTypeProperty());
        costColumn.setCellValueFactory(cellData -> cellData.getValue().costProperty().asObject());
        mileageColumn.setCellValueFactory(cellData -> cellData.getValue().mileageProperty().asObject());

        // Set specific column widths
        dateColumn.setPrefWidth(150);
        serviceTypeColumn.setPrefWidth(150);
        costColumn.setPrefWidth(100);
        mileageColumn.setPrefWidth(100);

        // Constrain resize policy to avoid empty columns
        recordsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        recordsTable.setItems(records);

        // Add listener to carSelector
        carSelector.setOnAction(event -> fetchRecordsForSelectedCar());
    }

    /**
     * Populates the car selector with cars belonging to the logged-in user.
     */
    private void populateCarSelector() {
        try {
            CarDAO carDAO = new CarDAO();
            List<String> userCars = carDAO.fetchCarNamesForUser(loggedInUserId); // Fetch car names

            ObservableList<String> carOptions = FXCollections.observableArrayList(userCars);
            carSelector.setItems(carOptions);
        } catch (Exception e) {
            System.err.println("Error populating car dropdown: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Fetches maintenance records for the selected car.
     */
    private void fetchRecordsForSelectedCar() {
        String selectedCar = carSelector.getValue();
        if (selectedCar == null || selectedCar.isEmpty()) {
            System.err.println("No car selected.");
            return;
        }

        try {
            // Fetch the car ID corresponding to the selected car name for the logged-in user
            CarDAO carDAO = new CarDAO();
            int carId = carDAO.fetchCarIdByName(selectedCar, loggedInUserId); // Adjust this to your method signature
            System.out.println("Fetched car ID for selected car: " + carId);

            // Fetch maintenance records for the fetched car ID
            MaintenanceDAO maintenanceDAO = new MaintenanceDAO();
            List<MaintenanceRecord> fetchedRecords = maintenanceDAO.fetchRecordsByCarId(carId);
            System.out.println("Fetched maintenance records for car ID " + carId + ": " + fetchedRecords);

            // Update the ObservableList and table
            records.clear();
            records.addAll(fetchedRecords);
            recordsTable.setItems(records);

        } catch (Exception e) {
            System.err.println("Error fetching records for selected car: " + e.getMessage());
            e.printStackTrace();
        }
    }



    /**
     * Handles adding a new maintenance record.
     */
    @FXML
    private void handleAddRecord() {
        String selectedCar = carSelector.getValue();
        if (selectedCar == null || selectedCar.isEmpty()) {
            showAlert("No Car Selected", "Please select a car before adding a maintenance record.");
            return;
        }

        try {
            // Fetch the car ID for the selected car
            CarDAO carDAO = new CarDAO();
            int carId = carDAO.fetchCarIdByName(selectedCar, loggedInUserId);

            // Load the AddMaintenanceRecordPopup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddMaintenanceRecordPopup.fxml"));
            Parent root = loader.load();

            // Pass the car ID to the AddMaintenanceRecordController
            AddMaintenanceRecordController controller = loader.getController();
            controller.setCarId(carId);

            Stage stage = new Stage();
            stage.setTitle("Add Maintenance Record");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh the records table after adding a new record
            fetchRecordsForSelectedCar();

        } catch (Exception e) {
            System.err.println("Error opening Add Maintenance Record popup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles editing the selected maintenance record.
     */
    @FXML
    private void handleEditRecord() {
        MaintenanceRecord selectedRecord = recordsTable.getSelectionModel().getSelectedItem();
        if (selectedRecord == null) {
            showAlert("No Record Selected", "Please select a record to edit.");
            return;
        }

        try {
            // Load the EditMaintenanceRecordPopUp.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditMaintenanceRecordPopUp.fxml"));
            Parent root = loader.load();

            // Pass the selected record to the controller
            EditMaintenanceRecordController controller = loader.getController();
            controller.setCurrentRecord(selectedRecord);

            // Show the popup
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Maintenance Record");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh the records table after editing
            fetchRecordsForSelectedCar();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Edit Record popup.");
        }
    }


    /**
     * Handles deleting the selected maintenance record.
     */
    @FXML
    private void handleDeleteRecord() {
        MaintenanceRecord selectedRecord = recordsTable.getSelectionModel().getSelectedItem();
        if (selectedRecord == null) {
            showAlert("No Record Selected", "Please select a record to delete.");
            return;
        }

        try {
            MaintenanceDAO maintenanceDAO = new MaintenanceDAO();
            maintenanceDAO.deleteRecord(selectedRecord.getRecordId());
            records.remove(selectedRecord);
            System.out.println("Deleted record: " + selectedRecord);
        } catch (Exception e) {
            System.err.println("Error deleting maintenance record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert dialog with the specified title and content.
     * @param title   The title of the alert.
     * @param content The content of the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
