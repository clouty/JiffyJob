package com.jiffyjob.nimblylabs.facebook;

import android.content.Intent;

import com.facebook.AccessToken;

import java.util.Observer;

/**
 * Created by NimblyLabs on 7/2/2015.
 */
public interface IFacebookController {
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onPause();
    void onResume();
    void onDestory();

    //Facebook Properties
    AccessToken getAccessToken();
    void setAccessToken(AccessToken graphUser);

    /**
     * provide method to add observers
     */
    void addObserver(Observer loginFragmentView);
}

