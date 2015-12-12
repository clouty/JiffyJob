package com.jiffyjob.nimblylabs.postJob;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.postJob.postJobEvents.PostJobCategoryEvent;
import com.jiffyjob.nimblylabs.xmlHelper.XmlJobCategoryHelper;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by NimblyLabs on 19/7/2015.
 */
public class PostJobStep1CategoryView extends DialogFragment {
    public PostJobStep1CategoryView() {
        this.setStyle(STYLE_NO_TITLE, 0);
    }

    public static PostJobStep1CategoryView getInstance() {
        if (postJobStep1CategoryView == null) {
            postJobStep1CategoryView = new PostJobStep1CategoryView();
            return postJobStep1CategoryView;
        }
        return postJobStep1CategoryView;
    }

    public void setSelectedJobList(List<String> selectedJobList) {
        this.selectedJobList = selectedJobList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_job_step1_jobcategory, container, false);
        context = view.getContext();
        init();
        updateJobCategory();
        return view;
    }

    private void init() {
        topNaviTitle = (TextView) view.findViewById(R.id.topNaviTitle);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        acceptBtn = (ImageButton) view.findViewById(R.id.acceptBtn);
        initListeners();

        topNaviTitle.setText("Select job categories");
        XmlJobCategoryHelper xmlHelper = new XmlJobCategoryHelper(context);
        jobList = xmlHelper.parseXML();
        jobCategoryListView = (ListView) view.findViewById(R.id.jobCategoryListView);
        jobCategoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        generateJobCatergoryList();
        this.setCancelable(true);
    }

    private void initListeners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postJobStep1CategoryView.dismiss();
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptJobCategory();
            }
        });
    }

    private void generateJobCatergoryList() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_checked, jobList);
        jobCategoryListView.setAdapter(arrayAdapter);
    }

    private void acceptJobCategory() {
        selectedJobList.clear();
        //return the list of boolean, true = checked
        SparseBooleanArray jobCatList = jobCategoryListView.getCheckedItemPositions();
        for (int i = 0; i < jobList.size(); i++) {
            if (jobCatList.get(i)) {
                selectedJobList.add(jobList.get(i));
            }
        }
        PostJobCategoryEvent postJobCategory = new PostJobCategoryEvent(selectedJobList);
        EventBus.getDefault().post(postJobCategory);
        postJobStep1CategoryView.dismiss();
    }

    private void updateJobCategory() {
        jobCategoryListView.clearChoices();
        for (String item : selectedJobList) {
            for (int i = 0; i < jobCategoryListView.getCount(); i++) {
                if (item.equalsIgnoreCase((String) jobCategoryListView.getItemAtPosition(i))) {
                    //jobCategoryListView.isItemChecked(i);
                    jobCategoryListView.setItemChecked(i, true);
                }
            }
        }
    }

    private static PostJobStep1CategoryView postJobStep1CategoryView;
    private TextView topNaviTitle;
    private ImageButton backBtn, acceptBtn;
    private List<String> selectedJobList = new ArrayList<>();
    private ListView jobCategoryListView;
    private Context context;
    private View view;
    private List<String> jobList = new ArrayList<>();
}
