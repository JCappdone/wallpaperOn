package com.example.shriji.androidlivewallpaper.Model;

/**
 * Created by shriji on 15/3/18.
 */

public class WallpaperItems {
    public String imgLink;
    public String categoryID;
    public long viewCount;

    public WallpaperItems(String imgLink, String categoryID) {
        this.imgLink = imgLink;
        this.categoryID = categoryID;
    }

    public WallpaperItems() {
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}
