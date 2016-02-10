package com.jiffyjob.nimblylabs.browsePage;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browsePage.event.BrowseIndividualViewEvent;
import com.jiffyjob.nimblylabs.xmlHelper.SimpleXMLReader;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * Created by NimblyLabs on 15/3/2015.
 */
public class BrowsePageAdapter extends ArrayAdapter<BrowsePageModel> {
    public BrowsePageAdapter(Context context, int view, ArrayList<BrowsePageModel> browsePageModels) {
        super(context, view, browsePageModels);
        this.context = context;
        this.viewID = view;
        readLinkXML();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BrowsePageModel model = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(viewID, null);
        }
        if (model.isPostBoosted()) {
            getBoostView(convertView, model);
        } else {
            getNonBoostView(convertView, model);
        }
        return convertView;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    private void getNonBoostView(View convertView, BrowsePageModel model) {
        TextView salaryTV = (TextView) convertView.findViewById(R.id.salaryTV);
        TextView mainContentTV = (TextView) convertView.findViewById(R.id.mainContentTV);
        TextView subContentTV = (TextView) convertView.findViewById(R.id.subContentTV);
        TextView companyNameTV = (TextView) convertView.findViewById(R.id.companyNameTV);
        companyNameTV.setTextColor(context.getResources().getColor(R.color.abc_primary_text_material_light));

        ImageView backgroundIV = (ImageView) convertView.findViewById(R.id.backgroundIV);
        ImageView darkbackgroundFillIV = (ImageView) convertView.findViewById(R.id.darkBackgroundFillIV);
        ImageView fillImageIV = (ImageView) convertView.findViewById(R.id.fillImage);

        Glide.with(fragment)
                .load(linkURI)
                .placeholder(R.drawable.image_placeholder)
                .crossFade()
                .into(fillImageIV);

        if (model.getPayout() > 0) {
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String payout = decimalFormat.format(model.getPayout());
            String salaryType = "";
            if (model.isSalaryDaily()) {
                salaryType = "Daily";
            } else {
                salaryType = "Hr";
            }
            salaryTV.setText(String.format("$%s/%s", payout, salaryType));
        } else {
            salaryTV.setText("Volunteer");
        }

        mainContentTV.setText(model.getJobTitle());
        mainContentTV.setTextColor(context.getResources().getColor(R.color.abc_primary_text_material_light));

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        DateFormat timeFormat = new SimpleDateFormat("ha");
        String startDate = dateFormat.format(model.getStartDate());
        String endDate = dateFormat.format(model.getEndDate());
        String startTime = timeFormat.format(model.getStartDate());
        String endTime = timeFormat.format(model.getEndDate());
        String location = model.getCity();
        String subContent = String.format("%s - %s \n%s - %s @ %s", startDate, endDate, startTime, endTime, location);
        subContentTV.setText(subContent);
        subContentTV.setTextColor(context.getResources().getColor(R.color.abc_primary_text_material_light));

        if (model.getCompanyName() != null && !model.getCompanyName().equalsIgnoreCase("null")) {
            companyNameTV.setText(model.getCompanyName());
        } else {
            String name = String.format(model.getFirstName(), model.getLastName());
            companyNameTV.setText(name);
        }
        darkbackgroundFillIV.setBackgroundColor(context.getResources().getColor(R.color.hashtag_background));
        darkbackgroundFillIV.setVisibility(View.VISIBLE);
        backgroundIV.setVisibility(View.GONE);

        initListeners(convertView, model);
        //todo: add fill image
    }

    private void getBoostView(View convertView, BrowsePageModel model) {
        TextView salaryTV = (TextView) convertView.findViewById(R.id.salaryTV);
        TextView mainContentTV = (TextView) convertView.findViewById(R.id.mainContentTV);
        TextView subContentTV = (TextView) convertView.findViewById(R.id.subContentTV);
        TextView companyNameTV = (TextView) convertView.findViewById(R.id.companyNameTV);

        ImageView backgroundIV = (ImageView) convertView.findViewById(R.id.backgroundIV);
        ImageView darkbackgroundFillIV = (ImageView) convertView.findViewById(R.id.darkBackgroundFillIV);
        ImageView fillImage = (ImageView) convertView.findViewById(R.id.fillImage);

        fillImage.setVisibility(View.GONE);

        linkURI = linkURI.replace("1", "2");
        Glide.with(fragment)
                .load(linkURI)
                .placeholder(R.drawable.image_placeholder)
                .crossFade()
                .into(backgroundIV);

        if (model.getPayout() > 0) {
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String payout = decimalFormat.format(model.getPayout());
            String salaryType = "";
            if (model.isSalaryDaily()) {
                salaryType = "Daily";
            } else {
                salaryType = "Hr";
            }
            salaryTV.setText(String.format("%s$%s/%s", model.getCurrencyCode(), payout, salaryType));
        } else {
            salaryTV.setText("Volunteer");
        }

        mainContentTV.setText(model.getJobTitle());
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        DateFormat timeFormat = new SimpleDateFormat("ha");
        String startDate = dateFormat.format(model.getStartDate());
        String endDate = dateFormat.format(model.getEndDate());
        String startTime = timeFormat.format(model.getStartDate());
        String endTime = timeFormat.format(model.getEndDate());
        String location = model.getCity();
        String subContent = String.format("%s - %s \n%s - %s @ %s", startDate, endDate, startTime, endTime, location);
        subContentTV.setText(subContent);

        if (model.getCompanyName() != null && !model.getCompanyName().equalsIgnoreCase("null")) {
            companyNameTV.setText(model.getCompanyName());
        } else {
            String name = String.format(model.getFirstName(), model.getLastName());
            companyNameTV.setText(name);
        }
        initListeners(convertView, model);
    }

    private void initListeners(View convertView, final BrowsePageModel model) {
        ImageButton favoriteBtn = (ImageButton) convertView.findViewById(R.id.favoriteBtn);
        ImageButton shareBtn = (ImageButton) convertView.findViewById(R.id.shareBtn);
        ImageButton applyBtn = (ImageButton) convertView.findViewById(R.id.applyBtn);

        if (!model.isPostBoosted()) {
            favoriteBtn.setBackgroundResource(R.drawable.btn_state_favorite_dark);
            shareBtn.setBackgroundResource(R.drawable.btn_state_share_dark);
            applyBtn.setBackgroundResource(R.drawable.btn_state_addperson_dark);
        }
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "favoriteBtn", Toast.LENGTH_SHORT).show();
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BrowseIndividualViewEvent(model));
            }
        });
    }

    private void readLinkXML() {
        SimpleXMLReader simpleXMLReader = new SimpleXMLReader(context, R.raw.links);
        List<String> listLink = simpleXMLReader.parseXML("gPhotos");
        if (listLink.size() > 0) {
            linkURI = listLink.get(0);
        }
        linkURI += "fnb/generic_fnb1.jpg";
    }

    private Fragment fragment;
    private String linkURI = null;
    private int viewID;
    private Context context;
}
