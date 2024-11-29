package com.mycarmate.models;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    // Replace placeholders with actual values
    private static final String URL = "jdbc:postgresql://localhost:5434/car_maintenance_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "csc325project";

    // Static method to establish a database connection
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Main method for testing the connection
    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println("Connection successful!");
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
