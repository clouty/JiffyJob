package com.jiffyjob.nimblylabs.managePost.confirmJob;

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
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.easing.linear.Linear;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browsePage.BrowsePageModel;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.carlom.stikkyheader.core.StikkyHeaderBuilder;
import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;

/**
 * Created by NielPC on 8/22/2016.
 */
public class ConfirmJobFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isViewDestroy = false;
        return inflater.inflate(R.layout.fragment_manage_confirm, container, false);
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

    @Override
    public void onStop() {
        super.onStop();
        isViewDestroy = true;
    }

    private void init() {
        View view = getView();
        if (view != null && !isViewDestroy) {
            ListView confirmLV = (ListView) view.findViewById(R.id.confirmLV);
            CalendarPickerView calendarView = (CalendarPickerView) view.findViewById(R.id.calendarView);
            LinearLayout calendarLL = (LinearLayout) view.findViewById(R.id.calendarLL);

            Calendar nextYear = Calendar.getInstance();
            nextYear.add(Calendar.YEAR, 1);
            Date today = new Date();
            calendarView.init(today, nextYear.getTime())
                    .inMode(CalendarPickerView.SelectionMode.RANGE);
            /*calendarView.highlightDates(dateList);*/

            StikkyHeaderBuilder.stickTo(confirmLV)
                    .allowTouchBehindHeader(true)
                    .setHeader(R.id.calendarLL, (ViewGroup) view)
                    .minHeightHeader(150)
                    .build();
            populateConfirmedDates(confirmLV);
        }
    }

    private void populateConfirmedDates(ListView listView) {
        Context context = getActivity();
        List<String> confirmList = new ArrayList<>();
        confirmList.add("Nike road show");
        confirmList.add("D'Cafe Barista");
        confirmList.add("Computex road show");
        confirmList.add("Baby sitter");
        confirmList.add("Food tester");
        confirmList.add("TEST6");
        confirmList.add("TEST7");
        confirmList.add("TEST8");
        confirmList.add("TEST9");
        confirmList.add("TEST10");
        confirmList.add("TEST11");
        confirmList.add("TEST12");
        confirmList.add("TEST13");
        confirmList.add("TEST14");
        confirmList.add("TEST15");
        confirmList.add("TEST16");
        confirmList.add("TEST17");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, confirmList);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    private List<BrowsePageModel> modelList;
    private static boolean isViewDestroy = false;
}
