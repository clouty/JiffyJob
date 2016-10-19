package com.jiffyjob.nimblylabs.browseCategories;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browseCategories.Event.ShowStarredEvent;
import com.jiffyjob.nimblylabs.browseCategories.Model.JobModel;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.database.DBHelper;
import com.jiffyjob.nimblylabs.httpServices.FeatureJobService;
import com.jiffyjob.nimblylabs.httpServices.RetrofitJiffyAPI;
import com.jiffyjob.nimblylabs.locationService.GeocoderService;
import com.jiffyjob.nimblylabs.locationService.UserLocationService;
import com.jiffyjob.nimblylabs.topNavigation.BrowseTopNaviFragment;
import com.jiffyjob.nimblylabs.topNavigation.TopNaviController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BrowseCategories extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
        lastScrollPreference = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        dbHelper = new DBHelper(getActivity(), DBHelper.DATABASE_JOB, null, DBHelper.DATABASE_VERSION);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_browse_categories, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Activity activity = getActivity();
        DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawers();
        initBrowseJob();
        updateStarred();
        YoYo.with(Techniques.SlideInUp)
                .duration(Utilities.getAnimationFast())
                .playOn(getView());
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().removeAllStickyEvents();
        new UpdateDbAsync().execute();
        //Log.e(BrowseCategories.class.getSimpleName(), "UpdateDbAsync, modelList: " + modelList.size());
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        lastScrollPreference.edit().putInt(LAST_SCROLL_POSITION, 0).apply();
        super.onDestroy();
    }

    private void initBrowseJob() {
        try {
            userLocationService = new UserLocationService(getActivity(), true);
            userLocationService.connectAPIClient();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error starting user location service.", Toast.LENGTH_SHORT).show();
        }

        View view = getView();
        if (view != null) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            recyclerViewAdapter = new RecyclerViewAdapter();
            recyclerViewAdapter.setItems(modelList);
            recyclerView.setAdapter(recyclerViewAdapter);

            pullRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.pullRefreshLayout);
            pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
            pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    callShowJob();
                }
            });

            //Control pullRefresh only active when displaying first item
            recyclerView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    int index = layoutManager.findFirstCompletelyVisibleItemPosition();
                    if (index == 0) {
                        pullRefreshLayout.setEnabled(true);
                    } else {
                        pullRefreshLayout.setEnabled(false);
                    }
                }
            });

            fetchJobs();
            /*if (modelList.size() == 0) {
                fetchJobs();
            }*/
            int lastPos = lastScrollPreference.getInt(LAST_SCROLL_POSITION, 0);
            recyclerView.scrollToPosition(lastPos);
        }
    }

    private void fetchJobs() {
        if (dbHelper.isTableExists()) {
            if (dbHelper.numberOfRows() > 0) {
                List<JobModel> jobList = dbHelper.getAllJobs();
                modelList.clear();
                backupModelList.clear();
                for (JobModel model : jobList) {
                    modelList.add(model);
                }
                backupModelList.addAll(modelList);
            } else {
                callShowJob();
            }
        }

        //Log.e(BrowseCategories.class.getSimpleName(), "fetchJobs, modelList: " + modelList.size());
        pullRefreshLayout.setRefreshing(false);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void callShowJob() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchType", "LocationSearch");
            jsonObject.put("searchString", "Boston,,");
        } catch (JSONException e) {
            Log.e(BrowseCategories.class.getSimpleName(), "Json string invalid format");
        }

        RetrofitJiffyAPI retrofitJiffyAPI = new RetrofitJiffyAPI();
        retrofitJiffyAPI.showJob(jsonObject.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<JobModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(BrowseCategories.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onNext(List<JobModel> jobModelList) {
                        new SyncJobModelAsync().execute(jobModelList);
                    }
                });
    }

    private void updateStarred() {
        Fragment fragment = getFragmentManager().findFragmentByTag(TopNaviController.topNaviTag);
        if (fragment instanceof BrowseTopNaviFragment) {
            ((BrowseTopNaviFragment) fragment).setIsStarred(isShowStarred);
        }
    }

    private TreeMap retrieveUserLocation() {
        TreeMap addressMap;
        Context context = getActivity();
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
        return addressMap;
    }

    //Fetch feature jobs
    private void fetchFeatureJobUrl() {
        featureUrlList.clear();
        featureUrlList.add("http://www.nimblylabs.com/jjws/gphotos/generic_fnb5.jpg");
        featureUrlList.add("http://www.nimblylabs.com/jjws/gphotos/generic_fnb3.jpg");
        featureUrlList.add("http://www.nimblylabs.com/jjws/gphotos/generic_fnb1.jpg");
    }

    private void fetchFeatureJobs() {
        featureJobService = new FeatureJobService();
        /*featureJobService.subscribe(this);*/
    }

    public void onEvent(ShowStarredEvent event) {
        isShowStarred = event.isShowStarred();
        if (event.isShowStarred()) {
            List<JobModel> tempList = new ArrayList<>();
            for (JobModel model : modelList) {
                if (model.IsStarred) {
                    tempList.add(model);
                }
            }
            modelList.clear();
            modelList.add(0, new JobModel());
            modelList.addAll(tempList);
        } else {
            modelList.clear();
            modelList.addAll(backupModelList);
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private class UpdateDbAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (dbHelper.numberOfRows() == 0) {
                dbHelper.insertJobList(modelList);
            } else {
                dbHelper.updateJobList(modelList);
            }
            return null;
        }
    }

    /**
     * Provide new jobModel list into async method, call by CallShowJob()
     */
    private class SyncJobModelAsync extends AsyncTask<List<JobModel>, Void, List<JobModel>> {

        @Override
        protected List<JobModel> doInBackground(List<JobModel>... params) {
            List<JobModel> newJobModelList = params[0];
            for (JobModel newModel : newJobModelList) {
                if (newModel.JobID != null) {
                    JobModel oldModel = findModelByJobId(newModel.JobID);
                    if (oldModel != null) {
                        newModel.IsStarred = oldModel.IsStarred;
                    }
                }
            }
            return newJobModelList;
        }

        @Override
        protected void onPostExecute(List<JobModel> jobModelList) {
            super.onPostExecute(jobModelList);
            if (jobModelList != null && jobModelList.size() > 0) {
                modelList.clear();
                backupModelList.clear();
                modelList.add(0, new JobModel());
                for (JobModel model : jobModelList) {
                    modelList.add(model);
                }
                backupModelList.addAll(modelList);
            }
            //Log.e(BrowseCategories.class.getSimpleName(), "SyncJobModelAsync, modelList: " + modelList.size());
            pullRefreshLayout.setRefreshing(false);
            recyclerViewAdapter.notifyDataSetChanged();
            isShowStarred = false;
            updateStarred();
        }

        /**
         * Find old model by ID
         *
         * @param jobId
         * @return
         */
        private JobModel findModelByJobId(String jobId) {
            for (JobModel model : modelList) {
                if (model.JobID != null && model.JobID.equalsIgnoreCase(jobId)) {
                    return model;
                }
            }
            return null;
        }
    }

    private SharedPreferences lastScrollPreference;
    public static final String LAST_SCROLL_POSITION = "LAST_SCROLL_POSITION";
    private static boolean isShowStarred = false;

    private List<JobModel> backupModelList = new ArrayList<>();
    private List<JobModel> modelList = new ArrayList<>();
    private List<String> featureUrlList = new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;
    private DBHelper dbHelper;

    private UserLocationService userLocationService;
    private FeatureJobService featureJobService;
    private PullRefreshLayout pullRefreshLayout;
}
