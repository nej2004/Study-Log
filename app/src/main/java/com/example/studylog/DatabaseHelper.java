package com.example.studylog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    // Database Info
    private static final String DATABASE_NAME = "StudyLogDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USERS = "users";

    // User Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_CREATED_AT = "created_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_USER_NAME + " TEXT NOT NULL," +
                KEY_USER_EMAIL + " TEXT," +
                KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
        Log.d(TAG, "Database created successfully");

        // Insert a default user for testing
        insertDefaultUser(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void insertDefaultUser(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, "John Doe");
        values.put(KEY_USER_EMAIL, "john.doe@example.com");

        long userId = db.insert(TABLE_USERS, null, values);
        Log.d(TAG, "Default user inserted with ID: " + userId);
    }

    // Method to get user name (as requested in the example)
    public String getUserName() {
        SQLiteDatabase db = this.getReadableDatabase();
        String userName = "Unknown User";

        String query = "SELECT " + KEY_USER_NAME + " FROM " + TABLE_USERS + " LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                userName = cursor.getString(0);
                Log.d(TAG, "Retrieved user name: " + userName);
            } else {
                Log.w(TAG, "No user found in database");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting user name: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return userName;
    }

    // Method to get user by ID
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        try {
            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_EMAIL)));
                user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CREATED_AT)));

                Log.d(TAG, "Retrieved user: " + user.getName());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting user by ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }

    // Method to get the first user (for profile display)
    public User getFirstUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM " + TABLE_USERS + " LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_EMAIL)));
                user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CREATED_AT)));

                Log.d(TAG, "Retrieved first user: " + user.getName());
            } else {
                Log.w(TAG, "No users found in database");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting first user: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }

    // Method to add a new user
    public long addUser(String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, name);
        values.put(KEY_USER_EMAIL, email);

        long userId = db.insert(TABLE_USERS, null, values);
        Log.d(TAG, "User added with ID: " + userId);

        return userId;
    }

    // Method to update user name
    public int updateUserName(int userId, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, newName);

        int rowsUpdated = db.update(TABLE_USERS, values, KEY_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
        Log.d(TAG, "Updated " + rowsUpdated + " user records");

        return rowsUpdated;
    }
}
