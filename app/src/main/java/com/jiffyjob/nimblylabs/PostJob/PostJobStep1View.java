package com.jiffyjob.nimblylabs.PostJob;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.jiffyjob.nimblylabs.XmlHelper.XmlJobCategoryHelper;
import com.jiffyjob.nimblylabs.app.R;

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
        generateSelectedCategoryList();
        return view;
    }

    private void init() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        threshhold = size.x / 2;

        XmlJobCategoryHelper xmlHelper = new XmlJobCategoryHelper(context);
        jobList = xmlHelper.parseXML();

        button = (Button) view.findViewById(R.id.button);
        jobCategoryList = (ListView) view.findViewById(R.id.jobCategoryList);
        selectedCategoryList = (ListView) view.findViewById(R.id.selectedCategoryList);
        favoriteCategoryList = (ListView) view.findViewById(R.id.favoriteCategoryList);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
    }

    private void generateJobCatergoryList() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_checked, jobList);
        jobCategoryList.setAdapter(arrayAdapter);
        jobCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
            }
        });
        jobCategoryList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent touchevent) {
                onTouchFlipperEvent(v, touchevent);
                return false;
            }
        });
    }

    private void generateSelectedCategoryList() {
        List<String> testList = new ArrayList<String>();
        testList.add("TESTING");
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_checked, testList);
        selectedCategoryList.setAdapter(arrayAdapter);
        selectedCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
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

                    // If there aren't any other children, just break.
                    if (viewFlipper.getDisplayedChild() == 0)
                        break;

                    // Next screen comes in from left.
                    viewFlipper.setInAnimation(context, R.anim.slide_in_from_left);
                    // Current screen goes out from right.
                    viewFlipper.setOutAnimation(context, R.anim.slide_out_to_right);

                    // Display next screen.
                    viewFlipper.showPrevious();
                }

                // Handling right to left screen swap.
                if (Math.abs(movedDis) > threshhold && movedDis < 0) {

                    // If there is a child (to the left), kust break.
                    if (viewFlipper.getDisplayedChild() == 1)
                        break;

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

    private float threshhold = 50;
    private float lastX;
    private List<String> jobList = new ArrayList<String>();
    private ViewFlipper viewFlipper;
    private Button button;
    private ListView jobCategoryList;
    private ListView selectedCategoryList;
    private ListView favoriteCategoryList;
    private View view;
    private Context context;
}
