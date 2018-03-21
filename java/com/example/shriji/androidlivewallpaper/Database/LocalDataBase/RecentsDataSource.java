package com.example.shriji.androidlivewallpaper.Database.LocalDataBase;

import com.example.shriji.androidlivewallpaper.Database.DataSource.IRecentDataSource;
import com.example.shriji.androidlivewallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by shriji on 16/3/18.
 */

public class RecentsDataSource implements IRecentDataSource {

    private RecentsDAO recentsDAO;
    private static RecentsDataSource instance;

    public RecentsDataSource(RecentsDAO recentsDAO) {
        this.recentsDAO = recentsDAO;
    }

    public static RecentsDataSource getInstance(RecentsDAO recentsDAO){
        if(instance == null){
            instance = new RecentsDataSource(recentsDAO);
        }
        return instance;
    }

    @Override
    public Flowable<List<Recents>> getAllRecents() {
        return recentsDAO.getAllRecents();
    }

    @Override
    public void insertRecents(Recents... recents) {
        recentsDAO.insertRecents(recents);

    }

    @Override
    public void updateRecents(Recents... recents) {
        recentsDAO.updateRecents(recents);

    }

    @Override
    public void deleteRecents(Recents... recents) {
        recentsDAO.deleteRecents(recents);
    }

    @Override
    public void deleteAllRecents() {
        recentsDAO.deleteAllRecents();
    }
}
