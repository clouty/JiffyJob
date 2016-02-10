package com.jiffyjob.nimblylabs.browsePage;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browseIndividual.BrowseIndividualView;
import com.jiffyjob.nimblylabs.browsePage.event.BrowseIndividualViewEvent;
import com.jiffyjob.nimblylabs.commonUtilities.IASyncResponse;
import com.jiffyjob.nimblylabs.httpServices.QueryJobService;
import com.jiffyjob.nimblylabs.locationService.GeocoderService;
import com.jiffyjob.nimblylabs.locationService.UserLocationService;
import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        new BackgroundAsyncTask().execute("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        userLocationService.onStop();
    }

    public void onEvent(BrowseIndividualViewEvent event) {
        try {
            BrowseIndividualView browseIndividualView = new BrowseIndividualView();
            browseIndividualView.setAttributes(event.getModel());
            FragmentTransaction fragmentTransaction = ((Activity) context).getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, browseIndividualView, JiffyJobMainActivity.FRAG_CONTAINER_TAG);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (ClassCastException e) {

        }
    }

    private void pullToRefreshListenter() {
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                new BackgroundAsyncTask().execute("");
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
        browsePageModelList = new ArrayList<>();
        browsePageAdapter = new BrowsePageAdapter(context, R.layout.browse_page_item_v3, browsePageModelList);
        browsePageAdapter.setFragment(this);
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
        Location userLocation = userLocationService.getLocation();
        if (userLocation != null) {
            GeocoderService geocoderService = new GeocoderService(context);
            Address address = geocoderService.getAddress(userLocation.getLongitude(), userLocation.getLatitude());
            addressMap = geocoderService.getAddressMap(address);
        } else {
            SharedPreferences settings = context.getSharedPreferences(String.valueOf(R.string.prefs_name), 0);
            float longitude = settings.getFloat("long", 0);
            float latitude = settings.getFloat("lat", 0);
            GeocoderService geocoderService = new GeocoderService(context);
            Address address = geocoderService.getAddress(longitude, latitude);
            addressMap = geocoderService.getAddressMap(address);
        }
        if (addressMap != null) {
            query = String.format("{\"searchType\":\"LocationSearch\", \"searchString\":\"%s,%s,%s\"}",
                    addressMap.get("City"), addressMap.get("State"), addressMap.get("Country"));
            if (query != null && !query.equalsIgnoreCase("")) {
                Date[] startEndTime = {Calendar.getInstance().getTime(), Calendar.getInstance().getTime()};
                final Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo);
                final BitmapDrawable profileBitmapDrawable = new BitmapDrawable(getResources(), bitmap);

                QueryJobService queryJobService = new QueryJobService(context, new IASyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        try {
                            JSONArray jsonArray = new JSONArray(output);
                            BrowsePageJsonReader browsePageJsonReader = new BrowsePageJsonReader(jsonArray, context);
                            browsePageModelList.clear();
                            browsePageModelList.addAll(browsePageJsonReader.getModelList());
                        } catch (JSONException j) {
                            System.out.println("JSONObject Error" + j.getMessage());
                        }
                        browsePageAdapter.notifyDataSetChanged();
                    }
                });
                queryJobService.execute(query);
            }
        }
    }

    private class BackgroundAsyncTask extends AsyncTask<String, Integer, Long> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pullRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Long doInBackground(String... params) {
            populateListView();
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
    private TreeMap<String, String> addressMap;
}
