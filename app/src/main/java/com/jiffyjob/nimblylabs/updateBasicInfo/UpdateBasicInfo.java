package com.jiffyjob.nimblylabs.updateBasicInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiffyjob.nimblylabs.app.R;

/**
 * Created by NimblyLabs on 1/6/2015.
 */
public class UpdateBasicInfo extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_updatebasicinfo, container, false);
        context = view.getContext();
        init();
        return view;
    }

    private void init() {


    }

    private View view;
    private Context context;
}
