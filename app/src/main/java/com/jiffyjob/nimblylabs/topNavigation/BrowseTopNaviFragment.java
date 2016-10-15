package com.jiffyjob.nimblylabs.topNavigation;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browseCategories.Event.ShowStarredEvent;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity;
import com.jiffyjob.nimblylabs.topNavigation.Event.ChangeMainFragmentEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by himur on 6/14/2016.
 */
public class BrowseTopNaviFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setAttribute(@NonNull DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_navigation, container, false);
        RelativeLayout navigationRL = (RelativeLayout) view.findViewById(R.id.navigationRL);

        View browseView = inflater.inflate(R.layout.top_navigation_browse, (ViewGroup) view, false);
        navigationRL.addView(browseView);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        initListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setIsStarred(boolean isStarred) {
        starredCB.setChecked(isStarred);
    }

    private void init() {
        View view = getView();
        if (view != null) {
            hamburgerBtn = (ImageButton) view.findViewById(R.id.hamburgerBtn);
            manageBtn = (ImageButton) view.findViewById(R.id.manageBtn);
            starredCB = (CheckBox) view.findViewById(R.id.starredBtn);
            searchET = (EditText) view.findViewById(R.id.searchET);
            starredCB.setChecked(isStarred);

            LinearLayout topMenuView = (LinearLayout) view.findViewById(R.id.topMenu);
            YoYo.with(Techniques.FlipInX)
                    .delay(Utilities.getAnimationFast())
                    .playOn(topMenuView);
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

        manageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeMainFragmentEvent(JiffyJobMainActivity.MenuItemEnum.Manage));
            }
        });

        starredCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStarred = ((CheckBox) v).isChecked();
                EventBus.getDefault().post(new ShowStarredEvent(isStarred));
            }
        });

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private static boolean isStarred = false;
    private ImageButton hamburgerBtn, manageBtn;
    private CheckBox starredCB;
    private EditText searchET;
    private DrawerLayout drawerLayout;
    private static BrowseTopNaviFragment browseTopNaviFragment = null;
}
