package com.mycarmate.dao;

import com.mycarmate.controllers.Car;
import com.mycarmate.models.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    /**
     * Fetch cars for a given user_id.
     *
     * @param userId The user_id to fetch cars for.
     * @return A list of cars belonging to the user.
     */
    public List<Car> fetchCarsForUser(int userId) throws Exception {
        String query = "SELECT car_id, make, model, year, vin, mileage, registration_exp_date, inspection_date FROM cars WHERE user_id = ?";
        List<Car> cars = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cars.add(new Car(
                            rs.getString("car_id"),
                            rs.getString("make"),
                            rs.getString("model"),
                            rs.getInt("year"),
                            rs.getString("vin"),
                            rs.getInt("mileage"),
                            rs.getString("registration_exp_date"),
                            rs.getString("inspection_date")
                    ));
                }
            }
        }

        System.out.println("Fetched cars from database: " + cars);
        return cars;
    }


    public Car fetchCarById(int carId) throws Exception {
        String query = "SELECT * FROM cars WHERE car_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, carId); // Use setInt to bind the integer carId
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Create a Car object using the constructor with appropriate field types
                return new Car(
                        String.valueOf(rs.getInt("car_id")), // Convert int car_id to String for the Car class
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("vin"),
                        rs.getInt("mileage"),
                        rs.getString("registration_exp_date"),
                        rs.getString("inspection_date")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching car by ID: " + e.getMessage());
            throw e;
        }
        throw new Exception("Car not found with ID: " + carId);
    }



    /**
     * Adds a new car to the database for a specific user.
     *
     * @param userId              The ID of the user adding the car.
     * @param make                The make of the car.
     * @param model               The model of the car.
     * @param year                The manufacturing year of the car.
     * @param vin                 The VIN of the car.
     * @param mileage             The mileage of the car.
     * @param registrationExpDate The registration expiration date of the car.
     * @param inspectionDate      The inspection date of the car.
     * @throws Exception If an error occurs while adding the car.
     */
    public void addCar(int userId, String make, String model, int year, String vin, int mileage, String registrationExpDate, String inspectionDate) throws Exception {
        String query = "INSERT INTO cars (user_id, make, model, year, vin, mileage, registration_exp_date, inspection_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setString(2, make);
            stmt.setString(3, model);
            stmt.setInt(4, year);
            stmt.setString(5, vin);
            stmt.setInt(6, mileage);
            stmt.setDate(7, java.sql.Date.valueOf(registrationExpDate)); // Convert to SQL DATE
            stmt.setDate(8, java.sql.Date.valueOf(inspectionDate)); // Convert to SQL DATE

            stmt.executeUpdate();
            System.out.println("Car added successfully.");
        }
    }
    public List<String> fetchCarNamesForUser(int userId) throws Exception {
        String query = "SELECT DISTINCT make || ' ' || model || ' (' || year || ')' AS car_name FROM cars WHERE user_id = ?";
        List<String> carNames = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    carNames.add(rs.getString("car_name"));
                }
            }
        }

        return carNames;
    }

    public int fetchCarIdByName(String carName, int userId) throws Exception {
        String query = "SELECT car_id FROM cars WHERE user_id = ? AND CONCAT(make, ' ', model, ' (', year, ')') = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, carName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("car_id");
                } else {
                    throw new Exception("Car ID not found for car name: " + carName);
                }
            }
        }
    }
    public void updateInspectionDate(int carId, LocalDate newInspectionDate) throws Exception {
        String query = "UPDATE cars SET inspection_date = ? WHERE car_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(newInspectionDate));
            stmt.setInt(2, carId);
            stmt.executeUpdate();
            System.out.println("Updated inspection_date for car ID: " + carId);
        } catch (Exception e) {
            System.err.println("Error updating inspection_date: " + e.getMessage());
            throw e;
        }
    }

    public void updateRegistrationDate(int carId, LocalDate newRegistrationDate) throws Exception {
        String query = "UPDATE cars SET registration_exp_date = ? WHERE car_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(newRegistrationDate));
            stmt.setInt(2, carId);
            stmt.executeUpdate();
            System.out.println("Updated registration_exp_date for car ID: " + carId);
        } catch (Exception e) {
            System.err.println("Error updating registration_exp_date: " + e.getMessage());
            throw e;
        }
    }

    public int fetchCarMileage(int carId) throws Exception {
        String query = "SELECT mileage FROM cars WHERE car_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, carId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("mileage");
                } else {
                    throw new Exception("Car not found for car_id: " + carId);
                }
            }
        }
    }

    public void updateCarMileage(int carId, int newMileage) throws Exception {
        String query = "UPDATE cars SET mileage = ? WHERE car_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, newMileage);
            stmt.setInt(2, carId);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated == 0) {
                throw new Exception("Failed to update mileage for car_id: " + carId);
            }
        }
    }

    public List<String> fetchUpcomingCarReminders(int userId) throws Exception {
        String query = "SELECT make, model, year, registration_exp_date, inspection_date FROM cars WHERE user_id = ?";
        List<String> reminders = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String make = rs.getString("make");
                    String model = rs.getString("model");
                    int year = rs.getInt("year");

                    // Handle registration expiration date
                    java.sql.Date registrationExpDate = rs.getDate("registration_exp_date");
                    if (registrationExpDate != null) {
                        LocalDate regDate = registrationExpDate.toLocalDate();
                        System.out.println("Processing registration expiration date: " + regDate);

                        if (regDate.isBefore(LocalDate.now().plusMonths(1)) || regDate.isEqual(LocalDate.now().plusMonths(1))) {
                            reminders.add("Registration for " + make + " " + model + " (" + year + ") is expiring on " + regDate + "!");
                        }
                    }

                    // Handle inspection expiration date
                    java.sql.Date inspectionDate = rs.getDate("inspection_date");
                    if (inspectionDate != null) {
                        LocalDate inspDate = inspectionDate.toLocalDate();
                        System.out.println("Processing inspection expiration date: " + inspDate);

                        if (inspDate.isBefore(LocalDate.now().plusMonths(1)) || inspDate.isEqual(LocalDate.now().plusMonths(1))) {
                            reminders.add("Inspection for " + make + " " + model + " (" + year + ") is expiring on " + inspDate + "!");
                        }
                    }
                }
            }
        }
        return reminders;
    }

    public void updateCar(Car car) throws Exception {
        String query = "UPDATE cars SET make = ?, model = ?, year = ?, vin = ? WHERE car_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, car.getMake());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setString(4, car.getVin());
            stmt.setString(5, car.getCarId());

            stmt.executeUpdate();
        }
    }

    public void deleteCar(int carId) throws Exception {
        String query = "DELETE FROM cars WHERE car_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, carId);
            stmt.executeUpdate();
            System.out.println("Car with ID " + carId + " has been deleted from the database.");
        } catch (Exception e) {
            System.err.println("Error deleting car: " + e.getMessage());
            throw e;
        }
    }





}
