package com.jiffyjob.nimblylabs.updateBasicInfo;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;

/**
 * Created by NimblyLabs on 1/6/2015.
 */
public class UpdateBasicInfo extends Fragment {
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_updatebasicinfo, container, false);
        context = view.getContext();
        init();
        initListeners();
        return view;
    }

    private void init() {
        nextBtn = (ImageButton) view.findViewById(R.id.nextBtn);
        userImageView = (ImageView) view.findViewById(R.id.userImageView);
        firstName = (EditText) view.findViewById(R.id.firstName);
        lastName = (EditText) view.findViewById(R.id.lastName);
        dob = (TextView) view.findViewById(R.id.dob);
        radioGender = (RadioGroup) view.findViewById(R.id.radioGender);
    }

    private void initListeners() {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:retrieve all user input
                //getFragmentManager().beginTransaction().remove(R.id.fragment_container, );
            }
        });
        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.DatePickerFragment datePickerFragment = new Utilities.DatePickerFragment();
                datePickerFragment.setOnView(dob);
                datePickerFragment.show(activity.getFragmentManager(), "Date of birth");
            }
        });
    }

    private Activity activity;
    private View view;
    private Context context;

    //UI controls
    private ImageButton nextBtn;
    private TextView dob;
    private ImageView userImageView;
    private EditText firstName, lastName;
    private RadioGroup radioGender;
}
