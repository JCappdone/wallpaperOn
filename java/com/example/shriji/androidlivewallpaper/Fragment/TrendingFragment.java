package com.example.shriji.androidlivewallpaper.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shriji.androidlivewallpaper.Activity.ListWallpaper;
import com.example.shriji.androidlivewallpaper.Activity.ViewWallpapers;
import com.example.shriji.androidlivewallpaper.Common.Common;
import com.example.shriji.androidlivewallpaper.Model.CategoryItem;
import com.example.shriji.androidlivewallpaper.Model.WallpaperItems;
import com.example.shriji.androidlivewallpaper.R;
import com.example.shriji.androidlivewallpaper.ViewHolder.ListWallpaperViewHolder;
import com.example.shriji.androidlivewallpaper.ViewHolder.categoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {

    private static TrendingFragment INSTANCE = null;

    @BindView(R.id.recycle_trending)
    RecyclerView recycleTrending;
    Unbinder unbinder;

    FirebaseRecyclerOptions<WallpaperItems> options;
    FirebaseRecyclerAdapter<WallpaperItems, ListWallpaperViewHolder> adapter;

    public TrendingFragment() {
        // Required empty public constructor

        Query query = FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPER)
                .orderByChild("viewCount")
                .limitToLast(3);  //get  10 item have biggest view count

        options = new FirebaseRecyclerOptions.Builder<WallpaperItems>()
                .setQuery(query, WallpaperItems.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<WallpaperItems, ListWallpaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, final int position, @NonNull final WallpaperItems model) {
                Picasso.get()
                        .load(model.getImgLink())
                        .into(holder.imgWallpaper);

              //  holder.txtName.setText(model.getName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),ViewWallpapers.class);
                        Common.select_background = model;
                        Common.select_background_key = adapter.getRef(position).getKey();
                        startActivity(intent);
                    }
                });
            }

                @NonNull
                @Override
                public ListWallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.layout_wallpaper_item, parent, false);
                    int height =  parent.getMeasuredHeight()/2;
                    itemView.setMinimumHeight(height);
                    return new ListWallpaperViewHolder(itemView);
                }
            };
    }

    public static TrendingFragment getInstanse() {
        if (INSTANCE == null)
            INSTANCE = new TrendingFragment();
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_popular, container, false);
        unbinder = ButterKnife.bind(this, view);
        recycleTrending.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recycleTrending.setLayoutManager(layoutManager);

        loadTrendingList();
        return view;
    }

    private void loadTrendingList() {
        adapter.startListening();
        recycleTrending.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }

    }

}
