package com.jiffyjob.nimblylabs.facebook;

import android.content.Intent;

import com.facebook.AccessToken;

import java.util.Observer;

/**
 * Created by NimblyLabs on 13/9/2015.
 */
public class FacebookController implements IFacebookController {
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestory() {

    }

    @Override
    public AccessToken getAccessToken() {
        return null;
    }

    @Override
    public void setAccessToken(AccessToken graphUser) {

    }

    @Override
    public void addObserver(Observer loginFragmentView) {

    }
}
