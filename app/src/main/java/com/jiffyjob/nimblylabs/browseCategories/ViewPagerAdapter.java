package com.jiffyjob.nimblylabs.browseCategories;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NimblyLabs on 10/8/2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm, List<String> urlList) {
        super(fm);
        this.urlList = urlList;
    }

    @Override
    public Fragment getItem(int position) {
        FeaturedJobFragment fragment = FeaturedJobFragment.getInstance();
        fragment.setUrl(urlList.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return this.urlList.size();
    }

    private List<String> urlList = new ArrayList<>();
}
