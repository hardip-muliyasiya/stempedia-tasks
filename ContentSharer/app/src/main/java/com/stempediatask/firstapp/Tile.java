package com.stempediatask.firstapp;

public class Tile {
    private String title;
    private String imageUrl;
    private String videoUrl;

    // IMPORTANT: Firestore needs an empty constructor
    public Tile() {}

    public Tile(String title, String imageUrl, String videoUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    // --- Getters and Setters ---
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
