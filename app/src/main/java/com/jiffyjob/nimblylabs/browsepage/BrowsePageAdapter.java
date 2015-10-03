package com.jiffyjob.nimblylabs.browsePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by NimblyLabs on 15/3/2015.
 */
public class BrowsePageAdapter extends ArrayAdapter<BrowsePageModel> {
    public BrowsePageAdapter(Context context, int view, ArrayList<BrowsePageModel> browsePageModels) {
        super(context, view, browsePageModels);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        BrowsePageModel model = getItem(position);

        convertView = inflater.inflate(R.layout.browse_page_item_v2, null);
        ((TextView) convertView.findViewById(R.id.userNameTV)).setText(model.getUserName());
        ((TextView) convertView.findViewById(R.id.companyNameTV)).setText(model.getCompanyName());
        ((TextView) convertView.findViewById(R.id.titleTV)).setText(model.getMessage());
        ((ImageView) convertView.findViewById(R.id.userImageView)).setImageBitmap(model.getProfileImage());
        RatingBar ratingBar = ((RatingBar) convertView.findViewById(R.id.jobRating));
        ratingBar.setMax(5);
        String ratingStr = model.getJobRating().replace("%", "");
        float rating = Float.parseFloat(ratingStr);
        ratingBar.setRating(rating / 20);

        if (model.getStartEndTime().length == 2) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String startTime = sdf.format(model.getStartEndTime()[0].getTime());
            String endTime = sdf.format(model.getStartEndTime()[1].getTime());
            String startEndTime = String.format("%s - %s", startTime, endTime);
            ((TextView) convertView.findViewById(R.id.time)).setText(startEndTime);
        }

        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MMM");
        String formattedDate = myFormat.format(model.getDate());
        ((TextView) convertView.findViewById(R.id.date)).setText(formattedDate);

        ((TextView) convertView.findViewById(R.id.location)).setText(model.getLocation());
        convertView.setTag(position);
        return convertView;
    }

    private Context context;
}
