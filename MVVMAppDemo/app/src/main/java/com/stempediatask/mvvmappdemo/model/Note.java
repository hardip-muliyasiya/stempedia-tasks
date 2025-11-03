// This Note class is the core data structure. It's designed to be easily read
// from and written to Firestore. The id field is crucial for updates/deletes later.


package com.stempediatask.mvvmappdemo.model;

// This class represents a single Note item in our app.
public class Note {
    private String id;
    private String title;
    private String description;

    public Note() {

    }

    public Note(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    // Setters (Firestore needs these too, or can be omitted if you only deserialize)
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
