package com.jiffyjob.nimblylabs.browseIndividual;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browseIndividual.Event.JobApplyEvent;
import com.jiffyjob.nimblylabs.browsePage.BrowsePageModel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * TEST COMMENT FOR GITHUB, 5/6/2015
 * Created by NimblyLabs on 22/3/2015.
 */
public class BrowseIndividualView extends Fragment implements OnMapReadyCallback {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_browse_individual, container, false);
        context = view.getContext();
        init();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.remove(mapFragment);
            fragmentTransaction.commit();
        }
        context = null;
        view = null;
    }

    private void init() {
        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        ImageView userIV = (ImageView) view.findViewById(R.id.userIV);
        TextView userNameTV = (TextView) view.findViewById(R.id.userNameTV);
        TextView companyNameTV = (TextView) view.findViewById(R.id.companyNameTV);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ImageButton favoriteBtn = (ImageButton) view.findViewById(R.id.favoriteBtn);
        ImageButton shareBtn = (ImageButton) view.findViewById(R.id.shareBtn);
        TextView creationDateTV = (TextView) view.findViewById(R.id.creationDateTV);
        TextView descriptionTV = (TextView) view.findViewById(R.id.descriptionTV);
        ListView jobScopeLV = (ListView) view.findViewById(R.id.jobScopeLV);
        TextView calenderTV = (TextView) view.findViewById(R.id.calenderTV);
        TextView timeTV = (TextView) view.findViewById(R.id.timeTV);
        TextView locationTV = (TextView) view.findViewById(R.id.locationTV);
        TextView paymentTV = (TextView) view.findViewById(R.id.paymentTV);
        TextView recruitmentTV = (TextView) view.findViewById(R.id.recruitmentTV);
        Button applyBtn = (Button) view.findViewById(R.id.applyBtn);

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyDialogFragment applyDialogFragment = new ApplyDialogFragment();
                applyDialogFragment.setIsApplied(false);
                applyDialogFragment.setModel(browsePageModel);
                applyDialogFragment.show(getFragmentManager(), ApplyDialogFragment.class.getSimpleName());
            }
        });

        ratingBar.setMax(5);
        //TODO: set rating bar

        long postedTime = browsePageModel.getDatePosted().getTime();
        long currentTIme = new Date().getTime();
        long diffTime = Math.abs(currentTIme - postedTime);
        long dayDiff = TimeUnit.MILLISECONDS.toDays(diffTime);
        if (dayDiff == 0) {
            creationDateTV.setText("Posted today");
        } else if (dayDiff == 1) {
            creationDateTV.setText("Posted yesterday");
        } else {
            creationDateTV.setText(String.format("Created %s days ago", dayDiff));
        }

        String name = String.format("%s %s", browsePageModel.getFirstName(), browsePageModel.getLastName());
        userNameTV.setText(name);
        companyNameTV.setText(browsePageModel.getCompanyName());

        String scopes = browsePageModel.getScopes();
        String[] scopeArr = scopes.split(",");
        ArrayList<String> scopeList = new ArrayList<>();
        Collections.addAll(scopeList, scopeArr);
        JobScopeAdapter jobScopeAdapter = new JobScopeAdapter(context, R.layout.browse_individual_item, scopeList);
        jobScopeLV.setAdapter(jobScopeAdapter);
        jobScopeLV.setDividerHeight(0);

        descriptionTV.setText(browsePageModel.getJobTitle());
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        DateFormat timeFormat = new SimpleDateFormat("ha", Locale.ENGLISH);
        String startDate = dateFormat.format(browsePageModel.getStartDate());
        String endDate = dateFormat.format(browsePageModel.getEndDate());
        String startTime = timeFormat.format(browsePageModel.getStartDate());
        String endTime = timeFormat.format(browsePageModel.getEndDate());

        String startEndDate = String.format("%s - %s", startDate, endDate);
        calenderTV.setText(startEndDate);
        String startEndTime = String.format("%s - %s", startTime, endTime);
        timeTV.setText(startEndTime);
        String location = String.format("%s, %s", browsePageModel.getState(), browsePageModel.getCity());
        locationTV.setText(location);
        double payout = browsePageModel.getPayout();
        if (payout > 0) {
            if (browsePageModel.isSalaryDaily()) {
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                String payoutFormatted = decimalFormat.format(browsePageModel.getPayout());
                String currencyCode = browsePageModel.getCurrencyCode();
                String payoutStr = String.format("%s$%s/Daily", currencyCode, payoutFormatted);
                paymentTV.setText(payoutStr);
            } else {
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                String payoutFormatted = decimalFormat.format(browsePageModel.getPayout());
                String currencyCode = browsePageModel.getCurrencyCode();
                String payoutStr = String.format("%s$%s/Hr", currencyCode, payoutFormatted);
                paymentTV.setText(payoutStr);
            }
        } else {
            paymentTV.setText("Volunteer");
        }
        String recruitmentStr = String.format("%s / %s recruited", browsePageModel.getCurrentlyRecuited(), browsePageModel.getTotalPax());
        recruitmentTV.setText(recruitmentStr);
    }

    //Google map methods
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // We will provide our own zoom controls.
        googleMap.getUiSettings().setZoomControlsEnabled(false);

        if (browsePageModel != null) {
            String markerStr = String.format("%s, %s", browsePageModel.getState(), browsePageModel.getCity());
            double longitude = browsePageModel.getLongitude();
            double latitude = browsePageModel.getLatitude();
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude))
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();
            updateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
            googleMap.addMarker(new MarkerOptions()
                    .draggable(false)
                    .position(new LatLng(latitude, longitude))
                    .title(markerStr));
        }
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

    public void setAttributes(BrowsePageModel browsePageModel) {
        this.browsePageModel = browsePageModel;
    }

    private void updateCamera(CameraUpdate update, GoogleMap.CancelableCallback cancelableCallback) {
        if (!checkMapReady()) {
            return;
        }
        googleMap.animateCamera(update, duration, cancelableCallback);
    }

    public void onEvent(JobApplyEvent event) {
        //Status 200 - Ok
        //Status 406 - Not accepted
        //Status 304 - Not modified

        String result = event.getResult();
        if (result.contains("Status 200")) {
            ApplyDialogFragment applyDialogFragment = new ApplyDialogFragment();
            applyDialogFragment.setIsApplied(true);
            applyDialogFragment.setModel(browsePageModel);
            applyDialogFragment.show(getFragmentManager(), ApplyDialogFragment.class.getSimpleName());
        }
    }

    /**
     * The amount by which to scroll the camera. Note that this amount is in raw pixels, not dp
     * (density-independent pixels).
     */
    private final int duration = 3000;
    private GoogleMap googleMap;
    private View view;
    private Context context;
    private BrowsePageModel browsePageModel;
}
