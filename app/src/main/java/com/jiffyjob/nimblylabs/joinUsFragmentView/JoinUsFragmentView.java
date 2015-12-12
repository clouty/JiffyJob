package com.jiffyjob.nimblylabs.joinUsFragmentView;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.httpServices.RegisterUserService;
import com.jiffyjob.nimblylabs.loginFragmentView.LoginFragmentView;
import com.nineoldandroids.animation.Animator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class JoinUsFragmentView extends Fragment implements Observer {
    public synchronized static JoinUsFragmentView getInstance() {
        if (joinUsFragmentView == null) {
            joinUsFragmentView = new JoinUsFragmentView();
            return joinUsFragmentView;
        } else {
            return joinUsFragmentView;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // FacebookSdk.sdkInitialize(this.getActivity().getApplicationContext());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_join_us, null, false);
        context = view.getContext();
        FacebookSdk.sdkInitialize(context);
        getKeyHash_debug(context);

        callbackManager = CallbackManager.Factory.create();

        final JoinUsFragmentModel model = new JoinUsFragmentModel();
        JoinUsFragmentController loginFragmentController = new JoinUsFragmentController(this, model);
        model.addObserver(loginFragmentController);
        init();

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
        joinUsBtn = (Button) view.findViewById(R.id.joinUsBtn);
        //loginTV = (TextView) view.findViewById(R.id.loginTV);

        //facebook login
        fbLoginBtn = (LoginButton) view.findViewById(R.id.fbAuthButton);
        fbLoginBtn.setReadPermissions(permissionArrays);

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

        joinUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Registering...", Toast.LENGTH_SHORT).show();
                submitRegistration();
            }
        });

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction fragTransaction = activity.getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.fragment_container, LoginFragmentView.getInstance());
                YoYo.with(Techniques.Hinge)
                        .withListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                fragTransaction.commit();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .duration(Utilities.getAnimationSlow())
                        .playOn(view.findViewById(R.id.scrollView));
            }
        });

        fbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                String error = e.getMessage();
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
            RegisterUserService registerPostService = new RegisterUserService(context);
            /*registerPostService.execute(registerArray);*/
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void setUserInformation() {
        //TODO: setuser information
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(context);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //fbController.onSaveInstanceState(outState);
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

    //update from facebook controller (Observable)
    @Override
    public void update(Observable observable, Object data) {
       /* if (fbController.getGraphUser() != null) {
            setUserInformation(fbController.getGraphUser());
        }
        if (fbController.getUserProfileBitmap() != null) {
            Bitmap userBitmap = fbController.getUserProfileBitmap();
            userProfileImageView.setImageBitmap(userBitmap);
        }*/
    }

    private void getKeyHash_debug(Context context) {
        // Add code to print out the key hash
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.jiffyjob.nimblylabs.app", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //Facebook UI variables
    private String TAG = "JoinUsFragmentView";
    private ArrayList<String> permissionArrays = new ArrayList<String>();
    private CallbackManager callbackManager;

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

    private static JoinUsFragmentView joinUsFragmentView = null;
    private Activity activity;
    private Context context;
    private View view;
    private LoginButton fbLoginBtn;

    //login info
    private String[] registerArray = new String[2];
    private boolean isEmailValid = false;
    private EditText emailET;
    private EditText passwordET;
    private TextView errorMsgTV;
    private Button joinUsBtn;
    private TextView loginTV;

    private WebView termsWebView;
    private View userDetailView;
    private TextView nameTextView;
    private TextView genderTextView;
    private TextView locationTextView;
    private ImageView userProfileImageView;
}
