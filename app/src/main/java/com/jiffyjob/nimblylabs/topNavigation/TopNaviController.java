package com.jiffyjob.nimblylabs.topNavigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;

import com.jiffyjob.nimblylabs.app.R;

/**
 * Created by himur on 6/11/2016.
 */
public class TopNaviController {
    public TopNaviController(@NonNull FragmentManager fragmentManager, @NonNull DrawerLayout drawerLayout) {
        this.fragmentManager = fragmentManager;
        this.drawerLayout = drawerLayout;
    }

    public void createBrowseTopNavi() {
        Fragment fragment = this.fragmentManager.findFragmentByTag(topNaviTag);
        BrowseTopNaviFragment browseTopNaviFragment = new BrowseTopNaviFragment();
        browseTopNaviFragment.setAttribute(drawerLayout);

        if (fragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.top_navigation, browseTopNaviFragment, topNaviTag);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.top_navigation, browseTopNaviFragment, topNaviTag);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void createBrowseIndivTopNavi() {
        Fragment fragment = this.fragmentManager.findFragmentByTag(topNaviTag);
        BrowseIndividualTopNaviFragment browseIndividualTopNaviFragment = new BrowseIndividualTopNaviFragment();
        browseIndividualTopNaviFragment.setAttribute(drawerLayout);
        if (fragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.top_navigation, browseIndividualTopNaviFragment, topNaviTag);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.top_navigation, browseIndividualTopNaviFragment, topNaviTag);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void createManageTopNavi() {
        Fragment fragment = this.fragmentManager.findFragmentByTag(topNaviTag);
        ManageTopNaviFragment manageTopNaviFragment = new ManageTopNaviFragment();
        manageTopNaviFragment.setAttribute(drawerLayout);

        if (fragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.top_navigation, manageTopNaviFragment, topNaviTag);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.top_navigation, manageTopNaviFragment, topNaviTag);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void removeTopNavi() {
        Fragment fragment = this.fragmentManager.findFragmentByTag(topNaviTag);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public static final String topNaviTag = "topNaviTag";
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
}
