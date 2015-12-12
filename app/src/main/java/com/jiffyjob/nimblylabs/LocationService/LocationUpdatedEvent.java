package com.jiffyjob.nimblylabs.locationService;

import android.location.Address;

/**
 * Created by NimblyLabs on 5/12/2015.
 */
public class LocationUpdatedEvent {
    public LocationUpdatedEvent(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    private Address address;
}
