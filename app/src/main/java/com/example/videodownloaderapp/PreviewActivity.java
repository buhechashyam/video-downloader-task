package com.example.videodownloaderapp;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.videodownloaderapp.databinding.ActivityPreviewBinding;

public class PreviewActivity extends AppCompatActivity {
ActivityPreviewBinding binding;

    DownloadManager downloadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDownload();

            }
        });







    }

    private void startDownload(){

        Uri uri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        DownloadManager.Request downloadRequest = new DownloadManager.Request(uri);

        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDescription("Downloading...")
                .setAllowedOverMetered(true);

        long downloadId = downloadManager.enqueue(downloadRequest);

        boolean finishDownload = false;
        int progress;
        while (!finishDownload) {
            Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_FAILED: {
                        Toast.makeText(this, "STATUS_FAILED", Toast.LENGTH_SHORT).show();
                        finishDownload = true;
                        break;
                    }
                    case DownloadManager.STATUS_PAUSED:
                        Toast.makeText(this, "STATUS_PAUSED", Toast.LENGTH_SHORT).show();

                        break;
                    case DownloadManager.STATUS_RUNNING: {
                        Toast.makeText(this, "STATUS_RUNNING", Toast.LENGTH_SHORT).show();

                        @SuppressLint("Range") final long total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        if (total >= 0) {
                            @SuppressLint("Range") final long downloaded = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            progress = (int) ((downloaded * 100L) / total);

                            binding.progressBar.setProgress(progress);
                        }
                        break;
                    }
                    case DownloadManager.STATUS_SUCCESSFUL: {
                        Toast.makeText(this, "STATUS_SUCCESSFUL", Toast.LENGTH_SHORT).show();

                        progress = 100;
                        binding.progressBar.setProgress(progress);
                        // if you use aysnc task
                        // publishProgress(100);
                        finishDownload = true;
                        break;
                    }
                }
            }
        }
    }
}