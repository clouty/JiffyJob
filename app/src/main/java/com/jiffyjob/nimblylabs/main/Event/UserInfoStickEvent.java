package com.jiffyjob.nimblylabs.main.Event;

import com.jiffyjob.nimblylabs.beforeLoginFragmentViews.UserInfoModel;

/**
 * Created by himur on 3/6/2016.
 */
public class UserInfoStickEvent {
    public UserInfoStickEvent(UserInfoModel userInfoModel) {
        this.userInfoModel = userInfoModel;
    }

    public UserInfoModel getUserInfoModel() {
        return userInfoModel;
    }

    private UserInfoModel userInfoModel;
}
