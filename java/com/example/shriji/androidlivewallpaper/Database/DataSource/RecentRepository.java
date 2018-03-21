package com.example.shriji.androidlivewallpaper.Database.DataSource;

import com.example.shriji.androidlivewallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by shriji on 16/3/18.
 */

public class RecentRepository implements IRecentDataSource {

    private IRecentDataSource mLocalDataSource;
    private static RecentRepository instance;

    public RecentRepository(IRecentDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static RecentRepository getInstance(IRecentDataSource mLocalDataSource) {
        if(instance == null){
            instance = new RecentRepository(mLocalDataSource);
        }
        return instance;
    }



    @Override
    public Flowable<List<Recents>> getAllRecents() {
        return mLocalDataSource.getAllRecents();
    }

    @Override
    public void insertRecents(Recents... recents) {
        mLocalDataSource.insertRecents(recents);
    }

    @Override
    public void updateRecents(Recents... recents) {
        mLocalDataSource.updateRecents(recents);
    }

    @Override
    public void deleteRecents(Recents... recents) {
        mLocalDataSource.deleteRecents(recents);
    }

    @Override
    public void deleteAllRecents() {
        mLocalDataSource.deleteAllRecents();
    }
}
