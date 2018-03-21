package com.example.shriji.androidlivewallpaper.Adpters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.shriji.androidlivewallpaper.Fragment.CategoryFragment;
import com.example.shriji.androidlivewallpaper.Fragment.TrendingFragment;
import com.example.shriji.androidlivewallpaper.Fragment.RecentsFragment;

/**
 * Created by shriji on 15/3/18.
 */

public class MyFragmentApdter extends FragmentPagerAdapter {

    private Context context;

    public MyFragmentApdter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CategoryFragment.getInstanse();
            case 1:
                return TrendingFragment.getInstanse();
            case 2:
                return RecentsFragment.getInstanse(context);
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Category";

            case 1:
                return "Trending";
            case 2:
                return "Recent";
        }

        return "";

    }
}
