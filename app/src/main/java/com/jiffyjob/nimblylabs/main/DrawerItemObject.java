package com.jiffyjob.nimblylabs.main;

import android.graphics.drawable.Drawable;

import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity.MenuItemEnum;

/**
 * Created by NimblyLabs on 20/1/2015.
 */
public class DrawerItemObject {
    public Drawable iconDrawable;
    public MenuItemEnum menuItem;

    public DrawerItemObject(Drawable icon, MenuItemEnum menuItem) {
        this.iconDrawable = icon;
        this.menuItem = menuItem;
    }
}
