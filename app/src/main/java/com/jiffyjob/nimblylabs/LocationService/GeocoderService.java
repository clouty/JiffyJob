package com.jiffyjob.nimblylabs.locationService;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

/**
 * Created by NimblyLabs on 5/12/2015.
 */
public class GeocoderService {
    public GeocoderService(Context context) {
        this.context = context;
    }

    public Address getAddress(double longitude, double latitude) {
        Address address = null;
        try {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addressList = gcd.getFromLocation(latitude, longitude, 1);
            if (addressList.size() > 0) {
                address = addressList.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public TreeMap getAddressMap(Address address) {
        if (address == null) return null;
        TreeMap<String, String> treeMap = new TreeMap<>();
        String country = address.getCountryName();
        String state = address.getAdminArea();
        String city = address.getLocality();
        int addressLineIndex = address.getMaxAddressLineIndex();
        if (addressLineIndex > 0) {
            if (country == null || country.equalsIgnoreCase("")) {
                return null;
            }
            if (state == null || state.equalsIgnoreCase("")) {
                state = address.getThoroughfare();
                if (state == null || state.equalsIgnoreCase("")) {
                    state = country;
                }
            }
            if (city == null || city.equalsIgnoreCase("")) {
                city = address.getSubThoroughfare();
                if (city == null || state.equalsIgnoreCase("")) {
                    city = country;
                }
            }
            treeMap.put("Country", country);
            treeMap.put("State", state);
            treeMap.put("City", city);
            treeMap.put("Longitude", String.valueOf(address.getLongitude()));
            treeMap.put("Latitude", String.valueOf(address.getLatitude()));
            return treeMap;
        } else {
            return null;
        }
    }

    public String getAddressString(Address address) {
        String addressStr = "";
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            addressStr += address.getAddressLine(i) + "\n";
        }
        return addressStr;
    }

    private Context context;
}
