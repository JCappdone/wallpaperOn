package com.example.shriji.androidlivewallpaper.Database.LocalDataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.shriji.androidlivewallpaper.Database.Recents;

import static com.example.shriji.androidlivewallpaper.Database.LocalDataBase.LocalDataBase.DATABASE_VERSION;

/**
 * Created by shriji on 16/3/18.
 */


@Database(entities = {Recents.class}, version = DATABASE_VERSION)
public abstract class LocalDataBase extends RoomDatabase {
    public static final int DATABASE_VERSION =1;
    public static final String DATABSE_NAME = "LiveWallpaper";

    public abstract RecentsDAO recentsDAO();
    private static LocalDataBase instance;

    public static LocalDataBase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context,LocalDataBase.class,DATABSE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;


    }



}
