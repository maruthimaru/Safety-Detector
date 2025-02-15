package com.soundary.saftyroute.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.soundary.saftyroute.db.dao.UserDao;
import com.soundary.saftyroute.db.table.User;

@Database(entities = {User.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "safety_detector";
    private static AppDatabase sInstance;

    public abstract UserDao userDao();


    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    Log.d(LOG_TAG, "Creating new database instance");
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, AppDatabase.DATABASE_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

}