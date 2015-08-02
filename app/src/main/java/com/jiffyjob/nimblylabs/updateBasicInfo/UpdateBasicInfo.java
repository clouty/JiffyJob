package com.jiffyjob.nimblylabs.updateBasicInfo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * Created by NimblyLabs on 1/6/2015.
 */
public class UpdateBasicInfo extends Fragment {
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        this.basicInforEventModel = new BasicInforEventModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_updatebasicinfo, container, false);
        context = view.getContext();
        init();
        initListeners();
        getDataFromEventBus();
        return view;
    }

    private void init() {
        nextBtn = (ImageButton) view.findViewById(R.id.nextBtn);
        userImageView = (ImageView) view.findViewById(R.id.userImageView);
        firstName = (EditText) view.findViewById(R.id.firstName);
        lastName = (EditText) view.findViewById(R.id.lastName);
        dob = (TextView) view.findViewById(R.id.dob);
        errorMsgTV = (TextView) view.findViewById(R.id.errorMsg);
        radioGender = (RadioGroup) view.findViewById(R.id.radioGender);
        fragmentTransaction = getFragmentManager().beginTransaction();
    }

    private void initListeners() {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getDataFromUI();
                    boolean hasError = checkError();
                    if (!hasError) {
                        EventBus.getDefault().postSticky(basicInforEventModel);
                        fragmentTransaction.replace(R.id.fragment_container, new UpdateBasicInfo_Step2());
                        fragmentTransaction.addToBackStack(this.getClass().getSimpleName());
                        fragmentTransaction.commit();
                    }
                } catch (ParseException e) {
                    checkError();
                    e.printStackTrace();
                }
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

    private void getDataFromUI() throws ParseException {
        String firstNameStr = firstName.getText().toString();
        String lastNameStr = lastName.getText().toString();
        int selectedId = radioGender.getCheckedRadioButtonId();
        selectedRadioBtn = (RadioButton) view.findViewById(selectedId);
        String genderStr = selectedRadioBtn.getText().toString();
        String dobStr = dob.getText().toString();

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date dobDate = format.parse(dobStr);
        basicInforEventModel.setFirstName(firstNameStr);
        basicInforEventModel.setLastName(lastNameStr);
        basicInforEventModel.setGender(genderStr);
        basicInforEventModel.setDateOfBirth(dobDate);
    }

    private void getDataFromEventBus() {
        BasicInforEventModel eventModel = EventBus.getDefault().getStickyEvent(BasicInforEventModel.class);
        if (eventModel != null) {
            basicInforEventModel = eventModel;
            updateDob();
        }
    }

    private void updateDob() {
        Date date = basicInforEventModel.getDateOfBirth();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String dobDate = format.format(date);
        dob.setText(dobDate);
    }

    private boolean checkError() {
        errorMsgTV.setVisibility(View.GONE);
        if (firstName.getText().toString().isEmpty()
                || lastName.getText().toString().isEmpty()
                || dob.getText().toString().isEmpty()) {
            errorMsgTV.setVisibility(View.VISIBLE);
            errorMsgTV.setText("Oops, there some incomplete fields.");
            return true;
        }
        return false;
    }

    private Activity activity;
    private View view;
    private Context context;
    private FragmentTransaction fragmentTransaction;
    private BasicInforEventModel basicInforEventModel;

    //UI controls
    private ImageButton nextBtn;
    private TextView dob;
    private ImageView userImageView;
    private EditText firstName, lastName;
    private RadioGroup radioGender;
    private RadioButton selectedRadioBtn;
    private TextView errorMsgTV;
}
