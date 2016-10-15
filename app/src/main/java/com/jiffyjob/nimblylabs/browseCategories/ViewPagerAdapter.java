package com.jiffyjob.nimblylabs.browseCategories;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jiffyjob.nimblylabs.app.R;

import java.util.List;


/**
 * Created by NimblyLabs on 10/8/2015.
 * Use to display FeatureJobs
 */
public class ViewPagerAdapter extends PagerAdapter {

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<String> urlList) {
        this.urlList = urlList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.browse_categories_item, container, false);
        ImageView featureItemIV = (ImageView) view.findViewById(R.id.featureItemIV);
        TextView featureTV = (TextView) view.findViewById(R.id.featureTV);
        Glide.with(context)
                .load(urlList.get(position))
                .fitCenter()
                .placeholder(R.drawable.image_placeholder)
                .crossFade()
                .into(featureItemIV);
        featureTV.setText("Test text view");
        container.addView(view);
        initListener(view, position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return urlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private void initListener(View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, String.format("FeatureJob %s", position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Context context;
    private List<String> urlList;
}
