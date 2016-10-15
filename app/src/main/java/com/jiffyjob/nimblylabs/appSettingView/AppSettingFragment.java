package com.jiffyjob.nimblylabs.appSettingView;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NielPC on 7/30/2016.
 */
public class AppSettingFragment extends Fragment {
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        Activity activity = getActivity();
        DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawers();
        YoYo.with(Techniques.SlideInUp)
                .duration(Utilities.getAnimationFast())
                .playOn(getView());
    }

    private void init() {
        View view = getView();
        if (view != null) {
            ListView settingLV = (ListView) view.findViewById(R.id.settingLV);
            ListView otherSettingLV = (ListView) view.findViewById(R.id.otherSettingLV);

            settingAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
            settingLV.setAdapter(settingAdapter);
            otherSettingAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
            otherSettingLV.setAdapter(otherSettingAdapter);
            populateSettings();
        }
    }

    private void populateSettings() {
        List<String> settingList = new ArrayList<>();
        List<String> otherSettingList = new ArrayList<>();

        settingList.add("Account Settings");
        settingList.add("Notifications Settings");
        settingList.add("Share Settings");
        settingList.add("Getting Started Guide");
        settingList.add("Help & FAQ");
        settingList.add("Like us on Facebook");
        settingList.add("Follow us on Twitter");
        settingAdapter.addAll(settingList);

        otherSettingList.add("About Jiffy Jobs");
        otherSettingList.add("Log out");
        otherSettingAdapter.addAll(otherSettingList);
    }

    private ArrayAdapter<String> settingAdapter, otherSettingAdapter;
}
