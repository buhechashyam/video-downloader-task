package com.example.videodownloaderapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "videos")
public class Video {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String sources;
    private String thumb;
    private String  subtitle;
    private String description;
    private String title;

    private boolean isDownload = false;
    private long videoId = 0L;

    public Video(String sources, String thumb, String subtitle, String description, String title, boolean isDownload, long videoId) {
        this.sources = sources;
        this.thumb = thumb;
        this.subtitle = subtitle;
        this.description = description;
        this.title = title;
        this.isDownload = isDownload;
        this.videoId = videoId;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }
}
