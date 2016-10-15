package com.jiffyjob.nimblylabs.topNavigation.Event;

import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity;
import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity.MenuItemEnum;

/**
 * Created by NielPC on 8/20/2016.
 * <pre>
 * Received by JiffyJobMainActivity, to update top navigation when selection changed.
 * Posted in DrawerItemAdapter in menu.
 * </pre>
 */
public class TopNavigationChangedEvent {
    /**
     * Received by JiffyJobMainActivity, to update top navigation when selection changed.
     *
     * @param menuItemEnum
     */
    public TopNavigationChangedEvent(JiffyJobMainActivity.MenuItemEnum menuItemEnum) {
        this.menuItemEnum = menuItemEnum;
    }

    public MenuItemEnum getMenuItem() {
        return menuItemEnum;
    }

    public void setMenuItemEnum(MenuItemEnum menuItemEnum) {
        this.menuItemEnum = menuItemEnum;
    }

    private MenuItemEnum menuItemEnum;
}
