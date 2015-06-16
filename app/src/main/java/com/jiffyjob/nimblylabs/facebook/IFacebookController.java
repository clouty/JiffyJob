package com.jiffyjob.nimblylabs.facebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.facebook.model.GraphUser;

import java.util.Observer;

/**
 * Created by NimblyLabs on 7/2/2015.
 */
public interface IFacebookController {
    public void showHashKey(Context context);
    public void downloadAvatar(final String USER_ID);
    public void onSaveInstanceState(Bundle outState);
    public void onActivityResult(int requestCode, int resultCode, Intent data);
    public void onPause();
    public void onResume();
    public void onDestory();

    //Facebook Properties
    public GraphUser getGraphUser();
    public void setGraphUser(GraphUser graphUser);
    public Bitmap getUserProfileBitmap();
    public void setUserProfileBitmap(Bitmap userProfileBitmap);

    /**provide method to add observers*/
    public void addObserver(Observer loginFragmentView);
}

