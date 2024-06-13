package com.example.videodownloaderapp.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Video.class}, version = 1 , exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract VideoDao videoDao();

    public static AppDatabase INSTANCE = null;

    public static AppDatabase getDatabaseInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class,"video-db").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

}
