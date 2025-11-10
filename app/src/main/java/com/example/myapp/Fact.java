package com.example.myapp;

public class Fact {
    private String text;
    private String imageUrl;

    public Fact(String text, String imageUrl) {
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
