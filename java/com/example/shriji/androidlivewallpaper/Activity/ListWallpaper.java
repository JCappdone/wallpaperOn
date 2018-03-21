package com.example.shriji.androidlivewallpaper.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shriji.androidlivewallpaper.Common.Common;

import com.example.shriji.androidlivewallpaper.Model.WallpaperItems;
import com.example.shriji.androidlivewallpaper.R;
import com.example.shriji.androidlivewallpaper.ViewHolder.ListWallpaperViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListWallpaper extends AppCompatActivity {


    //FirebaseUI Adapter
    FirebaseRecyclerOptions<WallpaperItems> options;
    FirebaseRecyclerAdapter<WallpaperItems, ListWallpaperViewHolder> adapter;
    @BindView(R.id.recycle_ist_wallpaper)
    RecyclerView recycleIstWallpaper;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_wallpaper);
        ButterKnife.bind(this);

        toolbar.setTitle(Common.CATEGORY_SELECTED);
        toolbar.setTitleTextColor(Color.BLUE);
        setSupportActionBar(toolbar);
        Toast.makeText(this, Common.CATEGORY_SELECTED, Toast.LENGTH_SHORT).show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

      //  recycleIstWallpaper.setHasFixedSize(true);
        recycleIstWallpaper.setLayoutManager(new LinearLayoutManager(this));
        loadBackGroundList();


    }

    private void loadBackGroundList() {
       Query query = FirebaseDatabase.getInstance().getReference(Common.STR_WALLPAPER)
                .orderByChild("categoryID").equalTo(Common.CATEGORY_ID_SELECTED);


        options = new FirebaseRecyclerOptions.Builder<WallpaperItems>()
                .setQuery(query, WallpaperItems.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<WallpaperItems, ListWallpaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, int position, @NonNull final WallpaperItems model) {

                Picasso.get()
                        .load(model.getImgLink())
                        .into(holder.imgWallpaper);
                holder.textView2.setText(model.getCategoryID());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ListWallpaper.this,ViewWallpapers.class);
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
                return new ListWallpaperViewHolder(itemView);
            }
        };
        adapter.startListening();
        recycleIstWallpaper.setAdapter(adapter);


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
        if (adapter != null) {
            adapter.stopListening();
        }
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); //close activity when click back button
        }
        return super.onOptionsItemSelected(item);

    }
}
