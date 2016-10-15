package com.jiffyjob.nimblylabs.managePost.historyJob;

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
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NielPC on 8/22/2016.
 */
public class HistoryJobFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_history, container, false);
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
            ListView historyLV = (ListView) view.findViewById(R.id.historyLV);
            TextView historyInfoTV = (TextView) view.findViewById(R.id.historyInfoTV);
            populateHistoryDate(historyLV);
        }
    }

    private void populateHistoryDate(ListView listView) {
        Context context = getActivity();
        List<String> historyList = new ArrayList<>();
        historyList.add("Nike road show");
        historyList.add("D'Cafe Barista");
        historyList.add("Computex road show");
        historyList.add("Baby sitter");
        historyList.add("Food tester");
        historyList.add("TEST6");
        historyList.add("TEST7");
        historyList.add("TEST8");
        historyList.add("TEST9");
        historyList.add("TEST10");
        historyList.add("TEST11");
        historyList.add("TEST12");
        historyList.add("TEST13");
        historyList.add("TEST14");
        historyList.add("TEST15");
        historyList.add("TEST16");
        historyList.add("TEST17");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, historyList);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }
}
