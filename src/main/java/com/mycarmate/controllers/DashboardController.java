package com.mycarmate.controllers;

import com.mycarmate.dao.CarDAO;
import com.mycarmate.dao.MaintenanceDAO;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
     *
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
     *
     * @param firebaseUid The Firebase UID of the logged-in user.
     */
    @FXML
    public void initializeDashboard(String firebaseUid) {
        System.out.println("Initializing dashboard for Firebase UID: " + firebaseUid);

        UserDAO userDAO = new UserDAO();
        try {
            // Fetch username and profile picture
            Map<String, String> userProfile = userDAO.fetchUserProfile(firebaseUid);

            // Update the welcome message with the username
            String username = userProfile.getOrDefault("username", "User");
            welcomeMessage.setText("Welcome " + username + " to your Dashboard!");

            // Update profile image
            String profilePicture = userProfile.get("profile_picture");
            if (profilePicture != null && !profilePicture.isEmpty()) {
                profileImage.setImage(new Image(profilePicture));
            } else {
                profileImage.setImage(new Image("/assets/account_circle.png"));
            }

            // Resolve user_id and fetch cars
            int userId = userDAO.fetchUserIdFromFirebaseUid(firebaseUid);
            if (userId != -1) {
                setLoggedInUserId(userId); // Set userId and fetch cars
            } else {
                System.err.println("Failed to resolve user_id for Firebase UID: " + firebaseUid);
            }
        } catch (Exception e) {
            System.err.println("Error fetching user profile or user_id: " + e.getMessage());
            e.printStackTrace();

            // Fallback to default values
            welcomeMessage.setText("Welcome User to your Dashboard!");
            profileImage.setImage(new Image("/assets/account_circle.png"));
        }

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

            {
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");

                editButton.setOnAction(event -> editCar(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> deleteCar(getTableView().getItems().get(getIndex())));
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

        // Load reminders dynamically
        loadReminders();
    }


    /**
     * Fetch and display cars for the given user_id.
     *
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
        reminders.clear();

        try {
            CarDAO carDAO = new CarDAO();
            MaintenanceDAO maintenanceDAO = new MaintenanceDAO();

            // Fetch car-related reminders
            List<String> carReminders = carDAO.fetchUpcomingCarReminders(loggedInUserId);
            System.out.println("Car reminders: " + carReminders);

            // Fetch oil change reminders
            List<String> oilChangeReminders = maintenanceDAO.fetchOilChangeReminders(loggedInUserId);
            System.out.println("Oil change reminders: " + oilChangeReminders);

            // Add all reminders to the ObservableList
            reminders.addAll(carReminders);
            reminders.addAll(oilChangeReminders);
            System.out.println("All reminders added: " + reminders);

            if (reminders.isEmpty()) {
                reminders.add("No upcoming reminders at this time.");
            }

        } catch (Exception e) {
            System.err.println("Error loading reminders: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editCar(Car car) {
        try {
            if (car == null) {
                throw new IllegalArgumentException("Car object is null");
            }

            // Fetch full car details using the CarDAO
            CarDAO carDAO = new CarDAO();
            Car fullCarDetails = carDAO.fetchCarById(Integer.parseInt(car.getCarId())); // Convert carId to int

            // Open the edit modal and pass the full car details
            openEditModal(fullCarDetails);
        } catch (NumberFormatException e) {
            System.err.println("Invalid car ID format: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error fetching full car details: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void openEditModal(Car car) {
        try {
            if (car == null) {
                throw new IllegalArgumentException("Car object is null");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditCarModal.fxml"));
            Parent root = loader.load();

            // Get controller and pass car details
            EditCarController controller = loader.getController();
            controller.setCarDetails(car); // Ensure this method correctly assigns all fields

            // Display the modal
            Stage stage = new Stage();
            stage.setTitle("Edit Car");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Optionally reload cars after editing
            fetchCarsForUser(loggedInUserId);

        } catch (Exception e) {
            System.err.println("Error opening edit modal: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void deleteCar(Car car) {
        try {
            CarDAO carDAO = new CarDAO();

            // Convert carId from String to int
            int carId = Integer.parseInt(car.getCarId());

            // Call the DAO to delete the car
            carDAO.deleteCar(carId);

            // Remove the car from the ObservableList
            cars.remove(car);
            System.out.println("Deleted car: " + car);
        } catch (NumberFormatException e) {
            System.err.println("Invalid car ID format: " + car.getCarId());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error deleting car: " + e.getMessage());
            e.printStackTrace();
        }
    }




    private void loadUserData() {
        try {
            UserDAO userDAO = new UserDAO();
            Map<String, String> userDetails = userDAO.fetchUserDetailsById(loggedInUserId);

            if (userDetails != null) {
                // Update the username
                String username = userDetails.get("username");
                welcomeMessage.setText("Welcome " + (username != null ? username : "User") + " to your Dashboard!");

                // Update the profile picture
                String profilePictureUrl = userDetails.get("profile_picture");
                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                    profileImage.setImage(new Image(profilePictureUrl));
                } else {
                    profileImage.setImage(new Image("/assets/account_circle.png")); // Default image
                }
            } else {
                System.err.println("Failed to load user data. User details not found.");
            }
        } catch (Exception e) {
            System.err.println("Error loading user data: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void openProfileModal() {
        try {
            // Load the FXML file for the Edit Profile popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditProfilePopup.fxml"));
            Parent root = loader.load();

            // Get the controller for the popup
            EditProfileController controller = loader.getController();

            // Fetch user data directly from the database
            UserDAO userDAO = new UserDAO();
            Map<String, String> userDetails = userDAO.fetchUserDetailsById(loggedInUserId);

            if (userDetails != null) {
                // Pass the user details to the controller
                controller.setLoggedInUserDetails(
                        loggedInUserId,
                        userDetails.get("first_name"),
                        userDetails.get("last_name"),
                        userDetails.get("username"),
                        userDetails.get("profile_picture")
                );
            } else {
                System.err.println("No user found with ID: " + loggedInUserId);
            }

            // Display the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Edit Profile");
            popupStage.setScene(new Scene(root));
            popupStage.initModality(Modality.APPLICATION_MODAL); // Makes the popup modal
            popupStage.showAndWait();

            // Refresh the dashboard with updated user data
            loadUserData();

        } catch (Exception e) {
            System.err.println("Error opening Edit Profile popup: " + e.getMessage());
            e.printStackTrace();
        }
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
    private void navigateToMaintenanceRecords() {
        try {
            // Load the Maintenance Records page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MaintenanceRecordsPage.fxml"));
            Parent root = loader.load();

            // Pass the loggedInUserId to MaintenanceRecordsController
            MaintenanceRecordsController maintenanceRecordsController = loader.getController();
            maintenanceRecordsController.setLoggedInUserId(loggedInUserId);

            // Set the new scene
            Stage stage = (Stage) profileImage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Maintenance Records");
            stage.show();

        } catch (Exception e) {
            System.err.println("Error navigating to Maintenance Records page: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void navigateToInsurancePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/InsurancePage.fxml"));
            Parent root = loader.load();

            InsurancePageController controller = loader.getController();
            controller.setLoggedInUserId(loggedInUserId);

            Stage stage = (Stage) profileImage.getScene().getWindow();
            Scene newScene = new Scene(root);
            stage.setTitle("View Insurance");

            // Optional: Set minimum width and height for the stage
            stage.setMinWidth(800); // Adjust these values as needed
            stage.setMinHeight(600);

            stage.setScene(newScene);

            // Ensure the stage resizes to fit the scene
            stage.sizeToScene();

            stage.centerOnScreen(); // Optional: Center the stage
            stage.show();
        } catch (Exception e) {
            System.err.println("Error navigating to InsurancePage: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleLogout() {
        try {
            // Show confirmation alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to log out?");

            // Wait for user response
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // Load the Login Page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginPage.fxml"));
                Parent root = loader.load();

                // Set the scene to the Login Page
                Stage stage = (Stage) profileImage.getScene().getWindow();
                Scene loginScene = new Scene(root);

                // Apply LoginPage.css stylesheet
                loginScene.getStylesheets().add(getClass().getResource("/styles/LoginPage.css").toExternalForm());

                stage.setScene(loginScene);
                stage.setTitle("Login");
                stage.sizeToScene(); // Adjust the stage size if necessary
            }
        } catch (IOException e) {
            System.err.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
