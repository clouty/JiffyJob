package com.jiffyjob.nimblylabs.postJob;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.postJob.PostJobEvents.PostJobStep2Event;
import com.jiffyjob.nimblylabs.postJob.PostJobEvents.PostJobStep3Event;
import com.jiffyjob.nimblylabs.postJob.PostJobEvents.ScopeItemEvent;
import com.wefika.flowlayout.FlowLayout;

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
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        scopeET = (EditText) view.findViewById(R.id.scopeET);
        hashTagET = (EditText) view.findViewById(R.id.hashTagET);
        addScopeBtn = (ImageButton) view.findViewById(R.id.addScopeBtn);
        addHashTagBtn = (ImageButton) view.findViewById(R.id.addHashTagBtn);
        scopeListView = (ListView) view.findViewById(R.id.scopeListView);
        fromAge = (EditText) view.findViewById(R.id.fromAge);
        toAge = (EditText) view.findViewById(R.id.toAge);
        hiringPax = (EditText) view.findViewById(R.id.hiringPax);
        hashTagFL = (FlowLayout) view.findViewById(R.id.hashTagFL);

        scopeArrayAdapter = new PostJobStep2ScopeAdapter(context, R.layout.fragment_post_job_step2_item, scopeList);
        scopeListView.setAdapter(scopeArrayAdapter);
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

        addHashTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hashStr = hashTagET.getText().toString();
                addHashTag(hashStr);
            }
        });
    }

    private void addScope() {
        scopeList.add(scopeET.getText().toString());
        scopeArrayAdapter.notifyDataSetChanged();
    }

    private void addHashTag(final String hashStr) {
        hashTagList.add(hashStr);
        createHashTagView(hashStr);
    }

    private void createHashTagView(final String hashStr) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.fragment_post_job_step2_hashtag, null);
        TextView itemTV = (TextView) convertView.findViewById(R.id.hashTagTV);
        ImageView crossImageView = (ImageView) convertView.findViewById(R.id.crossBtn);

        convertView.getTag(hashTagList.indexOf(hashStr));
        itemTV.setText("#" + hashStr);
        crossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHashtag(hashTagList.indexOf(hashStr));
            }
        });

        convertView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(convertView.getMeasuredWidth(), convertView.getMeasuredHeight());
        params.setMargins(5, 5, 10, 5);
        convertView.setLayoutParams(params);
        hashTagFL.addView(convertView);
    }

    private void updateHashtag(int position) {
        hashTagList.remove(position);
        hashTagFL.removeViewAt(position);
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
        postJobModel.setHiringPax(pax);
        postJobModel.setHashtags(hashTagList);
    }

    //Use when user press back and update UI to current postJobModel variables
    private void updateUI() {
        if (postJobModel.getJobScopes() != null && postJobModel.getHashtags() != null) {
            scopeList = postJobModel.getJobScopes();
            hashTagList = postJobModel.getHashtags();
            fromAge.setText(postJobModel.getMinAge() + "");
            toAge.setText(postJobModel.getMaxAge() + "");
            hiringPax.setText(postJobModel.getHiringPax() + "");

            hashTagFL.getChildCount();
            for (String item : hashTagList) {
                createHashTagView(item);
            }
            scopeArrayAdapter = new PostJobStep2ScopeAdapter(context, R.layout.fragment_post_job_step2_item, scopeList);
            scopeListView.setAdapter(scopeArrayAdapter);
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

    private PostJobStep2ScopeAdapter scopeArrayAdapter;
    private List<String> scopeList = new ArrayList<>();
    private List<String> hashTagList = new ArrayList<>();

    private PostJobModel postJobModel;
    private View view = null;
    private Context context;
    private EditText scopeET, hashTagET;
    private Button submitBtn;
    private ImageButton addScopeBtn, addHashTagBtn;
    private ListView scopeListView;
    private EditText fromAge = null, toAge = null, hiringPax = null;
    private FlowLayout hashTagFL;
}
