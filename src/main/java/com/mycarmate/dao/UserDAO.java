package com.mycarmate.dao;

import com.mycarmate.models.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, String> fetchUserProfile(String firebaseUid) throws Exception {
        String query = "SELECT username, profile_picture FROM users WHERE firebase_uid = ?";
        Map<String, String> userProfile = new HashMap<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, firebaseUid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userProfile.put("username", rs.getString("username"));
                    userProfile.put("profile_picture", rs.getString("profile_picture"));
                }
            }
        }

        return userProfile;
    }

    public static String fetchFirebaseUidByUserId(int userId) {
        String firebaseUid = null;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT firebase_uid FROM users WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        firebaseUid = rs.getString("firebase_uid");
                        System.out.println("Fetched Firebase UID: " + firebaseUid + " for User ID: " + userId);
                    } else {
                        System.err.println("No Firebase UID found for User ID: " + userId);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching Firebase UID: " + e.getMessage());
            e.printStackTrace();
        }

        return firebaseUid;
    }


    public Map<String, String> fetchUserDetailsById(int userId) throws Exception {
        String query = "SELECT first_name, last_name, username, profile_picture FROM users WHERE user_id = ?";
        Map<String, String> userDetails = new HashMap<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userDetails.put("first_name", rs.getString("first_name"));
                    userDetails.put("last_name", rs.getString("last_name"));
                    userDetails.put("username", rs.getString("username"));
                    userDetails.put("profile_picture", rs.getString("profile_picture"));
                }
            }
        }

        return userDetails.isEmpty() ? null : userDetails;
    }



    public void updateUserProfile(int userId, String firstName, String lastName, String username, String profilePicture) throws Exception {
        String query = "UPDATE users SET first_name = ?, last_name = ?, username = ?, profile_picture = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, username);
            stmt.setString(4, profilePicture);
            stmt.setInt(5, userId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User profile updated successfully.");
            } else {
                throw new Exception("Failed to update user profile.");
            }
        }
    }

    public void insertUser(String firebaseUid, String firstName, String lastName, String username, String email) throws Exception {
        String query = "INSERT INTO users (firebase_uid, first_name, last_name, username, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firebaseUid);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, username);
            statement.setString(5, email);
            statement.executeUpdate();
        }
    }

}