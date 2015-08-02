package com.jiffyjob.nimblylabs.loginFragmentView;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.joinUsFragmentView.JoinUsFragmentView;
import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity;
import com.nineoldandroids.animation.Animator;

/**
 * Created by NimblyLabs on 13/6/2015.
 */
public class LoginFragmentView extends Fragment {
    public synchronized static LoginFragmentView getInstance() {
        if (loginFragmentView == null) {
            loginFragmentView = new LoginFragmentView();
            return loginFragmentView;
        } else {
            return loginFragmentView;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        context = view.getContext();
        init();
        initListeners();
        return view;
    }

    private void init() {
        emailET = (EditText) view.findViewById(R.id.emailET);
        passwordET = (EditText) view.findViewById(R.id.passwordET);
        loginBtn = (Button) view.findViewById(R.id.loginBtn);
        forgetPwBtn = (Button) view.findViewById(R.id.forgetPwBtn);
        backBtn = (Button) view.findViewById(R.id.backBtn);

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
    }

    private void initListeners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction fragTransaction = activity.getFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.fragment_container, JoinUsFragmentView.getInstance());
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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainActivity = new Intent(context, JiffyJobMainActivity.class);
                context.startActivity(MainActivity);
            }
        });
    }

    private static LoginFragmentView loginFragmentView = null;
    private Context context;
    private View view;
    private Activity activity;

    //view controls
    private EditText emailET, passwordET;
    private Button loginBtn, forgetPwBtn, backBtn;
    private WebView termsWebView;
}
