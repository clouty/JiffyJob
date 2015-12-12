package com.jiffyjob.nimblylabs.postJob;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.postJob.postJobEvents.PostJobReqEduEvent;
import com.jiffyjob.nimblylabs.xmlHelper.SimpleXMLReader;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by NimblyLabs on 10/10/2015.
 */
public class PostJobStep2ReqEduView extends DialogFragment {
    public PostJobStep2ReqEduView() {
        this.setStyle(STYLE_NO_TITLE, 0);
    }

    public static PostJobStep2ReqEduView getInstance() {
        if (postJobStep2HighestEduView == null) {
            postJobStep2HighestEduView = new PostJobStep2ReqEduView();
            return postJobStep2HighestEduView;
        }
        return postJobStep2HighestEduView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_job_step2_reqedu, container, false);
        context = view.getContext();

        init();
        initListeners();
        return view;
    }

    private void initListeners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postJobStep2HighestEduView.dismiss();
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptReqEdu();
            }
        });
    }

    private void init() {
        generateEduList();
        topNaviTitle = (TextView) view.findViewById(R.id.topNaviTitle);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        acceptBtn = (ImageButton) view.findViewById(R.id.acceptBtn);

        topNaviTitle.setText("Required qualification");
        reqQualificationListView = (ListView) view.findViewById(R.id.reqQualificationListView);
        reqQualificationListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_checked, educationList);
        reqQualificationListView.setAdapter(arrayAdapter);
        this.setCancelable(true);
    }

    private void generateEduList() {
        SimpleXMLReader simpleXMLReader = new SimpleXMLReader(context, R.raw.qualitfication_global);
        educationList = simpleXMLReader.parseXML();
    }

    private void acceptReqEdu() {
        selectedReqEduList.clear();
        int pos = reqQualificationListView.getCheckedItemPosition();
        PostJobReqEduEvent postJobReqEduEvent = new PostJobReqEduEvent(educationList.get(pos));
        EventBus.getDefault().post(postJobReqEduEvent);
        postJobStep2HighestEduView.dismiss();
    }

    private Context context;
    private static PostJobStep2ReqEduView postJobStep2HighestEduView = null;
    private List<String> educationList = new ArrayList<>();
    private List<String> selectedReqEduList = new ArrayList<>();
    private View view;

    private ListView reqQualificationListView;
    private TextView topNaviTitle;
    private ImageButton backBtn;
    private ImageButton acceptBtn;
}
