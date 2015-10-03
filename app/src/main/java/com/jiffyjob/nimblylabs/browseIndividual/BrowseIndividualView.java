package com.jiffyjob.nimblylabs.browseIndividual;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.jiffyjob.nimblylabs.app.R;

/**
 * TEST COMMENT FOR GITHUB, 5/6/2015
 * Created by NimblyLabs on 22/3/2015.
 */
public class BrowseIndividualView extends Fragment implements OnMapReadyCallback {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Fragment fragment = getFragmentManager().findFragmentById(R.id.map);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    private void init() {
/*        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
    }

    //Google map methods
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // We will provide our own zoom controls.
        googleMap.getUiSettings().setZoomControlsEnabled(false);

        // Show Sydney
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.87365, 151.20689), 10));
        //UpdateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),null);
     /*   UpdateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                Toast.makeText(context, "Animation to Sydney complete", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, "Animation to Sydney canceled", Toast.LENGTH_SHORT)
                        .show();
            }
        });*/
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

    private void UpdateCamera(CameraUpdate update, GoogleMap.CancelableCallback cancelableCallback) {
        if (!checkMapReady()) {
            return;
        }
        googleMap.animateCamera(update, duration, cancelableCallback);
    }

    /**
     * The amount by which to scroll the camera. Note that this amount is in raw pixels, not dp
     * (density-independent pixels).
     */
    private static final int duration = 10;
    private GoogleMap googleMap;
    private CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(-33.87365, 151.20689))
                                            .zoom(15.5f)
                                            .bearing(0)
                                            .tilt(25)
                                            .build();
    private View view;
    private Context context;
}
