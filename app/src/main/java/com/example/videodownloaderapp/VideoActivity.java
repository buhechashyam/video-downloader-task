package com.example.videodownloaderapp;

import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;

import com.example.videodownloaderapp.databinding.ActivityVideoActivityBinding;

public class VideoActivity extends AppCompatActivity {

    ActivityVideoActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVideoActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra("title");
        String subtitle = getIntent().getStringExtra("subtitle");
        String desc = getIntent().getStringExtra("desc");

        binding.textTitle.setText(title);
        binding.textSubtitle.setText(subtitle);
        binding.textDesc.setText(desc);
        long videoId = getIntent().getLongExtra("uri", 1L);
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Uri video_uri = manager.getUriForDownloadedFile(videoId);



        binding.video.setVideoURI(video_uri);

        MediaController mediaController = new MediaController(this);
        binding.video.setMediaController(mediaController);
        // starts the video

        binding.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }
}