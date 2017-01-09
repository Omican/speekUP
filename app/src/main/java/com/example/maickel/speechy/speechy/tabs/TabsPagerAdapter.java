package com.example.maickel.speechy.speechy.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.maickel.speechy.PresentationFragment;
import com.example.maickel.speechy.TrainingFragment;

/**
 * Created by Maickel on 1/9/2017.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new PresentationFragment();
            case 1:
                // Games fragment activity
                return new TrainingFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
