package com.jiffyjob.nimblylabs.topNavigation;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browseCategories.Model.JobModel;
import com.jiffyjob.nimblylabs.browseIndividual.Event.JobModelStickyEvent;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.database.DBHelper;
import com.nineoldandroids.animation.Animator;

import de.greenrobot.event.EventBus;

/**
 * Created by himur on 10/11/2016.
 */
public class BrowseIndividualTopNaviFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getActivity(), DBHelper.DATABASE_JOB, null, DBHelper.DATABASE_VERSION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_navigation, container, false);
        RelativeLayout navigationRL = (RelativeLayout) view.findViewById(R.id.navigationRL);

        View browseView = inflater.inflate(R.layout.top_navigation_browse_individual, (ViewGroup) view, false);
        navigationRL.addView(browseView);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void setAttribute(@NonNull DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    private void init() {
        View view = getView();
        if (view != null) {
            hamburgerBtn = (ImageButton) view.findViewById(R.id.hamburgerBtn);
            CheckBox starredCB = (CheckBox) view.findViewById(R.id.starredCB);
            ImageButton shareBtn = (ImageButton) view.findViewById(R.id.shareBtn);

            starredCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JobModelStickyEvent event = EventBus.getDefault().getStickyEvent(JobModelStickyEvent.class);
                    JobModel model = event.getJobModel();
                    if (model != null) {
                        boolean isChecked = ((CheckBox) v).isChecked();
                        model.IsStarred = isChecked;
                        dbHelper.updateJob(model);
                        EventBus.getDefault().removeStickyEvent(JobModelStickyEvent.class);
                    }
                }
            });

            changeHamburgerBtn();
        }
    }

    /**
     * Change hamburger button, removing it and replace with back button
     */
    private void changeHamburgerBtn() {
        Context context = getActivity();
        View view = getView();
        if (context != null && hamburgerBtn != null && view != null) {
         /*   RelativeLayout navigationRL = (RelativeLayout) view.findViewById(R.id.navigationRL);
            navigationRL.setVisibility(View.GONE);*/

            hamburgerBtn.setVisibility(View.GONE);
            hamburgerBtn.setImageResource(R.drawable.chevron_left_white);
            hamburgerBtn.setPadding(8, 0, 0, 8);
            hamburgerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                        getFragmentManager().popBackStack();
                    }
                }
            });
            LinearLayout topMenuView = (LinearLayout) view.findViewById(R.id.topMenu);
            YoYo.with(Techniques.FlipInX)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            hamburgerBtn.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .delay(Utilities.getAnimationFast())
                    .playOn(topMenuView);
        }
    }

    private DBHelper dbHelper;
    private ImageButton hamburgerBtn;
    private DrawerLayout drawerLayout;
}
