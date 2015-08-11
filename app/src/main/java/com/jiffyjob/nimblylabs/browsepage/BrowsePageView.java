package com.jiffyjob.nimblylabs.browsePage;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.IASyncResponse;
import com.jiffyjob.nimblylabs.httpServices.AllJobService;
import com.jiffyjob.nimblylabs.httpServices.CatJobService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by NimblyLabs on 10/3/2015.
 */
public class BrowsePageView extends Fragment implements IASyncResponse {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_browse_page, container, false);
        context = view.getContext();
        init();
        pullToRefreshListenter();
        getAllJobsFromService();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void processFinish(String output) {
        try {
            JSONArray jsonArray = new JSONArray(output);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "JSONException, unable to parse output", Toast.LENGTH_SHORT).show();
        }

    }

    private void pullToRefreshListenter() {
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                new BackgroundAsyncTask().execute();
            }
        });
    }

    private void init() {
        //Details of pullRefreshLayout - https://android-arsenal.com/details/1/1084
        int[] colorArray = {Color.GREEN, Color.parseColor("#ff8000"), Color.GREEN, Color.parseColor("#ff8000")};
        pullRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.pullRefreshLayout);
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        pullRefreshLayout.setColorSchemeColors(colorArray);

        listView = (ListView) view.findViewById(R.id.listView);
        browsePageModelList = new ArrayList<BrowsePageModel>();
        browsePageAdapter = new BrowsePageAdapter(context, R.layout.browse_page_item, browsePageModelList);
        listView.setAdapter(browsePageAdapter);
        populateListView();
        listView.setOnItemClickListener(new BrowsePageOnItemClickListener(context, browsePageModelList));
    }

    private void populateListView() {
        final Date[] startEndTime = {
                Calendar.getInstance().getTime(), Calendar.getInstance().getTime()};
        final Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.swagdog2);
        String message = "Looking for software engineer to develop cutting edge technology, looking for innovative individual that has passion in mobile programming.";
        String cat = "";
        if (getArguments() == null || !getArguments().containsKey("Cat")) {
            AllJobService allJobService = new AllJobService(context, new IASyncResponse() {
                @Override
                public void processFinish(String output) {
                    try {
                        JSONArray jsonArray = new JSONArray(output);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject element = (JSONObject) jsonArray.get(i);
                            String desc = element.getString("JobDescription");
                            String location = element.getString("LocationID");
                            String company = element.getString("CreatorUserID");
                            BrowsePageModel model = new BrowsePageModel(new Date(), startEndTime, location, "Software Developer", desc, "The Verge", company, bitmap, "80%");

                            browsePageModelList.add(model);
                        }
                    } catch (JSONException j) {
                        System.out.println("JSONObject Error" + j.getMessage());
                    }

                    browsePageAdapter.notifyDataSetChanged();
                }
            });
            allJobService.execute();
        } else {
            cat = getArguments().getString("Cat");
            //generate models using http://www.nimblylabs.com/jiffyjobs_webservice/jobs/fetchcategoryjob.php
            CatJobService catJobService = new CatJobService(context, "Sales", new IASyncResponse() {
                @Override
                public void processFinish(String output) {
                    try {
                        Log.i("Json", output);
                        JSONArray jsonArray = new JSONArray(output);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject element = (JSONObject) jsonArray.get(i);
                            String desc = element.getString("JobDescription");
                            String location = element.getString("LocationID");
                            String company = element.getString("CreatorUserID");
                            BrowsePageModel model = new BrowsePageModel(new Date(), startEndTime, location, "Software Developer", desc, "The Verge", company, bitmap, "80%");

                            browsePageModelList.add(model);

                        }
                    } catch (JSONException j) {
                        Log.e(this.getClass().getSimpleName(), j.getMessage());
                    }

                    browsePageAdapter.notifyDataSetChanged();
                }
            });
            catJobService.execute();
            //
        }


        browsePageAdapter.notifyDataSetChanged();
    }

    private void getAllJobsFromService() {
        new AllJobService(context, this).execute();
    }


    private class BackgroundAsyncTask extends AsyncTask<String, Integer, Long> {

        @Override
        protected Long doInBackground(String... params) {
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            pullRefreshLayout.setRefreshing(false);
            //handler.sendEmptyMessage(0);
        }
    }

    private View view;
    private Context context;
    private PullRefreshLayout pullRefreshLayout;
    private ListView listView;
    private ArrayList<BrowsePageModel> browsePageModelList;
    private BrowsePageAdapter browsePageAdapter;
    private AllJobService allJobService;
}
