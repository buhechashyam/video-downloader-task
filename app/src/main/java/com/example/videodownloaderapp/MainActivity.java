package com.example.videodownloaderapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.videodownloaderapp.databinding.ActivityMainBinding;
import com.example.videodownloaderapp.model.Video;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<Video> mListVideoDetails = new ArrayList<>();
    AppDatabase appDatabase;

    VideosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appDatabase = AppDatabase.getDatabaseInstance(this);
        String jsonString = null;

        try {
            InputStream inputStream = getAssets().open("video.json");

            int size = inputStream.available();

            byte[] bytes = new byte[size];

            inputStream.read(bytes);
            inputStream.close();

            jsonString = new String(bytes, "UTF-8");

            JSONObject jsonObject = new JSONObject(jsonString);

            JSONArray category = jsonObject.getJSONArray("categories");

            //in our json array only one category object
            JSONObject object = category.getJSONObject(0);

            JSONArray videos = object.getJSONArray("videos");

            for (int i = 0; i < videos.length(); i++) {
                JSONObject videoData = videos.getJSONObject(i);
                //get source
                JSONArray jsonArray = videoData.getJSONArray("sources");
                //get video url
                String mVideoURL = (String) jsonArray.get(0);

                List<String> source = new ArrayList<String>();
                source.add(mVideoURL);

                String title = videoData.getString("title");
                String description = videoData.getString("description");
                String subtitle = videoData.getString("subtitle");
                String thumb = videoData.getString("thumb");

                Video video = new Video(mVideoURL, thumb, subtitle, description, title, false, 0L);
                mListVideoDetails.add(video);


                appDatabase.videoDao().addVideosList(video);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        List<Video> mList = appDatabase.videoDao().getAllVideos();

        adapter = new VideosAdapter(this);
        adapter.addList(mList);
        binding.recyclerviewVideos.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewVideos.setAdapter(adapter);

        Log.d("MAIN", String.valueOf(mListVideoDetails.size()));
    }
}