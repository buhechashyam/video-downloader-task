package com.example.videodownloaderapp;

import android.app.DownloadManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadHelper {

    public static void download(String url) {
        DownloadManager downloadManager;
        ExecutorService executor = Executors.newFixedThreadPool(1);
    }

}
