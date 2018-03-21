package com.example.shriji.androidlivewallpaper.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shriji.androidlivewallpaper.Adpters.MyRecyclerRecentAdpter;
import com.example.shriji.androidlivewallpaper.Database.DataSource.RecentRepository;
import com.example.shriji.androidlivewallpaper.Database.LocalDataBase.LocalDataBase;
import com.example.shriji.androidlivewallpaper.Database.LocalDataBase.RecentsDataSource;
import com.example.shriji.androidlivewallpaper.Database.Recents;
import com.example.shriji.androidlivewallpaper.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentsFragment extends Fragment {

    private static RecentsFragment INSTANCE = null;
    @BindView(R.id.recycle_recent)
    RecyclerView recycleRecent;
    Unbinder unbinder;

Context context;

    List<Recents> recentsList;
    MyRecyclerRecentAdpter myRecyclerRecentAdpter;

    //Room Database
    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;

    @SuppressLint("ValidFragment")
    public RecentsFragment(Context context) {
        // Required empty public constructor
            this.context = context;
        //init RoomDatabse
        compositeDisposable = new CompositeDisposable();
        LocalDataBase dataBase = LocalDataBase.getInstance(getActivity());
        recentRepository = RecentRepository
                .getInstance(RecentsDataSource.getInstance(dataBase.recentsDAO()));
    }

    public static RecentsFragment getInstanse(Context context) {
        if (INSTANCE == null)
            INSTANCE = new RecentsFragment(context);
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recents, container, false);
        unbinder = ButterKnife.bind(this, view);
        recycleRecent.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        recentsList =  new ArrayList<>();
        myRecyclerRecentAdpter = new MyRecyclerRecentAdpter(context,recentsList);
        recycleRecent.setAdapter(myRecyclerRecentAdpter);

        loadRecents();

        return view;
    }

    private void loadRecents() {

        Disposable disposable = recentRepository.getAllRecents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Recents>>() {
                    @Override
                    public void accept(List<Recents> recents) throws Exception {
                        onGetAllRecentsSucess(recents);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Error", throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);

    }

    private void onGetAllRecentsSucess(List<Recents> recents) {
            recentsList.clear();
            recentsList.addAll(recents);
            myRecyclerRecentAdpter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
