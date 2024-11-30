package com.mycarmate.controllers;

import com.mycarmate.dao.CarDAO;
import com.mycarmate.dao.UserDAO;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class DashboardController {

    @FXML
    private ImageView profileImage;

    @FXML
    private Label welcomeMessage;

    @FXML
    private TableView<Car> carsTable;

    @FXML
    private TableView<String> remindersTable;

    @FXML
    private TableColumn<Car, String> makeColumn;

    @FXML
    private TableColumn<Car, String> modelColumn;

    @FXML
    private TableColumn<Car, Integer> yearColumn;

    @FXML
    private TableColumn<Car, String> actionsColumn;

    @FXML
    private TableColumn<String, String> reminderColumn;

    private ObservableList<Car> cars = FXCollections.observableArrayList();
    private ObservableList<String> reminders = FXCollections.observableArrayList();

    private int loggedInUserId;

    /**
     * Set the logged-in user ID and fetch associated cars.
     * @param userId The resolved user ID.
     */
    public void setLoggedInUserId(int userId) {
        this.loggedInUserId = userId;
        System.out.println("Dashboard initialized for user ID: " + userId);

        // Fetch and display cars for the user_id
        fetchCarsForUser(userId);
    }

    /**
     * Initialize the dashboard using the Firebase UID.
     * @param firebaseUid The Firebase UID of the logged-in user.
     */
    @FXML
    public void initializeDashboard(String firebaseUid) {
        System.out.println("Initializing dashboard for Firebase UID: " + firebaseUid);

        // Resolve user_id from Firebase UID
        UserDAO userDAO = new UserDAO();
        int userId = userDAO.fetchUserIdFromFirebaseUid(firebaseUid);

        if (userId != -1) {
            setLoggedInUserId(userId); // Set userId and fetch cars
        } else {
            System.err.println("Failed to resolve user_id for Firebase UID: " + firebaseUid);
        }

        // Set the profile image (default or fetched from profile data)
        profileImage.setImage(new Image("/assets/account_circle.png"));

        // Example: Set username dynamically
        welcomeMessage.setText("Welcome User to your Dashboard!");

        // Set up table columns
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Set column widths
        makeColumn.setPrefWidth(150);
        modelColumn.setPrefWidth(150);
        yearColumn.setPrefWidth(100);
        actionsColumn.setPrefWidth(150);

        // Apply constrained resize policy
        carsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        // Set the ObservableList to the TableView
        carsTable.setItems(cars);

        // Set up actions column
        actionsColumn.setCellFactory(col -> new TableCell<Car, String>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonContainer = new HBox(5, editButton, deleteButton);

            {
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");
            }


            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }

        });

        // Set up reminders table
        reminderColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue()));
        remindersTable.setItems(reminders);

        // Load static reminders (replace with dynamic logic if needed)
        loadReminders();
    }

    /**
     * Fetch and display cars for the given user_id.
     * @param userId The user ID.
     */
    private void fetchCarsForUser(int userId) {
        CarDAO carDAO = new CarDAO();
        try {
            List<Car> userCars = carDAO.fetchCarsForUser(userId);

            cars.clear(); // Clear existing data
            cars.addAll(userCars); // Add fetched cars to the observable list
            System.out.println("Cars added to ObservableList: " + cars);

            carsTable.setItems(cars); // Update table view
        } catch (Exception e) {
            System.err.println("Error fetching cars for user: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void loadReminders() {
        // Example reminders; replace with actual logic to fetch reminders
        reminders.addAll("Oil change due soon", "Insurance expiring in 10 days");
    }

    private void editCar(Car car) {
        try {
            // Fetch full car details using the CarDAO
            CarDAO carDAO = new CarDAO();
            Car fullCarDetails = carDAO.fetchCarById(car.getCarId());

            // Open the edit modal and pass the full car details
            openEditModal(fullCarDetails);
        } catch (Exception e) {
            System.err.println("Error fetching full car details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openEditModal(Car car) {
        // Implement the logic to open the modal and populate it with the car details
        System.out.println("Editing car: " + car);
    }

    private void deleteCar(Car car) {
        // Delete car logic
        cars.remove(car);
        System.out.println("Deleted car: " + car);
    }

    @FXML
    private void openProfileModal() {
        System.out.println("Profile Modal Opened");
    }

    @FXML
    private void openAddCarPopup() {
        try {
            // Load the AddCarPopup FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddCarPopup.fxml"));
            Parent root = loader.load();

            // Optionally pass the loggedInUserId to the AddCarController
            AddCarController addCarController = loader.getController();
            addCarController.setLoggedInUserId(loggedInUserId);

            // Create and show a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Add Car");
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();

            // Refresh the car table after adding a car
            fetchCarsForUser(loggedInUserId);

        } catch (Exception e) {
            System.err.println("Error opening Add Car popup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateToMaintenance() {
        System.out.println("Navigating to Maintenance Page");
    }

    @FXML
    private void navigateToInsurance() {
        System.out.println("Navigating to Insurance Page");
    }
}
