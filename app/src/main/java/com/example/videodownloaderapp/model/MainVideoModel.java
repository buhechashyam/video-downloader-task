package com.example.videodownloaderapp.model;

public class MainVideoModel {
    Video video;
    boolean isDownloaded = false;
    long videoId = 0L;

    public MainVideoModel(Video video, boolean isDownloaded, long videoId) {
        this.video = video;
        this.isDownloaded = isDownloaded;
        this.videoId = videoId;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }
}
