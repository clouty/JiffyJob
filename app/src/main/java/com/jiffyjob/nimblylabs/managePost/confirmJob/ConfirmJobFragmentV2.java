package com.jiffyjob.nimblylabs.managePost.confirmJob;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.BeforeLoginActivityV2;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browseCategories.Model.JobModel;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.database.DBHelper;
import com.jiffyjob.nimblylabs.httpServices.RetrofitJiffyAPI;
import com.jiffyjob.nimblylabs.managePost.model.AppliedJobModel;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by NielPC on 10/16/2016.
 */
public class ConfirmJobFragmentV2 extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        prefs = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isViewDestroy = false;
        return inflater.inflate(R.layout.fragment_manage_confirmv2, container, false);
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
    }

    private void init() {
        View view = getView();
        if (view != null && !isViewDestroy) {
            CalendarPickerView calendarView = (CalendarPickerView) view.findViewById(R.id.calendarView);
            Calendar nextYear = Calendar.getInstance();
            nextYear.add(Calendar.YEAR, 1);
            Date today = new Date();
            calendarView.init(today, nextYear.getTime())
                    .inMode(CalendarPickerView.SelectionMode.RANGE);

            //Retrieve applied Jobs from local database
            try {
                String idStr = null;
                if (prefs.getString(BeforeLoginActivityV2.FACEBOOK_ID, null) != null) {
                    idStr = prefs.getString(BeforeLoginActivityV2.FACEBOOK_ID, null);
                } else {
                    idStr = prefs.getString(BeforeLoginActivityV2.LINKEDIN_ID, null);
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("UserID", idStr);
                callAppliedJobs(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void callAppliedJobs(String jsonStr) {
        RetrofitJiffyAPI retrofitJiffyAPI = new RetrofitJiffyAPI();
        retrofitJiffyAPI.getAppliedJobs(jsonStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AppliedJobModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<AppliedJobModel> appliedJobModels) {
                        updateAppliedJobs(appliedJobModels);
                    }
                });
    }

    private void updateAppliedJobs(List<AppliedJobModel> appliedJobModels) {
        DBHelper dbHelper = new DBHelper(getActivity(), DBHelper.DATABASE_JOB, null, DBHelper.DATABASE_VERSION);
        for (AppliedJobModel model : appliedJobModels) {
            if (model.getStatus() == 0) { //Retrieve only Confirmed jobs
                JobModel jobModel = dbHelper.getJobByJobID(model.getJobID());
                jobModelList.add(jobModel);
            }
        }
        jobModelList.size();
    }

    private static boolean isViewDestroy = false;
    private SharedPreferences prefs;
    private List<JobModel> jobModelList = new ArrayList<>();
}
