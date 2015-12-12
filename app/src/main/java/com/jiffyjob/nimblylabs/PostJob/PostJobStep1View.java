package com.jiffyjob.nimblylabs.postJob;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.postJob.postJobEvents.PostJobCategoryEvent;
import com.jiffyjob.nimblylabs.postJob.postJobEvents.PostJobStep2Event;
import com.jiffyjob.nimblylabs.xmlHelper.XmlJobCategoryHelper;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by NimblyLabs on 8/4/2015.
 */
public class PostJobStep1View extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_job_step1, container, false);
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

    private void init() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        XmlJobCategoryHelper xmlHelper = new XmlJobCategoryHelper(context);
        jobList = xmlHelper.parseXML();

        selectionTitle = (TextView) view.findViewById(R.id.selectionTitle);
        jobTitle = (AutoCompleteTextView) view.findViewById(R.id.jobTitle);
        addImageBtn = (ImageButton) view.findViewById(R.id.addImageBtn);
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        selectJobCatBtn = (Button) view.findViewById(R.id.selectJobCatBtn);
        selectedJobCategoryListView = (ListView) view.findViewById(R.id.selectedJobCategoryListView);
    }

    private void initEvents() {
        selectJobCatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: open job cat selection fragment
                PostJobStep1CategoryView catView = PostJobStep1CategoryView.getInstance();
                catView.setSelectedJobList(selectedJobList);
                catView.show(getFragmentManager(), catView.getClass().getSimpleName());
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: submit to next fragment
                updatePostJobModel();
                postStickyEvent();
                proceedToNextFragment();
            }
        });

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: submit select image
            }
        });
    }

    private void updateUI() {
        if (selectedJobList != null && selectedJobList.size() > 0) {
            onEvent(new PostJobCategoryEvent(selectedJobList));
        }
    }

    private void updatePostJobModel() {
        postJobModel.setJobTitle(jobTitle.getText().toString());
        if (addImageBtn.getDrawable() != null) {
            postJobModel.setUserPhotos(((BitmapDrawable) addImageBtn.getDrawable()).getBitmap());
        }
        postJobModel.setJobCategories(selectedJobList);
    }

    private void postStickyEvent() {
        PostJobStep2Event postJobStep2Event = new PostJobStep2Event(postJobModel);
        EventBus.getDefault().postSticky(postJobStep2Event);
    }

    private void proceedToNextFragment() {
        PostJobStep2View postJobStep2View = new PostJobStep2View();
        FragmentManager manager = this.getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.postJobStepView, postJobStep2View, PostJobStep2View.class.getSimpleName());
        transaction.addToBackStack(PostJobStep1View.class.getSimpleName());
        transaction.commit();
    }

    public void onEvent(PostJobCategoryEvent model) {
        selectedJobList = model.getPostJobCategory();
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, selectedJobList);
        selectedJobCategoryListView.setAdapter(arrayAdapter);
        if (selectedJobList.size() > 0) {
            String titleStr = String.format("Job Category (%s)", selectedJobList.size());
            selectJobCatBtn.setText("Edit Job Catergory");
            selectionTitle.setText(titleStr);
            selectedJobCategoryListView.setVisibility(View.VISIBLE);
        } else {
            selectionTitle.setText("Job Category");
            selectJobCatBtn.setText("Select Job Catergory");
            selectedJobCategoryListView.setVisibility(View.GONE);
        }
    }

    private PostJobModel postJobModel = new PostJobModel();
    private AutoCompleteTextView jobTitle;
    private List<String> jobList = new ArrayList<>();
    private List<String> selectedJobList = new ArrayList<>();
    private Button submitBtn;
    private Button selectJobCatBtn;
    private ImageButton addImageBtn;
    private ListView selectedJobCategoryListView;
    private TextView selectionTitle;
    private View view;
    private Context context;
}
