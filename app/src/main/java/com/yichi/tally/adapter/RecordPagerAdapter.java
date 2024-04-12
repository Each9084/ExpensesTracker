package com.yichi.tally.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class RecordPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;
        String[]titles = {"Expenses","Income"};

    public RecordPagerAdapter(@NonNull FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    //根据页面的位置 position 来返回对应位置的标题
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}