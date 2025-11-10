package com.example.studylog;

public class FactModel {
    private String text;
    private String imageUrl;

    public FactModel(String text, String imageUrl) {
        this.text = text;
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
