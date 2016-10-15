package com.jiffyjob.nimblylabs.topNavigation.Event;

import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity;

/**
 * Created by NielPC on 10/7/2016.
 */
public class ChangeMainFragmentEvent {
    public ChangeMainFragmentEvent(JiffyJobMainActivity.MenuItemEnum menuItem) {
        this.menuItem = menuItem;
    }

    public JiffyJobMainActivity.MenuItemEnum getFragmentType() {
        return menuItem;
    }

    private JiffyJobMainActivity.MenuItemEnum menuItem;
}
