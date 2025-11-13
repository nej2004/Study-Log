package com.example.studylog;

public class Task {
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
}

