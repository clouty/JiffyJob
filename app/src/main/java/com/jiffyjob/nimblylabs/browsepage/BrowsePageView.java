package com.jiffyjob.nimblylabs.browsepage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
import com.jiffyjob.nimblylabs.app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by NimblyLabs on 10/3/2015.
 */
public class BrowsePageView extends Fragment {

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
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
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
        Date[] startEndTime = {
                Calendar.getInstance().getTime(), Calendar.getInstance().getTime()};
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.swagdog2);
        String message = "Looking for software engineer to develop cutting edge technology, looking for innovative individual that has passion in mobile programming.";
        BrowsePageModel model = new BrowsePageModel(new Date(), startEndTime, "Woodlands, Cause way point", "Developer", message, "The Verge", "(Google Inc)", bitmap, "80%");
        browsePageModelList.add(model);
        browsePageModelList.add(model);
        browsePageModelList.add(model);
        browsePageModelList.add(model);
        browsePageModelList.add(model);
        browsePageAdapter.notifyDataSetChanged();
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
}
