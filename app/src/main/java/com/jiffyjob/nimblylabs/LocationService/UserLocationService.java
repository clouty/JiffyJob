package com.jiffyjob.nimblylabs.locationService;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import de.greenrobot.event.EventBus;

/**
 * Created by NimblyLabs on 25/3/2015.
 */
public class UserLocationService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, IUserLocationService {
    /**
     * Note: Service will post event
     * Retrieving user location through GoogleApiClient, create the class and call connectAPIClient()<br/>
     * To check on user location after application resumes call onResume()<br/>To stop service call onStop()
     *
     * @param context               application context (non nullable)
     * @param isListeningForUpdates continuously received updates (per 30 mins)
     * @throws InstantiationException throws exception if context is null.
     */
    public UserLocationService(Context context, boolean isListeningForUpdates) throws InstantiationException {
        if (context == null) {
            throw new InstantiationException("Context in UserLocationService cannot be null");
        }
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (isListeningForUpdates) {
            initListeners();
            this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
    }

    @Override
    public void connectAPIClient() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        checkPlayServices();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        if (locationListener != null) {
            this.locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public Location getLocation() {
        return mLastLocation;
    }

    //Google service API call back methods
    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            GeocoderService geocoderService = new GeocoderService(context);
            Address address = geocoderService.getAddress(mLastLocation.getLongitude(), mLastLocation.getLatitude());
            EventBus.getDefault().post(new LocationUpdatedEvent(address));
        } else {
            Toast.makeText(context, "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(context, "This device is not supported.", Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    private void initListeners() {
        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                boolean isBetter = isBetterLocation(location, mLastLocation);
                if (isBetter) {
                    mLastLocation = location;
                    GeocoderService geocoderService = new GeocoderService(context);
                    Address address = geocoderService.getAddress(mLastLocation.getLongitude(), mLastLocation.getLatitude());
                    EventBus.getDefault().post(new LocationUpdatedEvent(address));
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > THIRTY_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -THIRTY_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private LocationListener locationListener = null;
    private LocationManager locationManager;
    private String TAG = UserLocationService.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Context context;
    private Location mLastLocation;
    private static final int THIRTY_MINUTES = 1000 * 60 * 30;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
}
