package com.example.videodownloaderapp.view;

import android.app.DownloadManager;
import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.videodownloaderapp.databinding.ActivityVideoActivityBinding;
import com.example.videodownloaderapp.room.Video;

public class VideoActivity extends AppCompatActivity {

    ActivityVideoActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVideoActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Video video = (Video) getIntent().getSerializableExtra("video");

        binding.textTitle.setText(video.getTitle());
        binding.textSubtitle.setText(video.getSubtitle());
        binding.textDesc.setText(video.getDescription());

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Uri video_uri = Uri.parse(video.getVideoId());

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

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            binding.frameLayout.requestLayout();

        }else {

            binding.frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            binding.frameLayout.requestLayout();

        }
    }
}