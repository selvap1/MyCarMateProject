package com.mycarmate.dao;
public class SessionManager {
    private static int loggedInUserId; // Ensure this is an int

    public static void setLoggedInUserId(int userId) {
        loggedInUserId = userId;
    }

    public static int getLoggedInUserId() {
        return loggedInUserId; // Ensure the return type is int
    }
}
