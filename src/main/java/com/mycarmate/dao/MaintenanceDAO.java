package com.mycarmate.dao;

import com.mycarmate.controllers.MaintenanceRecord;
import com.mycarmate.models.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MaintenanceDAO {

    public List<MaintenanceRecord> fetchRecordsByCarId(int carId) throws Exception {
        String query = "SELECT * FROM maintenance_records WHERE car_id = ?";
        List<MaintenanceRecord> records = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, carId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MaintenanceRecord record = new MaintenanceRecord(
                            rs.getInt("record_id"),
                            rs.getInt("car_id"),
                            rs.getString("date_of_service"),
                            rs.getString("service_type"),
                            rs.getDouble("cost"),
                            rs.getInt("mileage_at_service"),
                            rs.getString("created_at")
                    );
                    records.add(record);
                }
            }
        }
        return records;
    }

    public void addMaintenanceRecord(int carId, LocalDate dateOfService, String serviceType, double cost, int mileage) throws Exception {
        String query = "INSERT INTO maintenance_records (car_id, date_of_service, service_type, cost, mileage_at_service, created_at) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, carId);
            stmt.setDate(2, java.sql.Date.valueOf(dateOfService)); // Convert LocalDate to java.sql.Date
            stmt.setString(3, serviceType);
            stmt.setDouble(4, cost);
            stmt.setInt(5, mileage);

            stmt.executeUpdate();
            System.out.println("Maintenance record added successfully.");
        } catch (Exception e) {
            System.err.println("Error adding maintenance record: " + e.getMessage());
            throw e;
        }
    }

    public void updateMaintenanceRecord(int recordId, LocalDate dateOfService, String serviceType, double cost, int mileage) throws Exception {
        String query = "UPDATE maintenance_records SET date_of_service = ?, service_type = ?, cost = ?, mileage_at_service = ? WHERE record_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, java.sql.Date.valueOf(dateOfService));
            stmt.setString(2, serviceType);
            stmt.setDouble(3, cost);
            stmt.setInt(4, mileage);
            stmt.setInt(5, recordId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new Exception("Failed to update maintenance record for record_id: " + recordId);
            }
        }
    }


    public void deleteRecord(int recordId) {
        String query = "DELETE FROM maintenance_records WHERE record_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, recordId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> fetchOilChangeReminders(int userId) throws Exception {
        String query = """
            SELECT c.make, c.model, c.year, MAX(m.date_of_service) AS last_oil_change_date
            FROM cars c
            LEFT JOIN maintenance_records m ON c.car_id = m.car_id
            WHERE c.user_id = ? AND (m.service_type = 'Oil Change' OR m.service_type IS NULL)
            GROUP BY c.car_id, c.make, c.model, c.year
        """;

        List<String> reminders = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String make = rs.getString("make");
                    String model = rs.getString("model");
                    int year = rs.getInt("year");
                    java.sql.Date lastOilChangeDate = rs.getDate("last_oil_change_date");

                    if (lastOilChangeDate != null) {
                        LocalDate lastOilChange = lastOilChangeDate.toLocalDate();
                        if (lastOilChange.isBefore(LocalDate.now().minusMonths(6))) {
                            reminders.add("Your oil for " + make + " " + model + " (" + year + ") may need to be changed soon!");
                        }
                    } else {
                        // No oil change record found, recommend an oil change
                        reminders.add("Your oil for " + make + " " + model + " (" + year + ") may need to be changed soon!");
                    }
                }
            }
        }

        return reminders;
    }



}
