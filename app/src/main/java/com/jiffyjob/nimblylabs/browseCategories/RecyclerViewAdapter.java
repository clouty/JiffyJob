package com.jiffyjob.nimblylabs.browseCategories;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browseCategories.Model.JobModel;
import com.jiffyjob.nimblylabs.browseIndividual.BrowseIndividualView;
import com.jiffyjob.nimblylabs.httpServices.FeatureJobService;
import com.jiffyjob.nimblylabs.main.JiffyJobMainActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by himur on 6/18/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> {

    public RecyclerViewAdapter() {
        super();
        this.itemList = new ArrayList<>();
    }

    public void setItems(@NonNull List<JobModel> itemList) {
        this.itemList = itemList;
        this.notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {
        View view;
        context = viewGroup.getContext();
        if (viewType == 0) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feature_job, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.browse_page_item_v4, viewGroup, false);
        }
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder viewHolder, int position) {
        if (position > 0) {
            SharedPreferences lastScrollPreference = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            lastScrollPreference.edit().putInt(BrowseCategories.LAST_SCROLL_POSITION, position - 1).apply();
        }

        if (position > 0) {
            initBrowseJobUI(viewHolder, position);
        } else {
            initFeatureJobUI(viewHolder);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void add(int position, JobModel model) {
        itemList.add(position, model);
        notifyItemInserted(position);
    }

    public void remove(JobModel model) {
        int position = itemList.indexOf(model);
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    private void initListener(final ItemViewHolder viewHolder, final JobModel model) {
        View view = viewHolder.itemView;
        CheckBox starredCB = (CheckBox) view.findViewById(R.id.starredCB);
        starredCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobModel model = itemList.get(viewHolder.position);
                model.IsStarred = ((CheckBox) v).isChecked();
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseIndividualView browseIndividualView = new BrowseIndividualView();
                browseIndividualView.setAttributes(model);
                FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag(JiffyJobMainActivity.FRAG_CONTAINER_TAG);
                if (!(fragment instanceof BrowseIndividualView)) {
                    fragmentManager.beginTransaction()
                            .addToBackStack(JiffyJobMainActivity.FRAG_CONTAINER_TAG)
                            .replace(R.id.fragment_container, browseIndividualView, JiffyJobMainActivity.FRAG_CONTAINER_TAG)
                            .commit();
                }
            }
        });
    }

    private void initBrowseJobUI(ItemViewHolder viewHolder, int position) {
        final JobModel model = itemList.get(position);
        viewHolder.position = position;
        viewHolder.companyNameTV.setText(model.CompanyName);

        initListener(viewHolder, model);
        try {
            String dateStr = model.DatePosted;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date postedDate = df.parse(dateStr);
            long postedTime = postedDate.getTime();
            long currentTIme = new Date().getTime();
            long diffTime = Math.abs(currentTIme - postedTime);
            long dayDiff = TimeUnit.MILLISECONDS.toDays(diffTime);
            if (dayDiff == 0) {
                viewHolder.postDateTV.setText("Posted today");
            } else if (dayDiff == 1) {
                viewHolder.postDateTV.setText("Posted yesterday");
            } else {
                viewHolder.postDateTV.setText(String.format("%s days ago", dayDiff));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (model.IsStarred) {
            viewHolder.starredCB.setChecked(true);
        } else {
            viewHolder.starredCB.setChecked(false);
        }

        viewHolder.jobTitle.setText(model.JobTitle);

        String imageUrl;
        if (model.IsGenericPhoto == 1) {
            imageUrl = context.getResources().getString(R.string.gPhotosUrl);
        } else {
            imageUrl = context.getResources().getString(R.string.uPhotosUrl);
        }

        String fullImageUrl = String.format("%s%s", imageUrl, model.Filename);
        Glide.with(context)
                .load(fullImageUrl)
                .fitCenter()
                .placeholder(R.drawable.image_placeholder)
                .crossFade()
                .into(viewHolder.jobIV);

        try {
            DateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault());
            Date startDate = df.parse(model.StartDateTime);
            Date endDate = df.parse(model.EndDateTime);

            DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String startDateStr = dateFormat.format(startDate);
            String endDateStr = dateFormat.format(endDate);
            String startEndDateStr = String.format("%s - %s", startDateStr, endDateStr);
            viewHolder.dateTV.setText(startEndDateStr);

            DateFormat timeFormat = new SimpleDateFormat("ha", Locale.getDefault());
            String startTimeStr = timeFormat.format(startDate);
            String endTimeStr = timeFormat.format(endDate);
            String startEndTimeStr = String.format("%s - %s", startTimeStr, endTimeStr);
            viewHolder.timeTV.setText(startEndTimeStr);

            String location = String.format("%s, %s", model.State, model.City);
            viewHolder.locationTV.setText(location);

            viewHolder.payoutTV.setText(String.format("$%s", model.Payout));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initFeatureJobUI(ItemViewHolder viewHolder) {
        ViewPager viewpager = viewHolder.featureJobVP;
        CircleIndicator circleIndicator = viewHolder.featureJobCI;
        fetchFeatureJobUrl();
        if (viewPagerAdapter == null) {
            viewPagerAdapter = new ViewPagerAdapter(context);
            viewPagerAdapter.setItems(featureUrlList);
            viewpager.setOffscreenPageLimit(0);
        }
        viewpager.setAdapter(viewPagerAdapter);

        circleIndicator.setViewPager(viewpager);
        viewPagerAdapter.notifyDataSetChanged();

        fetchFeatureJobs();
    }

    private void fetchFeatureJobUrl() {
        featureUrlList.clear();
        featureUrlList.add("http://www.nimblylabs.com/jjws/gphotos/generic_fnb6.jpg");
        featureUrlList.add("http://www.nimblylabs.com/jjws/gphotos/generic_fnb7.jpg");
        featureUrlList.add("http://www.nimblylabs.com/jjws/gphotos/generic_fnb8.jpg");
    }

    private void fetchFeatureJobs() {
        featureJobService = new FeatureJobService();
        /*featureJobService.subscribe(this);*/
    }

    private List<JobModel> itemList;
    private Context context;
    //Feature job
    private List<String> featureUrlList = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;
    private FeatureJobService featureJobService;

    public final static class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
            companyIV = (ImageView) itemView.findViewById(R.id.companyIV);
            jobIV = (ImageView) itemView.findViewById(R.id.jobIV);
            starredCB = (CheckBox) itemView.findViewById(R.id.starredCB);
            companyNameTV = (TextView) itemView.findViewById(R.id.companyNameTV);
            postDateTV = (TextView) itemView.findViewById(R.id.postDateTV);
            jobTitle = (TextView) itemView.findViewById(R.id.jobTitle);
            dateTV = (TextView) itemView.findViewById(R.id.dateTV);
            timeTV = (TextView) itemView.findViewById(R.id.timeTV);
            locationTV = (TextView) itemView.findViewById(R.id.locationTV);
            payoutTV = (TextView) itemView.findViewById(R.id.payoutTV);
            payoutTypeTV = (TextView) itemView.findViewById(R.id.payoutTypeTV);

            featureJobVP = (ViewPager) itemView.findViewById(R.id.featureJobVP);
            featureJobCI = (CircleIndicator) itemView.findViewById(R.id.featureJobCI);
        }

        private int position;
        private ImageView companyIV, jobIV;
        private CheckBox starredCB;
        private TextView companyNameTV, postDateTV, jobTitle, dateTV, timeTV, locationTV, payoutTV, payoutTypeTV;
        private ViewPager featureJobVP;
        private CircleIndicator featureJobCI;
    }
}

