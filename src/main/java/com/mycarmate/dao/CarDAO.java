package com.mycarmate.dao;

import com.mycarmate.controllers.Car;
import com.mycarmate.models.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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


    public Car fetchCarById(String carId) throws Exception {
        String query = "SELECT * FROM cars WHERE car_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, carId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Use the full constructor
                return new Car(
                        rs.getString("car_id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("vin"),
                        rs.getInt("mileage"),
                        rs.getString("registration_exp_date"),
                        rs.getString("inspection_date")
                );
            }
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

}
