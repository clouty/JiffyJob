package com.jiffyjob.nimblylabs.PostJob;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.customui.CircularStepsView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostJobCircularView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostJobCircularView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostJobCircularView extends Fragment {

    public PostJobCircularView() {
    }

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
    }


    public CircularStepsView[] getCircularStepsViews() {
        return circularStepsViews;
    }

    //4 steps circular view
    private CircularStepsView[] circularStepsViews = new CircularStepsView[4];
    private View view;
    private Context context;
}
