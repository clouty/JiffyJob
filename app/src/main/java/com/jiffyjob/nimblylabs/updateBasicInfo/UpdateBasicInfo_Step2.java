package com.jiffyjob.nimblylabs.updateBasicInfo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.httpServices.EditUserService;
import com.jiffyjob.nimblylabs.xmlHelper.SimpleXMLReader;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by NimblyLabs on 13/6/2015.
 */
public class UpdateBasicInfo_Step2 extends Fragment {
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_updatebasicinfo2, container, false);
        context = view.getContext();
        init();
        initListeners();
        populateQualification();
        populateInterest();
        if (!basicInforEventModel.getInterestList().isEmpty() || !basicInforEventModel.getDescription().isEmpty()) {
            updateUIFromEventBus();
        }
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(BasicInforEventModel model) {
        this.basicInforEventModel = model;
    }

    private void init() {
        fragmentTransaction = getFragmentManager().beginTransaction();
        errorMsgTV = (TextView) view.findViewById(R.id.errorMsgTV);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        nextBtn = (ImageButton) view.findViewById(R.id.nextBtn);
        wordCountTV = (TextView) view.findViewById(R.id.wordCountTV);
        qualificationSpinner = (Spinner) view.findViewById(R.id.qualificationSpinner);
        interestListView = (ListView) view.findViewById(R.id.interestListView);
        interestListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        descriptionET = (EditText) view.findViewById(R.id.descriptionET);
    }

    private void populateQualification() {
        SimpleXMLReader simpleXMLReader = new SimpleXMLReader(context, R.raw.qualitfication_sg);
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
        descriptionET.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        updateDescriptionCount();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (errorMsgTV.getVisibility() == View.VISIBLE) {
                    Toast.makeText(context, "Oops. You can't submit with error.", Toast.LENGTH_LONG).show();
                } else {
                    getDataFromUI();
                    submitUserInfoUpdate();
                    EventBus.getDefault().removeStickyEvent(BasicInforEventModel.class);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getDataFromUI();
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    private void getDataFromUI() {
        String highestQualification = qualificationSpinner.getSelectedItem().toString();
        SparseBooleanArray checked = interestListView.getCheckedItemPositions();
        List<String> selectedInterestList = new ArrayList<>();
        for (int i = 0; i < interestListView.getAdapter().getCount(); i++) {
            if (checked.get(i)) {
                selectedInterestList.add(interestList.get(i));
            }
        }
        String descriptionStr = descriptionET.getText().toString();
        basicInforEventModel.setHighestQualification(highestQualification);
        basicInforEventModel.setInterestList(selectedInterestList);
        basicInforEventModel.setDescription(descriptionStr);
    }

    private void updateDescriptionCount() {
        String descriptionStr = descriptionET.getText().toString();
        int wordsCount = Utilities.countWords(descriptionStr);
        wordCountTV.setText(wordsCount + "");
        if (wordsCount > maxWordCount) {
            errorMsgTV.setVisibility(View.VISIBLE);
            errorMsgTV.setText("Erm.. you have exceeded word limit.");
        } else {
            errorMsgTV.setVisibility(View.GONE);
        }
    }

    private void updateUIFromEventBus() {
        String highestQualification = basicInforEventModel.getHighestQualification();
        List<String> userInterestList = basicInforEventModel.getInterestList();
        String shortDescriptionStr = basicInforEventModel.getDescription();

        for (int i = 0; i < qualificationSpinner.getCount(); i++) {
            String item = (String) qualificationSpinner.getItemAtPosition(i);
            if (item.equalsIgnoreCase(highestQualification)) {
                qualificationSpinner.setSelection(i);
            }
        }

        for (int i = 0; i < interestListView.getCount(); i++) {
            String item = (String) interestListView.getItemAtPosition(i);
            if (userInterestList.contains(item)) {
                interestListView.setItemChecked(i, true);
            }
        }
        descriptionET.setText(shortDescriptionStr);
    }

    private void submitUserInfoUpdate() {
        EditUserService editUserService = new EditUserService(context, basicInforEventModel);
        editUserService.execute();
    }

    private Activity activity;
    private View view;
    private Context context;
    private FragmentTransaction fragmentTransaction;

    //UI views
    private TextView errorMsgTV;
    private Spinner qualificationSpinner;
    private ListView interestListView;
    private EditText descriptionET;
    private TextView wordCountTV;
    private ImageButton nextBtn;
    private ImageButton backBtn;
    //properties
    private List<String> qualificationList = new ArrayList<>();
    private List<String> interestList = new ArrayList<>();
    private BasicInforEventModel basicInforEventModel;
    private final int maxWordCount = 50;
}
