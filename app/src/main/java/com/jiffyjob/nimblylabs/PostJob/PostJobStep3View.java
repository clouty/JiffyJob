package com.jiffyjob.nimblylabs.PostJob;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.jiffyjob.nimblylabs.app.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by NimblyLabs on 26/3/2015.
 */
public class PostJobStep3View extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_job_step3, container, false);
        context = view.getContext();
        geocoder = new Geocoder(this.context, Locale.ENGLISH);
        init();
        return view;
    }

    private void init() {
        likeImage = (ImageView) view.findViewById(R.id.searchBtn);
        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_location);

        likeImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                doSearch(autoCompleteTextView.getText().toString());
                return true;
            }
        });
    }

    private void doSearch(String query) {
        try {
            List<Address> locationList = geocoder.getFromLocationName(query, maxResults);
            if (locationList == null || locationList.isEmpty()) {
                Toast.makeText(context, "Location not found.", Toast.LENGTH_LONG).show();
            } else {
                locationNameList.clear();
                for (Address address : locationList) {
                    String locationAddress = address.getCountryName() + " " + address.getFeatureName() + " " + address.getAdminArea();
                    locationNameList.add(locationAddress);
                }
                adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, locationNameList);
                autoCompleteTextView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                likeImage.setSelected(true);
                autoCompleteTextView.setSelected(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView autoCompleteTextView;
    private ImageView likeImage;
    private View view;
    private Context context;
    private Geocoder geocoder;
    private final static int maxResults = 5;
    private List<String> locationNameList = new ArrayList<String>();
}
