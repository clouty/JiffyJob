package com.jiffyjob.nimblylabs.browseIndividual;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.annotations.JsonAdapter;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browseCategories.Model.JobModel;
import com.jiffyjob.nimblylabs.browseIndividual.Event.JobApplyEvent;
import com.jiffyjob.nimblylabs.commonUtilities.ScalingUtilities;
import com.jiffyjob.nimblylabs.commonUtilities.StringHelper;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.database.DBHelper;
import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity;
import com.jiffyjob.nimblylabs.topNavigation.Event.TopNavigationChangedEvent;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * TEST COMMENT FOR GITHUB, 5/6/2015
 * Created by NimblyLabs on 22/3/2015.
 */
public class BrowseIndividualView extends Fragment implements OnMapReadyCallback {

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
        View view = inflater.inflate(R.layout.fragment_browse_individual, container, false);
        context = view.getContext();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().post(new TopNavigationChangedEvent(JiffyJobMainActivity.MenuItemEnum.BrowseIndividual));
        init();
        initListener();
        YoYo.with(Techniques.SlideInUp)
                .duration(Utilities.getAnimationFast())
                .playOn(getView());
    }

    @Override
    public void onStop() {
        super.onStop();
        friendIconLL.removeAllViews();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentByTag(mapFragTag);
        if (mapFragment != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.remove(mapFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
        EventBus.getDefault().post(new TopNavigationChangedEvent(JiffyJobMainActivity.MenuItemEnum.Browse));
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        context = null;
        super.onDestroy();
    }

    public JobModel getJobModel() {
        return jobModel;
    }

    private void init() {
        View view = getView();
        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mapFragment, mapFragTag);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);

        if (view == null) {
            return;
        }

        ImageView employerIV = (ImageView) view.findViewById(R.id.employerIV);
        TextView employerTV = (TextView) view.findViewById(R.id.employerTV);
        /*RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);*/
        TextView datePostedTV = (TextView) view.findViewById(R.id.datePostedTV);
        TextView jobTitleTV = (TextView) view.findViewById(R.id.jobTitleTV);
        TextView calenderTV = (TextView) view.findViewById(R.id.calenderTV);
        TextView timeTV = (TextView) view.findViewById(R.id.timeTV);
        TextView locationTV = (TextView) view.findViewById(R.id.locationTV);
        TextView paymentTV = (TextView) view.findViewById(R.id.paymentTV);
        TextView recruitmentTV = (TextView) view.findViewById(R.id.recruitmentTV);

        //TODO: set rating bar
        try {
            String dateStr = jobModel.DatePosted;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date postedDate = df.parse(dateStr);
            long postedTime = postedDate.getTime();
            long currentTIme = new Date().getTime();
            long diffTime = Math.abs(currentTIme - postedTime);
            long dayDiff = TimeUnit.MILLISECONDS.toDays(diffTime);
            if (dayDiff == 0) {
                datePostedTV.setText("Posted today");
            } else if (dayDiff == 1) {
                datePostedTV.setText("Posted yesterday");
            } else {
                datePostedTV.setText(String.format("%s days ago", dayDiff));
            }
        } catch (ParseException e) {

        }

        //set employer logo
        Glide.with(context)
                .load(R.drawable.jiffyjob)
                .fitCenter()
                .bitmapTransform(new CropCircleTransformation(context))
                .into(employerIV);

        //adding friends icons
        ImageView friendIV = new ImageView(context);
        friendIV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        friendIV.getLayoutParams().height = (int) getResources().getDimension(R.dimen.icon_height);
        friendIV.getLayoutParams().width = (int) getResources().getDimension(R.dimen.icon_width);
        friendIV.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(context)
                .load(R.drawable.jiffyjob)
                .fitCenter()
                .bitmapTransform(new CropCircleTransformation(context))
                .into(friendIV);
        friendIconLL = (LinearLayout) view.findViewById(R.id.friendIconLL);
        friendIconLL.addView(friendIV);

        employerTV.setText(jobModel.CompanyName);
        jobTitleTV.setText(jobModel.JobTitle);

        String scopes = jobModel.Scope;
      /*LinearLayout scopeListLL = (LinearLayout) view.findViewById(R.id.scopeListLL);
        String[] scopeArr = scopes.split(",");
        for (String item : scopeArr) {
            View scopeView = LayoutInflater.from(context).inflate(R.layout.browse_individual_item, null);
            scopeView.setClickable(false);
            TextView textview = (TextView) scopeView.findViewById(R.id.text1);
            textview.setText(item);
            scopeListLL.addView(scopeView);
        }*/

        ExpandableTextView expand_text_view = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
        expand_text_view.setText(scopes);

        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date startDate = df.parse(jobModel.StartDateTime);
            Date endDate = df.parse(jobModel.EndDateTime);
            DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            DateFormat timeFormat = new SimpleDateFormat("ha", Locale.ENGLISH);
            String startDateStr = dateFormat.format(startDate);
            String endDateStr = dateFormat.format(endDate);
            String startTimeStr = timeFormat.format(startDate);
            String endTimeStr = timeFormat.format(endDate);

            String startEndDate = String.format("%s - %s", startDateStr, endDateStr);
            calenderTV.setText(startEndDate);
            String startEndTime = String.format("%s - %s", startTimeStr, endTimeStr);
            timeTV.setText(startEndTime);
            String location = String.format("%s, %s", jobModel.State, jobModel.City);
            locationTV.setText(location);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String payoutFormatted = decimalFormat.format(jobModel.Payout);
        String currencyCode = jobModel.SalaryCurrencyCode.toUpperCase();
        String payoutStr = String.format("%s$%s/Hr", currencyCode, payoutFormatted);
        paymentTV.setText(payoutStr);

        String recruitmentStr = String.format("%s / %s recruited", jobModel.CurrentlyRecruited, jobModel.TotalPax);
        recruitmentTV.setText(recruitmentStr);
    }

    private void initListener() {
        View view = getView();
        if (view != null) {
            Button applyBtn = (Button) view.findViewById(R.id.applyBtn);
            applyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApplyDialogFragment applyDialogFragment = new ApplyDialogFragment();
                    applyDialogFragment.setIsApplied(false);
                    applyDialogFragment.setModel(jobModel);
                    applyDialogFragment.show(getFragmentManager(), ApplyDialogFragment.class.getSimpleName());
                }
            });
        }
    }

    //Google map methods
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // We will provide our own zoom controls.
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);
        this.googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.mapstyle));

        if (jobModel != null && checkMapReady()) {
            String markerStr = String.format("%s, %s", jobModel.State, jobModel.City);
            double longitude = jobModel.Lng;
            double latitude = jobModel.Lat;
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude))
                    .zoom(13.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();

            this.googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("green_marker_fill", 100, 100)))
                    .draggable(false)
                    .position(new LatLng(latitude, longitude))
                    .title(markerStr));
            this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), duration, null);
        }
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", context.getPackageName()));
        return ScalingUtilities.createScaledBitmap(imageBitmap, width, height, ScalingUtilities.ScalingLogic.FIT);
    }

    /**
     * When the map is not ready the CameraUpdateFactory cannot be used. This should be called on
     * all entry points that call methods on the Google Maps API.
     */
    private boolean checkMapReady() {
        if (googleMap == null) {
            Toast.makeText(context, "Google map is not ready", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void setAttributes(JobModel jobModel) {
        this.jobModel = jobModel;
    }

    //Event handlers
    public void onEvent(JobApplyEvent event) {
        //Status 200 - Ok
        //Status 406 - Not accepted
        //Status 304 - Not modified
        String result = event.getResult();
        if (result.contains("Status 200")) {
            ApplyDialogFragment applyDialogFragment = new ApplyDialogFragment();
            applyDialogFragment.setIsApplied(true);
            applyDialogFragment.setModel(jobModel);
            applyDialogFragment.show(getFragmentManager(), ApplyDialogFragment.class.getSimpleName());
        }
    }

    private class StarredDBSync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            DBHelper dbHelper = new DBHelper(getContext(), DBHelper.DATABASE_JOB, null, DBHelper.DATABASE_VERSION);
            List<JobModel> jobModelList = new ArrayList<>();
            jobModelList.add(jobModel);
            dbHelper.updateJobList(jobModelList);
            return null;
        }
    }

    /**
     * The amount by which to scroll the camera. Note that this amount is in raw pixels, not dp
     * (density-independent pixels).
     */
    private final String mapFragTag = "mapFragment";
    private final int duration = 3000;
    private GoogleMap googleMap;
    private Context context;
    private JobModel jobModel;
    private LinearLayout friendIconLL;
}
