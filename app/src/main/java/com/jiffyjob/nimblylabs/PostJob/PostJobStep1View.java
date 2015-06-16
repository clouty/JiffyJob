package com.jiffyjob.nimblylabs.postJob;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.xmlHelper.XmlJobCategoryHelper;

import java.util.ArrayList;
import java.util.List;

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
        generateJobCatergoryList();
        return view;
    }

    private void init() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        threshhold = size.x / 3;

        XmlJobCategoryHelper xmlHelper = new XmlJobCategoryHelper(context);
        jobList = xmlHelper.parseXML();

        selectionTitle = (TextView) view.findViewById(R.id.selectionTitle);
        button = (Button) view.findViewById(R.id.button);
        jobCategoryListView = (ListView) view.findViewById(R.id.jobCategoryList);
        jobCategoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        selectedCategoryListView = (ListView) view.findViewById(R.id.selectedCategoryList);
        selectedCategoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        favoriteCategoryListView = (ListView) view.findViewById(R.id.favoriteCategoryList);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
    }

    private void generateJobCatergoryList() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_checked, jobList);
        jobCategoryListView.setAdapter(arrayAdapter);
        jobCategoryListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent touchevent) {
                onTouchFlipperEvent(v, touchevent);
                return false;
            }
        });
    }

    private void generateSelectedCategoryList() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_checked, selectedJobList);
        selectedCategoryListView.setAdapter(arrayAdapter);
        for (int i = 0; i < selectedCategoryListView.getCount(); i++) {
            selectedCategoryListView.setItemChecked(i, true);
        }
        selectedCategoryListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent touchevent) {
                onTouchFlipperEvent(v, touchevent);
                return false;
            }
        });
        arrayAdapter.notifyDataSetChanged();
    }

    private void onTouchFlipperEvent(View view, MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = touchevent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = touchevent.getX();
                float movedDis = currentX - lastX;

                // Handling left to right screen swap.
                if (Math.abs(movedDis) > threshhold && movedDis > 0) {
                    updateTitleName(viewFlipper.getDisplayedChild() - 1);
                    // If it's the first item, just break.
                    if (viewFlipper.getDisplayedChild() == 0) {
                        break;
                    }

                    // Next screen comes in from left.
                    viewFlipper.setInAnimation(context, R.anim.slide_in_from_left);
                    // Current screen goes out from right.
                    viewFlipper.setOutAnimation(context, R.anim.slide_out_to_right);

                    // Display next screen.
                    viewFlipper.showPrevious();
                }

                // Handling right to left screen swap.
                if (Math.abs(movedDis) > threshhold && movedDis < 0) {
                    updateTitleName(viewFlipper.getDisplayedChild() + 1);
                    // Last item to be displayed, break
                    if (viewFlipper.getDisplayedChild() == 1) {
                        break;
                    }

                    // Next screen comes in from right.
                    viewFlipper.setInAnimation(context, R.anim.slide_in_from_right);
                    // Current screen goes out from left.
                    viewFlipper.setOutAnimation(context, R.anim.slide_out_to_left);

                    // Display previous screen.
                    viewFlipper.showNext();
                }
                break;
        }
    }

    private void updateTitleName(int index) {
        if (index == 0) {
            selectionTitle.setText("Job Categories");
        }
        if (index == 1) {
            selectionTitle.setText("Selected job categories");
            updateSelectedJobList();
            generateSelectedCategoryList();
        }
    }

    private void updateSelectedJobList() {
        selectedJobList.clear();
        SparseBooleanArray sparseBooleanArray = jobCategoryListView.getCheckedItemPositions();
        for (int i = 0; i < jobCategoryListView.getCount(); i++) {
            if (sparseBooleanArray.get(i)) {
                selectedJobList.add(jobList.get(i));
            }
        }
    }

    private TextView selectionTitle;
    private float threshhold = 50;
    private float lastX;
    private List<String> jobList = new ArrayList<>();
    private List<String> selectedJobList = new ArrayList<>();
    private ViewFlipper viewFlipper;
    private Button button;
    private ListView jobCategoryListView;
    private ListView selectedCategoryListView;
    private ListView favoriteCategoryListView;
    private View view;
    private Context context;
}
