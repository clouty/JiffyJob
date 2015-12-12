package com.jiffyjob.nimblylabs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.beforeLoginFragmentViews.JoinUsFragmentView;
import com.jiffyjob.nimblylabs.beforeLoginFragmentViews.LoginFragmentView;
import com.jiffyjob.nimblylabs.beforeLoginFragmentViews.UserInfoModel;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.httpServices.FacebookRegisterService;
import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by NimblyLabs on 13/6/2015.
 */
public class BeforeLoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.before_login_activity);
        callbackManager = CallbackManager.Factory.create();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateFragment(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //user by facebook to update token
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If the access token is available already assign it.
        updateAccessToken(AccessToken.getCurrentAccessToken());
    }

    private void init() {
        setPermissionArrays();
        fbLoginBtn = (LoginButton) this.findViewById(R.id.fbAuthButton);
        fbLoginBtn.setReadPermissions(permissionArrays);
        loginBtn = (Button) this.findViewById(R.id.loginBtn);
        signupBtn = (Button) this.findViewById(R.id.signupBtn);
        initListener();
        setTermsAndAgreedment();
    }

    private void initListener() {
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                updateAccessToken(currentAccessToken);
            }
        };
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFragment(true);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFragment(false);
            }
        });
    }

    //Facebook permission
    private void setPermissionArrays() {
        //facebook permission
        permissionArrays.add("public_profile");
        permissionArrays.add("user_birthday");
        permissionArrays.add("user_location");
        permissionArrays.add("email");
        permissionArrays.add("user_education_history");
    }

    //Facebook accessToken
    private void updateAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
        if (accessToken != null) {
            FacebookRegisterService facebookRegisterService = new FacebookRegisterService(this);
            //getting user information, call only after user login
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            fbRegister(object);
                            startMainActivity();
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,first_name,last_name,email,birthday,location,picture,gender,education");
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

    private void startMainActivity() {
        Intent myIntent = new Intent(BeforeLoginActivity.this, JiffyJobMainActivity.class);
        BeforeLoginActivity.this.startActivity(myIntent);
    }

    private void updateFragment(boolean isjoinUs) {
        try {
            int childCount = ((LinearLayout) this.findViewById(R.id.fragment_container)).getChildCount();
            Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            fragmentTransaction.setCustomAnimations(R.animator.slide_in_from_left, R.animator.slide_out_to_left, 0, 0);

            if (!isjoinUs) {
                LoginFragmentView loginFragmentView = LoginFragmentView.getInstance();
                if (childCount == 0) {
                    fragmentTransaction.add(R.id.fragment_container, loginFragmentView, LoginFragmentView.class.getSimpleName()).commit();
                } else {
                    if (!(fragment instanceof LoginFragmentView)) {
                        fragmentTransaction.replace(R.id.fragment_container, loginFragmentView).commit();
                    }
                }
            } else {
                JoinUsFragmentView joinUsFragmentView = JoinUsFragmentView.getInstance();
                if (childCount == 0) {
                    fragmentTransaction.add(R.id.fragment_container, joinUsFragmentView, JoinUsFragmentView.class.getSimpleName()).commit();
                } else {
                    if (!(fragment instanceof JoinUsFragmentView)) {
                        fragmentTransaction.replace(R.id.fragment_container, joinUsFragmentView).commit();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
    }

    private void setTermsAndAgreedment() {
        String htmlStr = Utilities.readHtml(this, R.raw.terms_and_agreement);
        termsWebView = (WebView) this.findViewById(R.id.termsWebView);
        termsWebView.setWebViewClient(new WebViewClient());
        termsWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
                WebView newWebView = new WebView(view.getContext());
                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                        return true;
                    }
                });
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }
        });
        termsWebView.loadData(htmlStr, "text/html; charset=UTF-8", null);
        termsWebView.setBackgroundColor(0x00000000);
        termsWebView.getSettings().setSupportMultipleWindows(true);
    }

    private UserInfoModel userInfoModel = new UserInfoModel();

    //facebook parameters
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;
    private LoginButton fbLoginBtn;
    private ArrayList<String> permissionArrays = new ArrayList<>();
    private CallbackManager callbackManager;

    //before login parameters
    private Button signupBtn;
    private Button loginBtn;
    private WebView termsWebView;
}
