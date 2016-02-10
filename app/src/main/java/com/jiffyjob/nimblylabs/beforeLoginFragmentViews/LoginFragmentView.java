package com.jiffyjob.nimblylabs.beforeLoginFragmentViews;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by NimblyLabs on 27/9/2015.
 */
public class LoginFragmentView extends Fragment {
    public static LoginFragmentView getInstance() {
        if (loginFragmentView == null) {
            loginFragmentView = new LoginFragmentView();
            return loginFragmentView;
        }
        return loginFragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (loginFragmentView == null) {
            throw new ExceptionInInitializerError("You can only create LoginFragmentView fragment by getInstance.");
        }
        context = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, null, false);
        init();
        setListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        YoYo.with(Techniques.SlideInLeft)
                .duration(Utilities.getAnimationNormal())
                .playOn(view);
    }

    private void init() {
        emailET = (EditText) view.findViewById(R.id.emailET);
        passwordET = (EditText) view.findViewById(R.id.passwordET);
        errorMsgTV = (TextView) view.findViewById(R.id.errorMsg);
        loginBtn = (Button) view.findViewById(R.id.loginBtn);
        forgetPwBtn = (Button) view.findViewById(R.id.forgetPwBtn);
    }

    private void setListeners() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainActivity = new Intent(context, JiffyJobMainActivity.class);
                context.startActivity(MainActivity);
            }
        });

        forgetPwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        emailET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isEmailValid = Utilities.validate(emailET.getText().toString());
                updateErrorMsg();
            }
        });
    }

    private void submitLogin() {
        //TODO:submit login
    }

    private void updateErrorMsg() {
        String errorMsgStr = "";
        if (isEmailValid || emailET.getText().toString().isEmpty()) {
            errorMsgTV.setVisibility(View.GONE);
        } else {
            errorMsgTV.setVisibility(View.VISIBLE);
            errorMsgStr += "Email invalid";
        }
        errorMsgTV.setText(errorMsgStr);
    }

    private static LoginFragmentView loginFragmentView = null;
    private Context context;
    private View view;
    private EditText emailET;
    private EditText passwordET;
    private Button loginBtn;
    private Button forgetPwBtn;
    private boolean isEmailValid = true;
    private TextView errorMsgTV;

}
