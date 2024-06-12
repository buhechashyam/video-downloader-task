package com.example.videodownloaderapp;

import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.videodownloaderapp.model.Video;

import java.util.List;

@Dao
public interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addVideosList(Video video);

    @Query("UPDATE videos SET isDownload = :isDownload, videoId= :vidId WHERE id = :id ")
    public void updateData(int id, boolean isDownload, long vidId);

    @Query("SELECT * FROM videos")
    public List<Video> getAllVideos();
}
