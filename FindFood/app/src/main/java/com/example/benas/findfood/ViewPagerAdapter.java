package com.example.benas.findfood;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Benas on 8/14/2016.
 */
public class ViewPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                // Top Rated fragment activity
                return new AlreadyLoggedIn();
            case 1:
                return new TrucksMap();
        }
        return null;
    }

    @Override
    public int getCount() {
       return TabActivityLoader.tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TabActivityLoader.tabs[position];
    }
}
