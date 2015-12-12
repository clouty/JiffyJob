package com.jiffyjob.nimblylabs.locationService;

import android.location.Location;

/**
 * Created by NimblyLabs on 25/3/2015.
 */
public interface IUserLocationService {
    /**
     * Call this method to start retrieve user location
     */
    void connectAPIClient();
    /**
     * To be called activity onResume()
     */
    void onResume();

    /**
     * Stop listening for location, remove location services
     */
    void onStop();
    /**
     * Return current user location
     * @return
     */
    Location getLocation();


}
