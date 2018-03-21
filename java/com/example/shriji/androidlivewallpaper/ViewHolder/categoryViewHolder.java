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

public class categoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgBG)
    public com.joooonho.SelectableRoundedImageView imgBG;
    @BindView(R.id.txtName)
    public TextView txtName;

    View view;

    public categoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        view=itemView;

    }

}
