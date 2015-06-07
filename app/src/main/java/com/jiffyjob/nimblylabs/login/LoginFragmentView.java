package com.jiffyjob.nimblylabs.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.facebook.FacebookController;
import com.jiffyjob.nimblylabs.facebook.IFacebookController;
import com.jiffyjob.nimblylabs.httpServices.RegisterPostService;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class LoginFragmentView extends Fragment implements Observer {
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_login_fragment, container, false);
        context = view.getContext();
        final LoginFragmentModel model = new LoginFragmentModel();
        LoginFragmentController loginFragmentController = new LoginFragmentController(this, model);
        model.addObserver(loginFragmentController);

        init();
        fbController = new FacebookController(context, view, savedInstanceState, permissionArrays);
        fbController.addObserver(this);
        return view;
    }

    private void init() {
        //facebook permission
        permissionArrays.add("user_likes");
        permissionArrays.add("user_status");
        permissionArrays.add("public_profile");
        permissionArrays.add("user_location");

        //login information
        emailET = (EditText) view.findViewById(R.id.emailET);
        passwordET = (EditText) view.findViewById(R.id.passwordET);
        errorMsgTV = (TextView) view.findViewById(R.id.errorMsg);
        createBtn = (Button) view.findViewById(R.id.createBtn);
        loginBtn = (Button) view.findViewById(R.id.loginBtn);

        //facebook login
        nameTextView = (TextView) view.findViewById(R.id.name);
        genderTextView = (TextView) view.findViewById(R.id.gender);
        locationTextView = (TextView) view.findViewById(R.id.location);
        userDetailView = (View) view.findViewById(R.id.userDetailsLayout);
        userProfileImageView = (ImageView) view.findViewById(R.id.userProfileImage);
        userDetailView.setVisibility(View.VISIBLE);

        //terms and agreement
        String htmlStr = Utilities.readHtml(context, R.raw.terms_and_agreement);
        termsWebView = (WebView) view.findViewById(R.id.termsWebView);
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
        initListeners();
    }

    public void initListeners() {
        emailET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!emailET.getText().toString().isEmpty()) {
                    isEmailValid = Utilities.validate(emailET.getText().toString());
                    updateErrorMsg();
                }
            }
        });

        createBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(context, "Registering...", Toast.LENGTH_SHORT).show();
                submitRegistration();
                //TODO: check for register result, maybe repeat or fail creation
                return true;
            }
        });

        loginBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(context, "Logging in...", Toast.LENGTH_SHORT).show();
                //TODO: login and procesed to main view;
                return true;
            }
        });
    }

    private void updateErrorMsg() {
        String errorMsgStr = "";
        if (isEmailValid) {
            errorMsgTV.setVisibility(View.GONE);
        } else {
            errorMsgTV.setVisibility(View.VISIBLE);
            errorMsgStr += "Email invalid";
        }
        errorMsgTV.setText(errorMsgStr);
    }

    private void submitRegistration() {
        if (isEmailValid && !emailET.getText().toString().isEmpty()) {
            registerArray[0] = emailET.getText().toString();
            registerArray[1] = passwordET.getText().toString();
            RegisterPostService registerPostService = new RegisterPostService(context);
            registerPostService.execute(registerArray);
        }
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
        AppEventsLogger.deactivateApp(context);
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
        if (fbController.getUserProfileBitmap() != null) {
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

    private Activity activity;
    private Context context;
    private View view;

    //login info
    private String[] registerArray = new String[2];
    private boolean isEmailValid = false;
    private EditText emailET;
    private EditText passwordET;
    private TextView errorMsgTV;
    private Button createBtn, loginBtn;

    private WebView termsWebView;
    private View userDetailView;
    private TextView nameTextView;
    private TextView genderTextView;
    private TextView locationTextView;
    private ImageView userProfileImageView;
}
