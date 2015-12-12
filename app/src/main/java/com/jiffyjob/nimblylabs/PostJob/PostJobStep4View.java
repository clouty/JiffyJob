package com.jiffyjob.nimblylabs.postJob;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.httpServices.PostJobService;
import com.jiffyjob.nimblylabs.jsonUtil.IJsonBuilder;
import com.jiffyjob.nimblylabs.postJob.jsonBuilder.PostJobJsonBuilder;
import com.jiffyjob.nimblylabs.postJob.postJobEvents.PostJobStep4Event;
import com.nineoldandroids.animation.Animator;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by NimblyLabs on 26/7/2015.
 */
public class PostJobStep4View extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_job_step4, container, false);
        context = view.getContext();
        init();
        initEvent();
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
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        generateSummary();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .duration(Utilities.getAnimationNormal())
                .playOn(view.findViewById(R.id.mainLayout));
        updateUI();
    }

    @Override
    public void onStop() {
        updatePostJobModel();
        super.onResume();
    }

    private void init() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().registerSticky(this);
        }
        summaryLayout = (LinearLayout) view.findViewById(R.id.summaryLayout);
        termsWebView = (WebView) view.findViewById(R.id.termsWebView);
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        agreedTermscb = (CheckBox) view.findViewById(R.id.agreedTermscb);
    }

    private void initEvent() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePostJobModel();
                postJobToServer();
            }
        });

        agreedTermscb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    private void generateSummary() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View jobCategoryView = layoutInflater.inflate(R.layout.fragment_post_job_step4_item, null);
        //job categories
        List<String> jobCategories = postJobModel.getJobCategories();
        TextView jobCatTitleTV = (TextView) jobCategoryView.findViewById(R.id.titleTV);
        TextView jobCatContentTV = (TextView) jobCategoryView.findViewById(R.id.contentTV);
        String content = "";
        for (String item : jobCategories) {
            content += String.format("- %s %n", item);
        }
        jobCatTitleTV.setText("Job categories");
        jobCatContentTV.setText(content);
        summaryLayout.addView(jobCategoryView);

        //job scopes
        List<String> scopeList = postJobModel.getJobScopes();
        View scopeView = layoutInflater.inflate(R.layout.fragment_post_job_step4_item, null);
        TextView scopeTitleTV = (TextView) scopeView.findViewById(R.id.titleTV);
        TextView scopeContentTV = (TextView) scopeView.findViewById(R.id.contentTV);
        content = "";
        for (String item : scopeList) {
            content += String.format("- %s %n", item);
        }
        scopeTitleTV.setText("Job scopes");
        scopeContentTV.setText(content);
        scopeView.setBackgroundColor(getResources().getColor(R.color.circular_transparent_green));
        summaryLayout.addView(scopeView);

        //min-max age
        String minAgeStr = postJobModel.getMinAge() + "";
        String maxAgeStr = postJobModel.getMaxAge() + "";
        View ageView = layoutInflater.inflate(R.layout.fragment_post_job_step4_item, null);
        TextView ageTitleTV = (TextView) ageView.findViewById(R.id.titleTV);
        TextView ageContentTV = (TextView) ageView.findViewById(R.id.contentTV);
        ageTitleTV.setText("Age range");
        ageContentTV.setText(String.format("%s-%s", minAgeStr, maxAgeStr));
        summaryLayout.addView(ageView);

        //hiring pax
        String hiringPaxStr = postJobModel.getTotalPax() + "";
        View hiringPaxView = layoutInflater.inflate(R.layout.fragment_post_job_step4_item, null);
        TextView hiringTitleTV = (TextView) hiringPaxView.findViewById(R.id.titleTV);
        TextView hiringContentTV = (TextView) hiringPaxView.findViewById(R.id.contentTV);
        hiringTitleTV.setText("Hiring pax");
        hiringContentTV.setText(hiringPaxStr);
        hiringPaxView.setBackgroundColor(getResources().getColor(R.color.circular_transparent_green));
        summaryLayout.addView(hiringPaxView);

        //address
        View addressView = layoutInflater.inflate(R.layout.fragment_post_job_step4_item, null);
        TextView addressTitleTV = (TextView) addressView.findViewById(R.id.titleTV);
        TextView addressContentTV = (TextView) addressView.findViewById(R.id.contentTV);
        String addressStr = "";
        if (postJobModel.getAddress() != null) {
            for (int i = 0; i < postJobModel.getAddress().getMaxAddressLineIndex(); i++) {
                addressStr += postJobModel.getAddress().getAddressLine(i) + "\n";
            }
            addressTitleTV.setText("Address");
            addressContentTV.setText(addressStr);
        }
        addressView.setBackgroundColor(getResources().getColor(R.color.circular_transparent_green));
        summaryLayout.addView(addressView);

        //Start time
        View startEndView = layoutInflater.inflate(R.layout.fragment_post_job_step4_item, null);
        TextView startEndTitleTV = (TextView) startEndView.findViewById(R.id.titleTV);
        TextView endContentTV = (TextView) startEndView.findViewById(R.id.contentTV);
        Date startTime = postJobModel.getStartTime().getTime();
        Date endTime = postJobModel.getEndTime().getTime();
        String startEndStr = String.format("%s\n%s", dmyDateFormat.format(startTime), dmyDateFormat.format(endTime));
        startEndTitleTV.setText("Start-End date");
        endContentTV.setText(startEndStr);
        summaryLayout.addView(startEndView);

        //Payout
        View payoutView = layoutInflater.inflate(R.layout.fragment_post_job_step4_item, null);
        TextView payoutTitleTV = (TextView) payoutView.findViewById(R.id.titleTV);
        TextView payoutContentTV = (TextView) payoutView.findViewById(R.id.contentTV);
        payoutTitleTV.setText("Payout");
        payoutContentTV.setText("$" + postJobModel.getPayout());
        payoutView.setBackgroundColor(getResources().getColor(R.color.circular_transparent_green));
        summaryLayout.addView(payoutView);

        //BoostPost
        View boostPostView = layoutInflater.inflate(R.layout.fragment_post_job_step4_item, null);
        TextView boostPostTV = (TextView) boostPostView.findViewById(R.id.titleTV);
        TextView boostPostContentTV = (TextView) boostPostView.findViewById(R.id.contentTV);
        boostPostTV.setText("Boost my post");
        if (postJobModel.isBoostPost()) {
            boostPostContentTV.setText("Yes");
        } else {
            boostPostContentTV.setText("False");
        }
        summaryLayout.addView(boostPostView);
    }

    private void updateUI() {
        agreedTermscb.setChecked(postJobModel.isAgreedToTerms());
    }

    private void updatePostJobModel() {
        postJobModel.setIsAgreedToTerms(agreedTermscb.isChecked());
    }

    private void postJobToServer() {
        IJsonBuilder postJobJsonBuilder = new PostJobJsonBuilder(context);
        postJobJSON = postJobJsonBuilder.getJSON(postJobModel);
        PostJobService postJobService = new PostJobService(context);
        postJobService.execute(postJobJSON);
    }

    //All event handlers are written here
    public void onEvent(PostJobStep4Event eventModel) {
        postJobModel = eventModel.getPostJobModel();
    }

    private final SimpleDateFormat dmyDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
    private final SimpleDateFormat mdyDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    private LinearLayout summaryLayout;
    private WebView termsWebView;
    private CheckBox agreedTermscb;
    private PostJobModel postJobModel;
    private JSONObject postJobJSON;
    private Button submitBtn;
    private View view;
    private Context context;
}
