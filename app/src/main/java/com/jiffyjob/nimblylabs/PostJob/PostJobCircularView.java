package com.jiffyjob.nimblylabs.postJob;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.customui.CircularStepsView;

public class PostJobCircularView extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_job_circular_view, container, false);
        context = view.getContext();
        init();
        return view;
    }

    private void init() {
        circularStepsViews[0] = (CircularStepsView) view.findViewById(R.id.circularStep1);
        circularStepsViews[1] = (CircularStepsView) view.findViewById(R.id.circularStep2);
        circularStepsViews[2] = (CircularStepsView) view.findViewById(R.id.circularStep3);
        circularStepsViews[3] = (CircularStepsView) view.findViewById(R.id.circularStep4);
        setOnBackStackChange();
    }

    private void setOnBackStackChange() {
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                PostJobCircularView postJobCircularView = (PostJobCircularView) getFragmentManager().findFragmentById(R.id.circularFragmentView);
                CircularStepsView[] circularStepsViews = postJobCircularView.getCircularStepsViews();
                for (CircularStepsView view : circularStepsViews) {
                    view.setCircleColor(R.color.circular_transparent_green);
                }

                Fragment fragment = getFragmentManager().findFragmentById(R.id.postJobStepView);
                if (fragment instanceof PostJobStep1View) {
                    circularStepsViews[0].setCircleColor(R.color.circular_green);
                    YoYo.with(Techniques.DropOut)
                            .duration(Utilities.getAnimationSlow())
                            .playOn(circularStepsViews[0]);
                } else if (fragment instanceof PostJobStep2View) {
                    circularStepsViews[0].setCircleColor(R.color.circular_green);
                    circularStepsViews[1].setCircleColor(R.color.circular_green);
                    YoYo.with(Techniques.DropOut)
                            .duration(Utilities.getAnimationSlow())
                            .playOn(circularStepsViews[1]);
                } else if (fragment instanceof PostJobStep3View) {
                    circularStepsViews[0].setCircleColor(R.color.circular_green);
                    circularStepsViews[1].setCircleColor(R.color.circular_green);
                    circularStepsViews[2].setCircleColor(R.color.circular_green);
                    YoYo.with(Techniques.DropOut)
                            .duration(Utilities.getAnimationSlow())
                            .playOn(circularStepsViews[2]);
                } else {
                    circularStepsViews[0].setCircleColor(R.color.circular_green);
                    circularStepsViews[1].setCircleColor(R.color.circular_green);
                    circularStepsViews[2].setCircleColor(R.color.circular_green);
                    circularStepsViews[3].setCircleColor(R.color.circular_green);
                    YoYo.with(Techniques.DropOut)
                            .duration(Utilities.getAnimationSlow())
                            .playOn(circularStepsViews[3]);
                }
            }
        });
    }

    public CircularStepsView[] getCircularStepsViews() {
        return circularStepsViews;
    }

    //4 steps circular view
    private CircularStepsView[] circularStepsViews = new CircularStepsView[4];
    private View view;
    private Context context;
}
