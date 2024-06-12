package com.example.videodownloaderapp.model;

import java.util.List;

public class Category {
    private String name;
    private List<Video> videos;

    public Category(String name, List<Video> videos) {
        this.name = name;
        this.videos = videos;
    }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public List<Video> getVideos() { return videos; }
    public void setVideos(List<Video> value) { this.videos = value; }
}
