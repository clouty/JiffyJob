package com.jiffyjob.nimblylabs.main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jiffyjob.nimblylabs.BeforeLoginActivityV2;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.beforeLoginFragmentViews.UserInfoModel;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.main.Event.UserInfoStickEvent;
import com.jiffyjob.nimblylabs.notification.Config;
import com.jiffyjob.nimblylabs.questionnaireFragmentView.QnsCameraFragment;
import com.jiffyjob.nimblylabs.topNavigation.Event.ChangeMainFragmentEvent;
import com.jiffyjob.nimblylabs.topNavigation.Event.TopNavigationChangedEvent;
import com.jiffyjob.nimblylabs.topNavigation.TopNaviController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class JiffyJobMainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiffy_job_main);
        init();
        populateDrawerItems();
        // Set the adapter for the list view
        drawerItemAdapter = new DrawerItemAdapter(this, R.layout.custom_drawer_item, drawerItems);
        mDrawerList.setAdapter(drawerItemAdapter);
        prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().registerSticky(this);
        }

        if (model == null) {
            String linkedinJson = prefs.getString(BeforeLoginActivityV2.LINKEDIN_USER_INFO, null);
            String facebookJson = prefs.getString(BeforeLoginActivityV2.FACEBOOK_USER_INFO, null);
            try {
                if (linkedinJson != null) {
                    model = new UserInfoModel(new JSONObject(linkedinJson), BeforeLoginActivityV2.LINKEDIN_USER_INFO);
                    drawerItemAdapter.updateUserImage(model);
                } else if (facebookJson != null) {
                    model = new UserInfoModel(new JSONObject(facebookJson), BeforeLoginActivityV2.LINKEDIN_USER_INFO);
                    drawerItemAdapter.updateUserImage(model);
                }
                drawerItemAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Fragment fragment = getFragmentManager().findFragmentByTag(FRAG_CONTAINER_TAG);
        if (fragment == null) {
            drawerItemAdapter.createFragment(MenuItemEnum.Browse);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int result = 0;
        for (int i : grantResults) {
            result += i;
        }
        //QnsCameraFragment Permission reply
        if (QnsCameraFragment.PERMISSION_CAMERA_CODE == requestCode && result != PackageManager.PERMISSION_GRANTED) {
            String permissionText = this.getResources().getString(R.string.permissionCamera);
            Toast.makeText(this, permissionText, Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        } else if (QnsCameraFragment.PERMISSION_CAMERA_CODE == requestCode && result == PackageManager.PERMISSION_GRANTED) {
            Fragment fragment = getFragmentManager().findFragmentByTag(FRAG_CONTAINER_TAG);
            if (fragment instanceof QnsCameraFragment) {
                fragment.onStart();
            }
        }
    }

    private void init() {
        this.setTitle("JiffyJob");
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        //Create control for top navigation
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            topNaviController = new TopNaviController(getFragmentManager(), mDrawerLayout);
            topNaviController.createBrowseTopNavi();
        }
    }

    private void populateDrawerItems() {
        drawerItems = new ArrayList<>();
        drawerItems.add(new DrawerItemObject(null, MenuItemEnum.Profile));//create userInfo
        drawerItems.add(new DrawerItemObject(getResources().getDrawable(R.drawable.btn_state_drawer_browse), MenuItemEnum.Browse));
        drawerItems.add(new DrawerItemObject(this.getResources().getDrawable(R.drawable.btn_state_drawer_inbox), MenuItemEnum.Inbox));
        drawerItems.add(new DrawerItemObject(this.getResources().getDrawable(R.drawable.btn_state_drawer_manage), MenuItemEnum.Manage));
        drawerItems.add(new DrawerItemObject(this.getResources().getDrawable(R.drawable.btn_state_drawer_shop), MenuItemEnum.Shop));
        drawerItems.add(new DrawerItemObject(this.getResources().getDrawable(R.drawable.btn_state_drawer_preferences), MenuItemEnum.Preferences));
        drawerItems.add(new DrawerItemObject(this.getResources().getDrawable(R.drawable.btn_state_drawer_app_settings), MenuItemEnum.AppSetting));
    }

    //Events handlers
    public void onEvent(UserInfoStickEvent event) {
        model = event.getUserInfoModel();
        drawerItemAdapter.updateUserImage(model);
        EventBus.getDefault().removeStickyEvent(event);
    }

    public void onEvent(TopNavigationChangedEvent event) {
        View view = this.findViewById(R.id.top_navigation);
        YoYo.with(Techniques.FlipInX)
                .delay(Utilities.getAnimationFast())
                .playOn(view);
        switch (event.getMenuItem()) {
            case Browse:
                this.findViewById(R.id.top_navigation).setVisibility(View.VISIBLE);
                topNaviController.createBrowseTopNavi();
                break;
            case BrowseIndividual:
                this.findViewById(R.id.top_navigation).setVisibility(View.VISIBLE);
                topNaviController.createBrowseIndivTopNavi();
                break;
            case Manage:
                this.findViewById(R.id.top_navigation).setVisibility(View.VISIBLE);
                topNaviController.createManageTopNavi();
                break;
            default:
                this.findViewById(R.id.top_navigation).setVisibility(View.GONE);
                topNaviController.removeTopNavi();
                break;
        }
    }

    public void onEvent(ChangeMainFragmentEvent event) {
        drawerItemAdapter.createFragment(event.getFragmentType());
    }

    private SharedPreferences prefs;
    private UserInfoModel model;
    private ArrayList<DrawerItemObject> drawerItems;
    private DrawerItemAdapter drawerItemAdapter;
    private TopNaviController topNaviController;
    private ListView mDrawerList;

    public enum MenuItemEnum {
        Profile(0), Browse(1), BrowseIndividual(2), Inbox(3), Manage(4), Shop(5), Preferences(6), AppSetting(7);

        private final int value;

        MenuItemEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static String FRAG_CONTAINER_TAG = "mainFragContainer";
}
