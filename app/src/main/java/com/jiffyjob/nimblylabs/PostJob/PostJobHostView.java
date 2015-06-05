package com.jiffyjob.nimblylabs.postJob;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiffyjob.nimblylabs.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostJobHostView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostJobHostView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostJobHostView extends Fragment {
    public PostJobHostView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_job_host_view, container, false);
        context = view.getContext();
        init();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_job_host_view, container, false);
    }

    private void init() {
        PostJobCircularView postJobCircularView = new PostJobCircularView();
        postJobCircularView.setArguments(this.getActivity().getIntent().getExtras());

        PostJobStep3View postJobStep3View = new PostJobStep3View();
        postJobStep3View.setArguments(this.getActivity().getIntent().getExtras());

        FragmentManager manager = this.getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.circularFragmentView, postJobCircularView, PostJobCircularView.class.getSimpleName());
        transaction.add(R.id.postJobStepView, postJobStep3View, PostJobStep3View.class.getSimpleName());
        transaction.commit();
    }

    private View view;
    private Context context;
}