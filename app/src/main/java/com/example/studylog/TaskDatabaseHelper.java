package com.example.studylog;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "studylog.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASK_NAME = "task_name";
    public static final String COLUMN_CREATED_AT = "created_at";

    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_NAME + " TEXT NOT NULL, " +
                COLUMN_CREATED_AT + " LONG NOT NULL)";
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public long insertTask(String taskName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskName);
        values.put(COLUMN_CREATED_AT, System.currentTimeMillis());
        return db.insert(TABLE_TASKS, null, values);
    }

    public java.util.List<Task> getAllTasks() {
        java.util.List<Task> tasks = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        android.database.Cursor cursor = db.query(
                TABLE_TASKS,
                null,
                null,
                null,
                null,
                null,
                COLUMN_CREATED_AT + " DESC"
        );
        
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String taskName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_NAME));
                long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT));
                
                tasks.add(new Task(id, taskName, createdAt));
            } while (cursor.moveToNext());
        }
        cursor.close();
        
        return tasks;
    }
}

