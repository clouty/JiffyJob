package com.jiffyjob.nimblylabs.browseCategories;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jiffyjob.nimblylabs.app.R;

/**
 * Created by NimblyLabs on 10/8/2015.
 */
public class FeaturedJobFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_browse_categories_item, container, false);
        context = view.getContext();
        init();
        return view;
    }

    public static FeaturedJobFragment getInstance() {
        featuredJobFragment = new FeaturedJobFragment();
        return featuredJobFragment;
    }

    public void setUrl(String url) {
        this.featureUrlStr = url;
    }

    private void init() {
        /*if (featureUrlStr == null) return;
        featureJobImage = (ImageView) view.findViewById(R.id.featureJobImage);
        Picasso.with(context).load(featureUrlStr).into(featureJobImage);*/
    }

    private static FeaturedJobFragment featuredJobFragment = null;
    private String featureUrlStr = null;
    private ImageView featureJobImage;
    private View view;
    private Context context;
}
