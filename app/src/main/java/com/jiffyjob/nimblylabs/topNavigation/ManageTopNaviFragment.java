package com.jiffyjob.nimblylabs.topNavigation;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.managePost.ManageJobFragment;

/**
 * Created by NielPC on 8/20/2016.
 */
public class ManageTopNaviFragment extends Fragment {

    public void setAttribute(@NonNull DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_navigation, container, false);
        RelativeLayout navigationRL = (RelativeLayout) view.findViewById(R.id.navigationRL);

        View browseView = inflater.inflate(R.layout.top_navigation_manage, (ViewGroup) view, false);
        navigationRL.addView(browseView);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        manageJobFragment = new ManageJobFragment(getActivity());
        init();
        initListener();
    }

    @Override
    public void onDestroy() {
        if (manageJobFragment != null) {
            manageJobFragment.recycle();
        }
        super.onDestroy();
    }

    private void init() {
        View view = getView();
        if (view != null) {
            hamburgerBtn = (ImageButton) view.findViewById(R.id.hamburgerBtn);
            btn0LL = (LinearLayout) view.findViewById(R.id.btn0LL);
            btn1LL = (LinearLayout) view.findViewById(R.id.btn1LL);
            btn2LL = (LinearLayout) view.findViewById(R.id.btn2LL);
            underline0 = view.findViewById(R.id.underline0);
            underline1 = view.findViewById(R.id.underline1);
            underline2 = view.findViewById(R.id.underline2);

            underline0.setVisibility(View.VISIBLE);
            underline1.setVisibility(View.GONE);
            underline2.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        hamburgerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        btn0LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUnderline(0);
                loadFragment();
            }
        });

        btn1LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUnderline(1);
                loadFragment();
            }
        });

        btn2LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUnderline(2);
                loadFragment();
            }
        });
    }

    private void setUnderline(int position) {
        curPosition = position;
        underline0.setVisibility(View.GONE);
        underline1.setVisibility(View.GONE);
        underline2.setVisibility(View.GONE);

        if (position == 0) {
            underline0.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.BounceIn)
                    .duration(Utilities.getAnimationNormal())
                    .playOn(underline0);
        } else if (position == 1) {
            underline1.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.BounceIn)
                    .duration(Utilities.getAnimationNormal())
                    .playOn(underline1);
        } else if (position == 2) {
            underline2.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.BounceIn)
                    .duration(Utilities.getAnimationNormal())
                    .playOn(underline2);
        }
    }

    private void loadFragment() {
        if (curPosition == 0) {
            manageJobFragment.createConfirmJobFragment();
        } else if (curPosition == 1) {
            manageJobFragment.createPendingJobFragment();
        } else if (curPosition == 2) {
            manageJobFragment.createHistoryJobFragment();
        }
    }

    private static ManageTopNaviFragment manageTopNaviFragment = null;
    private ManageJobFragment manageJobFragment;
    private int curPosition = 0;
    private LinearLayout btn0LL, btn1LL, btn2LL;
    private View underline0, underline1, underline2;
    private ImageButton hamburgerBtn;
    private DrawerLayout drawerLayout;
}
