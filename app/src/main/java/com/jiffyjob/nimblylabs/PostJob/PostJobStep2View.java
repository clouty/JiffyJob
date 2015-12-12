package com.jiffyjob.nimblylabs.postJob;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.postJob.postJobEvents.PostJobReqEduEvent;
import com.jiffyjob.nimblylabs.postJob.postJobEvents.PostJobStep2Event;
import com.jiffyjob.nimblylabs.postJob.postJobEvents.PostJobStep3Event;
import com.jiffyjob.nimblylabs.postJob.postJobEvents.ScopeItemEvent;
import com.jiffyjob.nimblylabs.xmlHelper.SimpleXMLReader;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by NimblyLabs on 22/7/2015.
 */
public class PostJobStep2View extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_job_step2, container, false);
        context = view.getContext();
        generateEduList();
        init();
        initEvents();
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        YoYo.with(Techniques.SlideInLeft)
                .duration(Utilities.getAnimationNormal())
                .playOn(view.findViewById(R.id.mainLayout));
        updateUI();
    }

    @Override
    public void onStop() {
        updatePostJobModel();
        super.onStop();
    }

    private void init() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().registerSticky(this);
        }
        selectReqEduBtn = (Button) view.findViewById(R.id.selectReqEduBtn);
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        scopeET = (EditText) view.findViewById(R.id.scopeET);
        addScopeBtn = (ImageButton) view.findViewById(R.id.addScopeBtn);
        scopeListView = (ListView) view.findViewById(R.id.scopeListView);
        selectedEduListView = (ListView) view.findViewById(R.id.selectedEduListView);
        fromAge = (EditText) view.findViewById(R.id.fromAge);
        toAge = (EditText) view.findViewById(R.id.toAge);
        hiringPax = (EditText) view.findViewById(R.id.totalPax);

        scopeArrayAdapter = new PostJobStep2ScopeAdapter(context, R.layout.fragment_post_job_step2_item, scopeList);
        scopeListView.setAdapter(scopeArrayAdapter);

        reqEduArrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, minReqEduList);
        selectedEduListView.setAdapter(reqEduArrayAdapter);
    }

    private void initEvents() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePostJobModel();
                postStickyEvent();
                proceedToNextFragment();
            }
        });

        addScopeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addScope();
            }
        });

        selectReqEduBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostJobStep2ReqEduView reqEduView = PostJobStep2ReqEduView.getInstance();
                reqEduView.show(getFragmentManager(), reqEduView.getClass().getSimpleName());
            }
        });
    }

    private void addScope() {
        scopeList.add(scopeET.getText().toString());
        scopeArrayAdapter.notifyDataSetChanged();
    }

    private void updatePostJobModel() {
        postJobModel.setJobScopes(scopeList);
        String fromAgeStr = fromAge.getText().toString();
        String toAgeStr = toAge.getText().toString();
        String hiringPaxStr = hiringPax.getText().toString();
        int minAge = (fromAgeStr.isEmpty()) ? 16 : Integer.parseInt(fromAgeStr);
        int maxAge = (toAgeStr.isEmpty()) ? 65 : Integer.parseInt(toAgeStr);
        int pax = (hiringPaxStr.isEmpty()) ? 1 : Integer.parseInt(hiringPaxStr);

        postJobModel.setMinAge(minAge);
        postJobModel.setMaxAge(maxAge);
        postJobModel.setTotalPax(pax);
    }

    //Use when user press back and update UI to current postJobModel variables
    private void updateUI() {
        if (postJobModel.getJobScopes() != null && postJobModel.getRequiredEducation() != -1) {
            scopeList = postJobModel.getJobScopes();
            minReqEduList.clear();
            minReqEduList.add(educationList.get(postJobModel.getRequiredEducation()));
            fromAge.setText(postJobModel.getMinAge() + "");
            toAge.setText(postJobModel.getMaxAge() + "");
            hiringPax.setText(postJobModel.getTotalPax() + "");

            scopeArrayAdapter = new PostJobStep2ScopeAdapter(context, R.layout.fragment_post_job_step2_item, scopeList);
            scopeListView.setAdapter(scopeArrayAdapter);

            reqEduArrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, minReqEduList);
            selectedEduListView.setAdapter(reqEduArrayAdapter);
            if (minReqEduList.size() > 0) {
                selectedEduListView.setVisibility(View.VISIBLE);
                selectReqEduBtn.setText("Edit required education");
                reqEduArrayAdapter.notifyDataSetChanged();
            } else {
                selectedEduListView.setVisibility(View.GONE);
                selectReqEduBtn.setText("Add required education");
            }

        }
    }

    private void postStickyEvent() {
        PostJobStep3Event postJobStep3Event = new PostJobStep3Event(postJobModel);
        EventBus.getDefault().postSticky(postJobStep3Event);
    }

    private void proceedToNextFragment() {
        PostJobStep3View postJobStep3View = new PostJobStep3View();
        FragmentManager manager = this.getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.postJobStepView, postJobStep3View, PostJobStep3View.class.getSimpleName());
        transaction.addToBackStack(PostJobStep2View.class.getSimpleName());
        transaction.commit();
    }

    private void verificationChecks() {

    }

    //All event handlers are written here
    public void onEvent(PostJobStep2Event eventModel) {
        postJobModel = eventModel.getPostJobModel();
    }

    public void onEvent(ScopeItemEvent eventModel) {
        scopeList.remove(eventModel.getPosition());
        scopeArrayAdapter.notifyDataSetChanged();
    }

    public void onEvent(PostJobReqEduEvent eventModel) {
        if (eventModel.getReqEducation() != null && !eventModel.getReqEducation().isEmpty()) {
            minReqEduList.clear();
            minReqEduList.add(eventModel.getReqEducation());
            int eduIndex = educationList.indexOf(minReqEduList.get(0));
            postJobModel.setRequiredEducation(eduIndex);
            reqEduArrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, minReqEduList);
            selectedEduListView.setAdapter(reqEduArrayAdapter);
            selectedEduListView.setVisibility(View.VISIBLE);
            selectReqEduBtn.setText("Edit required education");
        } else if (eventModel.getReqEducation() != null && eventModel.getReqEducation().isEmpty()) {
            minReqEduList.clear();
            postJobModel.setRequiredEducation(-1);
            selectedEduListView.setVisibility(View.GONE);
            selectReqEduBtn.setText("Add required education");
        }
        reqEduArrayAdapter.notifyDataSetChanged();
    }

    private void generateEduList() {
        SimpleXMLReader simpleXMLReader = new SimpleXMLReader(context, R.raw.qualitfication_global);
        educationList = simpleXMLReader.parseXML();
    }

    private PostJobStep2ScopeAdapter scopeArrayAdapter;
    private ArrayAdapter<String> reqEduArrayAdapter;
    private List<String> scopeList = new ArrayList<>();
    private List<String> minReqEduList = new ArrayList<>();
    private List<String> educationList = new ArrayList<>();

    private PostJobModel postJobModel;
    private View view = null;
    private Context context;
    private EditText scopeET;
    private Button submitBtn, selectReqEduBtn;
    private ImageButton addScopeBtn;
    private ListView scopeListView, selectedEduListView;
    private EditText fromAge = null, toAge = null, hiringPax = null;
}
