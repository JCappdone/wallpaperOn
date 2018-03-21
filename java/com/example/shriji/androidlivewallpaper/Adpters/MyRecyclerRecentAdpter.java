package com.example.shriji.androidlivewallpaper.Adpters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shriji.androidlivewallpaper.Activity.ViewWallpapers;
import com.example.shriji.androidlivewallpaper.Common.Common;
import com.example.shriji.androidlivewallpaper.Database.Recents;
import com.example.shriji.androidlivewallpaper.Model.WallpaperItems;
import com.example.shriji.androidlivewallpaper.R;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shriji on 16/3/18.
 */

public class MyRecyclerRecentAdpter extends RecyclerView.Adapter<MyRecyclerRecentAdpter.RecentItemHolder> {

    private Context context;
    private List<Recents> recents;

    public MyRecyclerRecentAdpter(Context context, List<Recents> recents) {
        this.context = context;
        this.recents = recents;
    }

    @NonNull
    @Override
    public RecentItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_layout, parent, false);
        return new RecentItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentItemHolder holder, int position) {
        Picasso.get().load(recents.get(position).getImageLink()).into(holder.imgBG);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewWallpapers.class);
                WallpaperItems wallpaperItems = new WallpaperItems();
                wallpaperItems.setCategoryID(recents.get(position).getCategoryID());
                wallpaperItems.setImgLink(recents.get(position).getImageLink());
                Common.select_background = wallpaperItems;
                Common.select_background_key = recents.get(position).getKey();
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return recents.size();
    }

    public class RecentItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgBG)
        SelectableRoundedImageView imgBG;
        @BindView(R.id.txtName)
        TextView txtName;
        View view;
        public RecentItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }
    }
}
