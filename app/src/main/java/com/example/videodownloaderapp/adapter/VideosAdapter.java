package com.example.videodownloaderapp.adapter;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videodownloaderapp.R;
import com.example.videodownloaderapp.room.AppDatabase;
import com.example.videodownloaderapp.room.Video;
import com.example.videodownloaderapp.view.VideoActivity;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    List<Video> mListVideos;
    Context mContext;
    boolean isDownload = false;


    public VideosAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addList(List<Video> videos) {
        this.mListVideos = videos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Video video = mListVideos.get(position);

        holder.mTextViewTitle.setText(video.getTitle());
        holder.mTextViewSubtitle.setText(video.getSubtitle());
        holder.mTextViewDescription.setText(video.getDescription());

        holder.progressBar.setVisibility(View.GONE);
        Glide.with(mContext).load(video.getThumb()).into(holder.mImageViewThumb);

        if (video.isDownload()) {
            holder.mButtonDownload.setText("Play");
        } else {
            holder.mButtonDownload.setText("Download");
        }
        holder.mButtonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (video.isDownload() == false) {
                    holder.mButtonDownload.setText("Download");
                    //when downloading that time button is disable
                    holder.mButtonDownload.setEnabled(false);
                    startDownload(holder, video, position);


                } else {
                    holder.mButtonDownload.setText("Play");
                    Intent intent = new Intent(mContext, VideoActivity.class);
                    intent.putExtra("video",video);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListVideos.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageViewThumb;
        MaterialButton mButtonDownload;
        TextView mTextViewTitle, mTextViewSubtitle, mTextViewDescription;
        ProgressBar progressBar;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageViewThumb = itemView.findViewById(R.id.img_thumb);
            mTextViewTitle = itemView.findViewById(R.id.text_title);
            mTextViewSubtitle = itemView.findViewById(R.id.text_subtitle);
            mTextViewDescription = itemView.findViewById(R.id.text_desc);
            mButtonDownload = itemView.findViewById(R.id.btn_download);
            progressBar = itemView.findViewById(R.id.progress_bar);

        }
    }

    private void startDownload(VideoViewHolder holder, Video video, int position) {
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.progressBar.setProgress(0);

        DownloadManager downloadManager;
        ExecutorService executor = Executors.newFixedThreadPool(10);

        Handler handler = new Handler(Looper.getMainLooper());

        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);


        Uri uri = Uri.parse(video.getSources());
        DownloadManager.Request downloadRequest = new DownloadManager.Request(uri);

        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDescription("Downloading...")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, video.getTitle())
                .setAllowedOverMetered(true);

        long downloadId = downloadManager.enqueue(downloadRequest);

       // videoId = downloadId;

        executor.execute(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                boolean finishDownload = false;

                while (!finishDownload) {
                    DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(query);

                    if (cursor != null && cursor.moveToFirst()) {
                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                        switch (status) {
                            case DownloadManager.STATUS_FAILED:
                                finishDownload = true;
                                break;
                            case DownloadManager.STATUS_PAUSED:
                                break;
                            case DownloadManager.STATUS_RUNNING:

                                final long total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                if (total > 0) {
                                    final long downloaded = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                    final int progress = (int) ((downloaded * 100L) / total);

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            holder.progressBar.setProgress(progress);

                                        }
                                    });
                                }
                                break;
                            case DownloadManager.STATUS_SUCCESSFUL:
                                finishDownload = true;

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder.progressBar.setProgress(100);
                                        holder.progressBar.setVisibility(View.GONE);

                                        //enable button
                                        holder.mButtonDownload.setEnabled(true);
                                        holder.mButtonDownload.setText("Play");

                                        // Set video as downloaded
                                        isDownload = true;
                                        AppDatabase.getDatabaseInstance(mContext).videoDao().updateData(video.getId(), isDownload, uri);
                                        video.setDownload(true);
                                        video.setVideoId(uri);
                                        notifyItemChanged(position);

                                        Toast.makeText(mContext, "Download complete", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                        }
                        cursor.moveToNext();
                    }
                }
            }
        });
    }
}