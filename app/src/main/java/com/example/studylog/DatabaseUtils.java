package com.example.studylog;

import android.content.Context;
import android.util.Log;

/**
 * Utility class for database operations and testing
 */
public class DatabaseUtils {
    private static final String TAG = "DatabaseUtils";
    
    /**
     * Initialize database with sample data for testing
     */
    public static void initializeWithSampleData(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        
        try {
            // Check if we already have users
            String existingUser = dbHelper.getUserName();
            if ("Unknown User".equals(existingUser)) {
                // Add some sample users
                dbHelper.addUser("Alice Johnson", "alice.johnson@example.com");
                dbHelper.addUser("Bob Smith", "bob.smith@example.com");
                dbHelper.addUser("Carol Davis", "carol.davis@example.com");
                
                Log.d(TAG, "Sample users added to database");
            } else {
                Log.d(TAG, "Database already contains users");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing sample data: " + e.getMessage());
        } finally {
            dbHelper.close();
        }
    }
    
    /**
     * Get user count for debugging
     */
    public static void logUserCount(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        
        try {
            User user = dbHelper.getFirstUser();
            if (user != null) {
                Log.d(TAG, "First user in database: " + user.getName() + " (ID: " + user.getId() + ")");
            } else {
                Log.d(TAG, "No users found in database");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking user count: " + e.getMessage());
        } finally {
            dbHelper.close();
        }
    }
    
    /**
     * Clear all users (for testing purposes)
     */
    public static void clearAllUsers(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        
        try {
            dbHelper.getWritableDatabase().delete("users", null, null);
            Log.d(TAG, "All users cleared from database");
        } catch (Exception e) {
            Log.e(TAG, "Error clearing users: " + e.getMessage());
        } finally {
            dbHelper.close();
        }
    }
}
