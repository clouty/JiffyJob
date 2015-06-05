package com.jiffyjob.nimblylabs.registration;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.httpServices.RegisterPostService;

/**
 * Created by NimblyLabs on 1/6/2015.
 */
public class RegisterView extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        context = view.getContext();
        init();
        return view;
    }

    private void init() {
        submitBtn = (ImageButton) view.findViewById(R.id.submitBtn);
        errorMsg = (TextView) view.findViewById(R.id.errorMsg);
        emailEditText = (TextView) view.findViewById(R.id.emailEditText);
        pwEditText = (TextView) view.findViewById(R.id.pwEditText);
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!emailEditText.getText().toString().isEmpty()) {
                    isEmailValid = Utilities.validate(emailEditText.getText().toString());
                    updateErrorMsg();
                }
            }
        });
        pwEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        submitBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                submitRegistration();
                return false;
            }
        });

        String htmlStr = Utilities.readHtml(context, R.raw.terms_and_agreement);
        termsWebView = (WebView) view.findViewById(R.id.termsWebView);
        termsWebView.setWebViewClient(new WebViewClient());
        termsWebView.setWebChromeClient(new WebChromeClient());
        termsWebView.loadData(htmlStr, "text/html; charset=UTF-8", null);
        termsWebView.setBackgroundColor(0x00000000);
    }

    private void updateErrorMsg() {
        String errorMsgStr = "";
        if (isEmailValid) {
            errorMsg.setVisibility(View.GONE);
        } else {
            errorMsg.setVisibility(View.VISIBLE);
            errorMsgStr += "Email invalid";
        }
        errorMsg.setText(errorMsgStr);
    }

    private void submitRegistration() {
        if (isEmailValid && !pwEditText.getText().toString().isEmpty()) {
            registerArray[0] = emailEditText.getText().toString();
            registerArray[1] = pwEditText.getText().toString();
            RegisterPostService registerPostService = new RegisterPostService();
            registerPostService.execute(registerArray);
        }
    }

    private String[] registerArray = new String[2];
    private boolean isEmailValid = false;
    private ImageView submitBtn;
    private TextView errorMsg;
    private TextView emailEditText;
    private TextView pwEditText;
    private WebView termsWebView;
    private View view;
    private Context context;
}
