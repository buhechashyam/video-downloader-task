package com.example.videodownloaderapp.model;
import java.io.IOException;
import java.util.List;

public class VideoData {
    private List<Category> categories;

    public VideoData(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> value) { this.categories = value; }
}

