package com.example.shriji.androidlivewallpaper.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

/**
 * Created by shriji on 16/3/18.
 */


@Entity(tableName = "recents", primaryKeys = {"imageLink", "categoryID"})
public class Recents {

    @ColumnInfo(name = "imageLink")
    @NonNull
    private String imageLink;

    @ColumnInfo(name = "categoryID")
    @NonNull
    private String categoryID;

    @ColumnInfo(name = "saveTime")
    private String saveTime;

    @ColumnInfo(name = "key")
    private String key;

    public Recents(@NonNull String imageLink, @NonNull String categoryID, String saveTime, String key) {
        this.imageLink = imageLink;
        this.categoryID = categoryID;
        this.saveTime = saveTime;
        this.key = key;
    }

    public Recents() {
    }

    @NonNull
    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(@NonNull String imageLink) {
        this.imageLink = imageLink;
    }

    @NonNull
    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(@NonNull String categoryID) {
        this.categoryID = categoryID;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
