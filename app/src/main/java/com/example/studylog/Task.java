package com.example.studylog;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Task implements Parcelable {
    // Database column names (convenience for DB helpers)
    public static final String COL_ID = "id";
    public static final String COL_TASK_NAME = "task_name";
    public static final String COL_CREATED_AT = "created_at";

    private int id;
    private String taskName;
    private long createdAt;

    public Task(String taskName) {
        this.taskName = taskName;
        this.createdAt = System.currentTimeMillis();
    }

    public Task(int id, String taskName, long createdAt) {
        this.id = id;
        this.taskName = taskName;
        this.createdAt = createdAt;
    }

    // Create from a Cursor (null-safe)
    public static Task fromCursor(Cursor c) {
        if (c == null) return null;
        int id = -1;
        long created = System.currentTimeMillis();
        String name = null;

        int idx;
        idx = c.getColumnIndex(COL_ID);
        if (idx != -1) id = c.getInt(idx);
        idx = c.getColumnIndex(COL_TASK_NAME);
        if (idx != -1) name = c.getString(idx);
        idx = c.getColumnIndex(COL_CREATED_AT);
        if (idx != -1) created = c.getLong(idx);

        return new Task(id, name, created);
    }

    // Convert to ContentValues for inserting/updating in DB
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        if (taskName != null) values.put(COL_TASK_NAME, taskName);
        values.put(COL_CREATED_AT, createdAt);
        // Do not put id for inserts (usually auto-generated) but allow for explicit updates
        if (id > 0) values.put(COL_ID, id);
        return values;
    }

    // Parcelable implementation
    protected Task(Parcel in) {
        id = in.readInt();
        taskName = in.readString();
        createdAt = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(taskName);
        dest.writeLong(createdAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id &&
                createdAt == task.createdAt &&
                Objects.equals(taskName, task.taskName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskName, createdAt);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
