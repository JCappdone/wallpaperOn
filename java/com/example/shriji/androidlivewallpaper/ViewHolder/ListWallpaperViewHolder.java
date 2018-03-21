package com.example.shriji.androidlivewallpaper.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.shriji.androidlivewallpaper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shriji on 15/3/18.
 */

public class ListWallpaperViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgWallpaper)
    public com.joooonho.SelectableRoundedImageView imgWallpaper;
    @BindView(R.id.textView2)
    public TextView textView2;

    View view;

    public ListWallpaperViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        view = itemView;
    }




}
