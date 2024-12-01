package com.mycarmate.dao;

import com.mycarmate.models.DatabaseConnection;
import com.mycarmate.controllers.InsuranceRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsuranceDAO {

    public List<InsuranceRecord> fetchInsuranceRecordsForUser(int userId) throws Exception {
        String query = "SELECT i.insurance_id, c.make, c.model, i.provider_name, i.policy_number, " +
                "i.start_date, i.end_date, i.coverage_amount " +
                "FROM insurance i " +
                "JOIN cars c ON i.car_id = c.car_id " +
                "WHERE i.user_id = ?";
        List<InsuranceRecord> records = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String carName = rs.getString("make") + " " + rs.getString("model");
                    records.add(new InsuranceRecord(
                            rs.getInt("insurance_id"),
                            carName,
                            rs.getString("provider_name"),
                            rs.getString("policy_number"),
                            rs.getString("start_date"),
                            rs.getString("end_date"),
                            rs.getDouble("coverage_amount")
                    ));
                    System.out.println("Record fetched: " + carName + ", Coverage: " + rs.getDouble("coverage_amount"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching insurance records for user ID: " + userId);
            e.printStackTrace();
            throw e;
        }
        return records;
    }


    public void addInsurance(int userId, int carId, String providerName, String policyNumber, String startDate, String endDate, double coverageAmount) throws Exception {
        String query = "INSERT INTO insurance (user_id, car_id, provider_name, policy_number, start_date, end_date, coverage_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, carId);
            stmt.setString(3, providerName);
            stmt.setString(4, policyNumber);

            // Convert the startDate and endDate to java.sql.Date
            stmt.setDate(5, java.sql.Date.valueOf(startDate));
            stmt.setDate(6, java.sql.Date.valueOf(endDate));

            stmt.setDouble(7, coverageAmount);
            stmt.executeUpdate();
        }
    }


    public void updateInsuranceRecord(int insuranceId, String providerName, String policyNumber, String startDate, String endDate, double coverageAmount) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE insurance SET provider_name = ?, policy_number = ?, start_date = ?, end_date = ?, coverage_amount = ? WHERE insurance_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, providerName);
                statement.setString(2, policyNumber);
                statement.setDate(3, Date.valueOf(startDate)); // Convert String to java.sql.Date
                statement.setDate(4, Date.valueOf(endDate));   // Convert String to java.sql.Date
                statement.setDouble(5, coverageAmount);
                statement.setInt(6, insuranceId);
                statement.executeUpdate();
            }
        }
    }


    // Delete an insurance record
    public void deleteInsurance(int insuranceId) throws Exception {
        String query = "DELETE FROM insurance WHERE insurance_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, insuranceId);
            stmt.executeUpdate();
        }
    }
}
