package com.jiffyjob.nimblylabs.updateBasicInfo;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.xmlHelper.SimpleXMLReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NimblyLabs on 13/6/2015.
 */
public class UpdateBasicInfo_Step2 extends Fragment {
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
        populateQualification();
        populateInterest();
        return view;
    }

    private void init() {
        nextBtn = (ImageButton) view.findViewById(R.id.nextBtn);
        wordCountTV = (TextView) view.findViewById(R.id.wordCountTV);
        qualificationSpinner = (Spinner) view.findViewById(R.id.qualificationSpinner);
        interestListView = (ListView) view.findViewById(R.id.interestListView);
        interestListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        descriptionET = (EditText) view.findViewById(R.id.descriptionET);
    }

    private void populateQualification() {
        SimpleXMLReader simpleXMLReader = new SimpleXMLReader(context, R.raw.qualitfication);
        qualificationList = simpleXMLReader.parseXML();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, qualificationList);
        qualificationSpinner.setAdapter(dataAdapter);
    }

    private void populateInterest() {
        SimpleXMLReader simpleXMLReader = new SimpleXMLReader(context, R.raw.interest);
        interestList = simpleXMLReader.parseXML();
        ArrayAdapter dataAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_checked, interestList);
        interestListView.setAdapter(dataAdapter);
    }

    private void initListeners() {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private Activity activity;
    private View view;
    private Context context;

    //UI views
    private Spinner qualificationSpinner;
    private ListView interestListView;
    private EditText descriptionET;
    private TextView wordCountTV;
    private ImageButton nextBtn;
    //properties
    private List<String> qualificationList = new ArrayList<>();
    private List<String> interestList = new ArrayList<>();
}
