package com.sustowns.sustownsapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sustowns.sustownsapp.Fragments.NewsFragment;
import com.sustowns.sustownsapp.Fragments.VideosFragment;

/**
 * Created by rk on 14-Oct-17.
 */

public class NewsTabsAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public NewsTabsAdapter(FragmentManager manager, int tabCount) {
        super(manager);
        this.mNumOfTabs = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NewsFragment tab1 = new NewsFragment();
                return tab1;

            case 1:
                VideosFragment tab2 = new VideosFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}