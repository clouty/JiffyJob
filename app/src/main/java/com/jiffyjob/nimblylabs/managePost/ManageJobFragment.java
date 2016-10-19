package com.jiffyjob.nimblylabs.managePost;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity;
import com.jiffyjob.nimblylabs.managePost.confirmJob.ConfirmJobFragment;
import com.jiffyjob.nimblylabs.managePost.confirmJob.ConfirmJobFragmentV2;
import com.jiffyjob.nimblylabs.managePost.historyJob.HistoryJobFragment;
import com.jiffyjob.nimblylabs.managePost.pendingJob.PendingJobFragment;

/**
 * Created by himur on 2/14/2016.
 */
public class ManageJobFragment {

    public ManageJobFragment(Activity context) {
        this.activity = context;
    }

    public void createConfirmJobFragment() {
        FragmentManager fragmentManager = this.activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ConfirmJobFragmentV2 confirmJobFragment = new ConfirmJobFragmentV2();
        if (!confirmJobFragment.isAdded()) {
            fragmentTransaction.replace(R.id.fragment_container, confirmJobFragment, JiffyJobMainActivity.FRAG_CONTAINER_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void createPendingJobFragment() {
        FragmentManager fragmentManager = this.activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PendingJobFragment pendingJobFragment = new PendingJobFragment();
        if (!pendingJobFragment.isAdded()) {
            fragmentTransaction.replace(R.id.fragment_container, pendingJobFragment, JiffyJobMainActivity.FRAG_CONTAINER_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void createHistoryJobFragment() {
        FragmentManager fragmentManager = this.activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HistoryJobFragment historyJobFragment = new HistoryJobFragment();
        if (!historyJobFragment.isAdded()) {
            fragmentTransaction.replace(R.id.fragment_container, historyJobFragment, JiffyJobMainActivity.FRAG_CONTAINER_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void recycle() {
        if (!activity.isDestroyed()) {
            FragmentManager fragmentManager = this.activity.getFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(JiffyJobMainActivity.FRAG_CONTAINER_TAG);
            if (fragment != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        }
    }

    private Activity activity;
}
