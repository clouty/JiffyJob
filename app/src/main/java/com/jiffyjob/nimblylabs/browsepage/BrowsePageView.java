package com.jiffyjob.nimblylabs.browsePage;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.IASyncResponse;
import com.jiffyjob.nimblylabs.httpServices.QueryJobService;
import com.jiffyjob.nimblylabs.locationService.GeocoderService;
import com.jiffyjob.nimblylabs.locationService.LocationUpdatedEvent;
import com.jiffyjob.nimblylabs.locationService.UserLocationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import de.greenrobot.event.EventBus;

/**
 * Created by NimblyLabs on 10/3/2015.
 */
public class BrowsePageView extends Fragment implements IASyncResponse {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_browse_page, container, false);
        context = view.getContext();
        init();
        pullToRefreshListenter();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void processFinish(String output) {
        try {
            pullRefreshLayout.setRefreshing(false);
            JSONArray jsonArray = new JSONArray(output);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "JSONException, unable to parse output", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userLocationService.onResume();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        userLocationService.onStop();
        super.onDestroyView();
    }

    public void onEvent(LocationUpdatedEvent event) {
        Address address = event.getAddress();
        GeocoderService geocoderService = new GeocoderService(context);
        TreeMap<String, String> addressMap = geocoderService.getAddressMap(address);
        query = String.format("{\"searchType\":\"LocationSearch\", \"searchString\":\"%s,%s,%s\"}",
                addressMap.get("City"), addressMap.get("State"), addressMap.get("Country"));
    }

    private void pullToRefreshListenter() {
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                populateListView();
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
        listView.setOnItemClickListener(new BrowsePageOnItemClickListener(context, browsePageModelList));

        try {
            userLocationService = new UserLocationService(context, false);
            userLocationService.connectAPIClient();
        } catch (Exception e) {
            Toast.makeText(context, "Error starting user location service.", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateListView() {
        pullRefreshLayout.setRefreshing(true);
        if (query != null || query.equalsIgnoreCase("")) {
            Date[] startEndTime = {Calendar.getInstance().getTime(), Calendar.getInstance().getTime()};
            final Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo);
            final BitmapDrawable profileBitmapDrawable = new BitmapDrawable(getResources(), bitmap);


            QueryJobService queryJobService = new QueryJobService(context, new IASyncResponse() {
                @Override
                public void processFinish(String output) {
                    try {
                        JSONArray jsonArray = new JSONArray(output);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject element = (JSONObject) jsonArray.get(i);
                            String jobID = element.getString("ID2");
                            String creatorID = element.getString("ID3");
                            String scopes = element.getString("ScopesJson");
                            String title = element.getString("JobTitle");
                            String city = element.getString("City");
                            String jobPax = element.getString("TotalPax");
                            //String jobCat = element.getString("JobCategories");
                            String startDateTime = element.getString("StartDateTime");
                            String endDateTime = element.getString("EndDateTime");

                            Date startDate = null;
                            Date endDate = null;
                            Date[] startEndTime = new Date[2];
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                            try {
                                startDate = dateFormat.parse(startDateTime);
                                endDate = dateFormat.parse(endDateTime);
                                startEndTime[0] = startDate;
                                startEndTime[1] = endDate;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            BrowsePageModel model = new BrowsePageModel(title, jobID, startDate, startEndTime, city, jobPax, creatorID, "", profileBitmapDrawable, "", "");
                            browsePageModelList.add(model);
                        }
                    } catch (JSONException j) {
                        System.out.println("JSONObject Error" + j.getMessage());
                    }
                    browsePageAdapter.notifyDataSetChanged();
                }
            });
            queryJobService.execute(query);
        }
        pullRefreshLayout.setRefreshing(false);
    }

    private class BackgroundAsyncTask extends AsyncTask<String, Integer, Long> {

        @Override
        protected Long doInBackground(String... params) {
            //populateListView();
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            pullRefreshLayout.setRefreshing(false);
        }
    }

    private String query;
    private View view;
    private Context context;
    private PullRefreshLayout pullRefreshLayout;
    private ListView listView;
    private ArrayList<BrowsePageModel> browsePageModelList;
    private BrowsePageAdapter browsePageAdapter;
    private QueryJobService queryJobService;
    private UserLocationService userLocationService;
}
