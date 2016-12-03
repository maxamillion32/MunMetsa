package com.example.asus.munmestsa0_1.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Asus on 3.12.2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tabTitles =  new ArrayList<>();


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragments(Fragment fragments,String titles){
        this.fragments.add(fragments);
        this.tabTitles.add(titles);

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles.get(position);
    }

}
