package com.jiffyjob.nimblylabs.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AppEventsLogger;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.jiffyjob.nimblylabs.facebook.FacebookController;
import com.jiffyjob.nimblylabs.facebook.IFacebookController;
import com.jiffyjob.nimblylabs.main.R;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class LoginFragmentView extends ActionBarActivity implements Observer {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LoginFragmentModel model = new LoginFragmentModel();
        LoginFragmentController loginFragmentController = new LoginFragmentController(this, model);
        model.addObserver(loginFragmentController);

        setContentView(R.layout.activity_login_fragment);
        Init();

        fbController = new FacebookController(this, savedInstanceState, permissionArrays);
        fbController.addObserver(this);
    }

    private void Init() {
        permissionArrays.add("user_likes");
        permissionArrays.add("user_status");
        permissionArrays.add("public_profile");
        permissionArrays.add("user_location");

        nameTextView = (TextView) findViewById(R.id.name);
        genderTextView = (TextView) findViewById(R.id.gender);
        locationTextView = (TextView) findViewById(R.id.location);
        userDetailView = (View) findViewById(R.id.userDetailsLayout);
        userProfileImageView = (ImageView) findViewById(R.id.userProfileImage);
        userDetailView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbController.onActivityResult(requestCode, resultCode, data);
    }

    private void setUserInformation(GraphUser user) {
        if (user != null) {
            nameTextView.setText(String.format("UserID:%s Name:%s", user.getId(), user.getName()));
            genderTextView.setText(user.getProperty("gender").toString());
            GraphPlace loc = user.getLocation();

            fbController.downloadAvatar(user.getId());

            locationTextView.setText(String.format("cat:%s location:%s",
                    loc.getCategory(),
                    loc.getProperty("name")));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fbController.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
        fbController.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fbController.onDestory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fbController.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //facebook controller update
    @Override
    public void update(Observable observable, Object data) {
        if (fbController.getGraphUser() != null) {
            setUserInformation(fbController.getGraphUser());
        }
        if(fbController.getUserProfileBitmap() != null){
            Bitmap userBitmap = fbController.getUserProfileBitmap();
            userProfileImageView.setImageBitmap(userBitmap);
        }
    }

    //Facebook UI variables
    private String TAG = "LoginFragmentView";
    private ArrayList<String> permissionArrays = new ArrayList<String>();
    private IFacebookController fbController;

    //variables and properties
    public View getUserDetailView() {
        return userDetailView;
    }

    public TextView getNameTextView() {
        return nameTextView;
    }

    public TextView getGenderTextView() {
        return genderTextView;
    }

    public TextView getLocationTextView() {
        return locationTextView;
    }

    public ImageView getUserProfileImageView() {
        return userProfileImageView;
    }

    private View userDetailView;
    private TextView nameTextView;
    private TextView genderTextView;
    private TextView locationTextView;
    private ImageView userProfileImageView;
}
