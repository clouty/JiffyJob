package com.jiffyjob.nimblylabs.browsePage;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.xmlHelper.SimpleXMLReader;

import java.util.List;

/**
 * Created by himur on 2/11/2016.
 */
public class BrowsePageAdapterV2 extends ArrayAdapter<BrowsePageModel> {

    public BrowsePageAdapterV2(Context context, int resource, List<BrowsePageModel> objects) {
        super(context, resource, objects);
        this.resource = resource;
        readLinkXML();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, null);
        }
        initListener(convertView, position);

 /*       ImageView backgroundIV = (ImageView) convertView.findViewById(R.id.backgroundIV);
        ImageView posterImageIV = (ImageView) convertView.findViewById(R.id.posterImageIV);
        TextView posterNameTV = (TextView) convertView.findViewById(R.id.posterNameTV);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        TextView jobTitleTV = (TextView) convertView.findViewById(R.id.jobTitleTV);

        TextView dateTV = (TextView) convertView.findViewById(R.id.dateTV);
        TextView timeTV = (TextView) convertView.findViewById(R.id.timeTV);
        TextView locationTV = (TextView) convertView.findViewById(R.id.locationTV);
        TextView payTV = (TextView) convertView.findViewById(R.id.payTV);
        TextView paymentTV = (TextView) convertView.findViewById(R.id.paymentTV);
        TextView currencyTV = (TextView) convertView.findViewById(R.id.currencyTV);
        Glide.with(fragment)
                .load(linkURI)
                .placeholder(R.drawable.image_placeholder)
                .fitCenter()
                .into(backgroundIV);

        BrowsePageModel model = getItem(position);
        String posterName = String.format("%s %s", model.getFirstName(), model.getLastName());
        posterNameTV.setText(posterName);
        //TODO: no rating for now
        //ratingBar.setRating();
        jobTitleTV.setText(model.getJobTitle());

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        DateFormat timeFormat = new SimpleDateFormat("ha");
        String startDate = dateFormat.format(model.getStartDate());
        String endDate = dateFormat.format(model.getEndDate());
        String startTime = timeFormat.format(model.getStartDate());
        String endTime = timeFormat.format(model.getEndDate());
        String startEndDateStr = String.format("%s - %s", startDate, endDate);
        dateTV.setText(startEndDateStr);
        String startEndTimeStr = String.format("%s - %s", startTime, endTime);
        timeTV.setText(startEndTimeStr);
        locationTV.setText(model.getCity());

        final int payout = Double.valueOf(model.getPayout()).intValue();
        if (payout == 0) {
            payTV.setText("Volunteer");
            paymentTV.setText("");
            currencyTV.setText("");
        } else {
            payTV.setText(String.format("$%s", model.getPayout()));
            if (model.isSalaryDaily()) {
                paymentTV.setText("/Daily");
            } else {
                paymentTV.setText("/HR");
            }
            currencyTV.setText(model.getCurrencyCode());
        }*/
        return convertView;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    private void initListener(View convertView, int position) {
        final BrowsePageModel model = getItem(position);
      /*  RelativeLayout payoutLayout = (RelativeLayout) convertView.findViewById(R.id.payoutLayout);
        ImageButton favoriteBtn = (ImageButton) convertView.findViewById(R.id.favoriteBtn);
        ImageButton shareBtn = (ImageButton) convertView.findViewById(R.id.shareBtn);
        payoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BrowseIndividualViewEvent(model));
            }
        });
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFunction();
            }
        });*/
    }

    private void shareFunction() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Share content URL";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void readLinkXML() {
        SimpleXMLReader simpleXMLReader = new SimpleXMLReader(getContext(), R.raw.links);
        List<String> listLink = simpleXMLReader.parseXML("gPhotos");
        if (listLink.size() > 0) {
            linkURI = listLink.get(0);
        }
        linkURI += "fnb/generic_fnb1.jpg";
    }

    private Fragment fragment;
    private int resource;

    //test use
    private String linkURI = null;
}
