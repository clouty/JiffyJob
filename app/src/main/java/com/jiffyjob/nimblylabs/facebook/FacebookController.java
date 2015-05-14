package com.jiffyjob.nimblylabs.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.AppEventsLogger;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.jiffyjob.nimblylabs.app.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by NimblyLabs on 7/2/2015.
 */
public class FacebookController extends Observable implements IFacebookController{
    /**Class required facebook login activity view, permission for facebook, creation bundle of activity.<br/>
     * Observer are to be added to Facebook controller.<br/>
     * By setting setUserProfileBitmap and setGraphUser will notify observers.
     * */
    public FacebookController(Activity activity, Bundle savedInstanceState, ArrayList<String> permissionArrays)
    {
        this.context = activity.getApplicationContext();
        this.permissionArrays = permissionArrays;
        this.fbLoginBtn = (LoginButton) activity.findViewById(R.id.fbAuthButton);
        uiHelper = new UiLifecycleHelper(activity, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        fbButtonInit();
    }

    //use to find hashkey
    public void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("packageName:", context.getPackageName());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.getMessage());
        }
    }

    /**Setting user avatar to a imageView, requires userID & imageView*/
    public synchronized void downloadAvatar(final String USER_ID) {
        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            public Bitmap doInBackground(Void... params) {
                URL fbAvatarUrl = null;
                Bitmap fbAvatarBitmap = null;
                try {
                    fbAvatarUrl = new URL("https://graph.facebook.com/"+USER_ID+"/picture?type=large");
                    fbAvatarBitmap = BitmapFactory.decodeStream(fbAvatarUrl.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return fbAvatarBitmap;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                setUserProfileBitmap(result);
            }

        };
        task.execute();
    }

    public void onPause(){
        AppEventsLogger.deactivateApp(this.context);
        uiHelper.onPause();
    }

    public void onResume(){
        AppEventsLogger.activateApp(this.context);
        Session session = Session.getActiveSession();
        if (session != null) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();
    }

    public void onDestory(){
        uiHelper.onDestroy();
    }

    @Override
    public void addObserver(Observer loginFragmentView) {
        super.addObserver(loginFragmentView);
    }

    public void onSaveInstanceState(Bundle outState) {
        uiHelper.onSaveInstanceState(outState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }


    private void fbButtonInit(){
        this.fbLoginBtn.setReadPermissions(permissionArrays);
        this.fbLoginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                setGraphUser(user);
            }
        });
    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            sessionState = state;
            if (state.isOpened()) {
                Log.d(TAG, "StatusCallback, Facebook session opened");
            } else if (state.isClosed()) {
                Log.d(TAG, "StatusCallback, Facebook session closed");
            }
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    //setUserInformation(user);
                    setGraphUser(user);
                }
            });
            Log.i(TAG, "onSessionStateChange, Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "onSessionStateChange, Logged out...");
        }
    }


    //Facebook UI variables and properties
    public GraphUser getGraphUser() {
        return graphUser;
    }
    public void setGraphUser(GraphUser graphUser){
        this.graphUser = graphUser;
        setChanged();
        notifyObservers();
    }
    public Bitmap getUserProfileBitmap() {
        return userProfileBitmap;
    }
    public void setUserProfileBitmap(Bitmap userProfileBitmap) {
        this.userProfileBitmap = userProfileBitmap;
        setChanged();
        notifyObservers();
    }
    /**To check is facebook session is active, default is closed*/
    public SessionState getSessionState() {
        return sessionState;
    }

    private GraphUser graphUser;
    private Bitmap userProfileBitmap;
    private String TAG = "FacebookController";
    private UiLifecycleHelper uiHelper;
    private LoginButton fbLoginBtn;
    private ArrayList<String> permissionArrays = new ArrayList<String>();
    private SessionState sessionState = SessionState.CLOSED;

    //Android activity variables
    private Context context;

}
