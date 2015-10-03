package com.jiffyjob.nimblylabs.beforeLoginFragmentViews;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;

/**
 * Created by NimblyLabs on 27/9/2015.
 */
public class JoinUsFragmentView extends Fragment {

    public static JoinUsFragmentView getInstance() {
        if (joinUsFragmentView == null) {
            joinUsFragmentView = new JoinUsFragmentView();
            return joinUsFragmentView;
        }
        return joinUsFragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (joinUsFragmentView == null) {
            throw new ExceptionInInitializerError("You can only create JoinUsFragmentView fragment by getInstance.");
        }
        context = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_join_us, null ,false);
        init();
        initListeners();
        return view;
    }

    private void init() {
        errorMsgTV = (TextView) view.findViewById(R.id.errorMsgTV);
        emailET = (EditText) view.findViewById(R.id.emailET);
        passwordET = (EditText) view.findViewById(R.id.passwordET);
        joinUsBtn = (Button) view.findViewById(R.id.joinUsBtn);
    }

    private void initListeners() {
        joinUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitJoinUs();
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

    private void submitJoinUs() {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        //TODO: submit join us data to server
    }

    private static JoinUsFragmentView joinUsFragmentView = null;
    private Context context = null;
    private View view;
    private TextView errorMsgTV;
    private EditText emailET;
    private EditText passwordET;
    private Button joinUsBtn;
    private boolean isEmailValid = true;
}
