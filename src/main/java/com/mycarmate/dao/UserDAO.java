package com.mycarmate.dao;

import com.mycarmate.models.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class UserDAO {

    /**
     * Fetch user_id from the users table using firebase_uid.
     *
     * @param firebaseUid The Firebase UID of the logged-in user.
     * @return The user_id associated with the Firebase UID, or -1 if not found.
     */
    public static int fetchUserIdFromFirebaseUid(String firebaseUid) {
        int userId = -1; // Default for invalid case

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT user_id FROM users WHERE firebase_uid = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, firebaseUid);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("user_id");
                        System.out.println("Fetched user_id: " + userId + " for Firebase UID: " + firebaseUid);
                    } else {
                        System.err.println("No user found for Firebase UID: " + firebaseUid);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching user_id: " + e.getMessage());
            e.printStackTrace();
        }

        return userId;
    }
    }