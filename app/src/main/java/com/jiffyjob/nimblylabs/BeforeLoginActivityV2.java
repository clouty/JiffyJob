package com.jiffyjob.nimblylabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.beforeLoginFragmentViews.UserInfoModel;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.httpServices.FacebookRegisterService;
import com.jiffyjob.nimblylabs.main.Event.UserInfoStickEvent;
import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity;
import com.jiffyjob.nimblylabs.questionnaireFragmentView.Camera.PlaybackSurface;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.nineoldandroids.animation.Animator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

import de.greenrobot.event.EventBus;

/**
 * Created by himur on 9/15/2016.
 */
public class BeforeLoginActivityV2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.before_login_activity_v2);
        playbackSurface = new PlaybackSurface(this);
        prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
        // If the access token is available already assign it.
        //updateAccessToken(AccessToken.getCurrentAccessToken());
    }

    @Override
    protected void onStop() {
        super.onStop();
        frameLayout.removeAllViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //use by Facebook to update token
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //use by LinkedIn to update token
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }


    private void init() {
        setPermissionArrays();
        if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired()) {
            updateAccessToken(AccessToken.getCurrentAccessToken());
        }
        if (hasLoginLinkedIn()) {
            setUpdateState();
        }

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        termsTV = (TextView) findViewById(R.id.termsTV);
        connectBtn = (Button) this.findViewById(R.id.connectBtn);
        linkedinBtn = (Button) this.findViewById(R.id.linkedinBtn);
        devLoginBtn = (Button) this.findViewById(R.id.devLoginBtn);
        loginBtnLL = (LinearLayout) this.findViewById(R.id.loginBtnLL);
        fbLoginBtn = (LoginButton) this.findViewById(R.id.fbAuthButton);
        fbLoginBtn.setReadPermissions(permissionArrays);

        frameLayout.addView(playbackSurface);
        setTermsAndAgreedment();
        initListener();
        playbackSetup();
    }

    private void initListener() {
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                updateAccessToken(currentAccessToken);
            }
        };
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideConnectBtn();
            }
        });
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginBtnLL.getVisibility() == View.VISIBLE) {
                    hideLoginBtnLL();
                }
            }
        });
        linkedinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLinkedInToken();
            }
        });
        devLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        if (!mainActivityStarted) {
            mainActivityStarted = true;
            EventBus.getDefault().postSticky(new UserInfoStickEvent(model));
            Intent myIntent = new Intent(BeforeLoginActivityV2.this, JiffyJobMainActivity.class);
            BeforeLoginActivityV2.this.startActivity(myIntent);
            finish();
        }
    }

    //Background playback setup
    private void playbackSetup() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.icybokeh2);
        playbackSurface.setVideoUrl(uri.toString());
        playbackSurface.setClickable(true);
        playbackSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginBtnLL.getVisibility() == View.VISIBLE) {
                    hideLoginBtnLL();
                }
            }
        });
    }


    //LinkedIn stuff
    private boolean hasLoginLinkedIn() {
        LISessionManager sessionManager = LISessionManager.getInstance(getApplicationContext());
        LISession session = sessionManager.getSession();
        return session.isValid();
    }

    private void getLinkedInToken() {
        Utilities.getKeyHash_debug(this);
        LISessionManager liSessionManager = LISessionManager.getInstance(this);
        liSessionManager.init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                setUpdateState();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                Toast.makeText(getApplicationContext(), "Error logging into LinkedIn.", Toast.LENGTH_LONG).show();
            }
        }, false);
    }

    private void setUpdateState() {
        LISessionManager sessionManager = LISessionManager.getInstance(getApplicationContext());
        LISession session = sessionManager.getSession();
        boolean accessTokenValid = session.isValid();

        if (accessTokenValid) {
            View loadingLayout = this.findViewById(R.id.loadingLayout);
            loadingLayout.setVisibility(View.VISIBLE);

            String token = session.getAccessToken().getValue();
            long tokenExpDate = session.getAccessToken().getExpiresOn();
            String userInfo = prefs.getString(LINKEDIN_USER_INFO, null);
            if (userInfo != null) {
                try {
                    JSONObject jsonObject = new JSONObject(userInfo);
                    storeLinkedInUserInfo(jsonObject);
                    startMainActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                getLinkedInUserInfo();
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(LINKEDIN_TOKEN, token);
            editor.putLong(LINKEDIN_TOKEN_EXP_DATE, tokenExpDate);
            editor.apply();
        }
    }

    private void getLinkedInUserInfo() {
        //get all industry codes
        //https://developer.linkedin.com/docs/reference/industry-codes
        String baseLinkedInUrl = this.getResources().getString(R.string.baseLinkedInUrl);
        String basicProfileUrl = String.format("%s/v1/people/~:(id,first-name,last-name,industry,location,summary,picture-urls::(original),email-address)?format=json", baseLinkedInUrl);
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, basicProfileUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                if (apiResponse.getStatusCode() == 200) {
                    JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                    storeLinkedInUserInfo(jsonObject);
                    startMainActivity();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                String result = error.toString();
            }
        });

    }

    private void storeLinkedInUserInfo(JSONObject jsonObject) {
        if (model == null) {
            model = new UserInfoModel(jsonObject, LINKEDIN_USER_INFO);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(LINKEDIN_ID, model.getId());
            editor.apply();
        }
    }

    // Build the list of member permissions our LinkedIn session requires
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
    }


    //Facebook Stuff
    private void setPermissionArrays() {
        //facebook permission
        permissionArrays.add("public_profile");
        permissionArrays.add("user_birthday");
        permissionArrays.add("user_location");
        permissionArrays.add("email");
        permissionArrays.add("user_education_history");
    }

    private void updateAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
        if (this.accessToken != null) {
            View loadingLayout = this.findViewById(R.id.loadingLayout);
            loadingLayout.setVisibility(View.VISIBLE);
            //getting user information, call only after user login
            GraphRequest request = GraphRequest.newMeRequest(accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            fbRegister(object);
                            storeFbUserInfo(object);
                            startMainActivity();
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,first_name,last_name,email,birthday,location,picture.height(961),gender,education,cover");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    private void fbRegister(JSONObject jsonObject) {
        try {
            FacebookRegisterService facebookRegisterService = new FacebookRegisterService(this);
            String userID = jsonObject.getString("id");
            Hashtable<String, String> dictionary = new Hashtable<>();
            dictionary.put("userID", userID);
            dictionary.put("tokenString", this.accessToken.getToken());
            facebookRegisterService.execute(dictionary);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void storeFbUserInfo(JSONObject jsonObject) {
        if (model == null) {
            model = new UserInfoModel(jsonObject, FACEBOOK_USER_INFO);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(FACEBOOK_ID, model.getId());
            editor.apply();
        }
    }


    //Etc private methods
    private void setTermsAndAgreedment() {
        String terms = getResources().getString(R.string.terms);
        termsTV.setText(terms);
        termsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String termsLink = getResources().getString(R.string.termsLink);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(termsLink));
                startActivity(browserIntent);
            }
        });
    }

    //Animate buttons
    private void hideConnectBtn() {
        YoYo.with(Techniques.SlideOutDown)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        showLoginBtnLL();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .duration(Utilities.getAnimationFast())
                .playOn(connectBtn);
    }

    private void showConnectBtn() {
        loginBtnLL.setVisibility(View.GONE);
        YoYo.with(Techniques.FadeIn)
                .duration(Utilities.getAnimationFast())
                .playOn(connectBtn);
    }

    private void showLoginBtnLL() {
        loginBtnLL.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.SlideInUp)
                .duration(Utilities.getAnimationFast())
                .playOn(loginBtnLL);
    }

    private void hideLoginBtnLL() {
        YoYo.with(Techniques.SlideOutDown)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        showConnectBtn();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .duration(Utilities.getAnimationFast())
                .playOn(loginBtnLL);
    }

    private SharedPreferences prefs;
    private FrameLayout frameLayout;
    private PlaybackSurface playbackSurface;
    private Button connectBtn, linkedinBtn, devLoginBtn;
    private LinearLayout loginBtnLL;
    private UserInfoModel model = null;
    private TextView termsTV;
    private boolean mainActivityStarted = false;

    //LinkedIn stuff
    private LISessionManager liSessionManager;
    public static final String LINKEDIN_USER_INFO = "LINKEDIN_USER_INFO";
    public static final String LINKEDIN_TOKEN = "LINKEDIN_TOKEN";
    public static final String LINKEDIN_TOKEN_EXP_DATE = "LINKEDIN_TOKEN_EXP_DATE";
    public static final String LINKEDIN_ID = "LINKEDIN_ID";

    //Facebook stuff
    public static final String FACEBOOK_USER_INFO = "FACEBOOK_USER_INFO";
    public static final String FACEBOOK_ID = "FACEBOOK_ID";
    private LoginButton fbLoginBtn;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;
    private ArrayList<String> permissionArrays = new ArrayList<>();
}

