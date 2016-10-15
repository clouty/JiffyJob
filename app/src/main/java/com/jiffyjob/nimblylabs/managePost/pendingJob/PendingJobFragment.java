package com.jiffyjob.nimblylabs.managePost.pendingJob;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.carlom.stikkyheader.core.StikkyHeaderBuilder;

/**
 * Created by NielPC on 8/22/2016.
 */
public class PendingJobFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_pending, container, false);
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
            ListView pendingLV = (ListView) view.findViewById(R.id.pendingLV);
            TextView pendingInfoTV = (TextView) view.findViewById(R.id.pendingInfoTV);
            populatePendingDate(pendingLV);
        }
    }

    private void populatePendingDate(ListView listView) {
        Context context = getActivity();
        List<String> pendingList = new ArrayList<>();
        pendingList.add("Nike road show");
        pendingList.add("D'Cafe Barista");
        pendingList.add("Computex road show");
        pendingList.add("Baby sitter");
        pendingList.add("Food tester");
        pendingList.add("TEST6");
        pendingList.add("TEST7");
        pendingList.add("TEST8");
        pendingList.add("TEST9");
        pendingList.add("TEST10");
        pendingList.add("TEST11");
        pendingList.add("TEST12");
        pendingList.add("TEST13");
        pendingList.add("TEST14");
        pendingList.add("TEST15");
        pendingList.add("TEST16");
        pendingList.add("TEST17");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, pendingList);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }
}
