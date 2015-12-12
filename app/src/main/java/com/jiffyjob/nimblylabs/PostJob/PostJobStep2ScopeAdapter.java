package com.jiffyjob.nimblylabs.postJob;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.postJob.postJobEvents.ScopeItemEvent;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by NimblyLabs on 22/7/2015.
 */
public class PostJobStep2ScopeAdapter extends ArrayAdapter<String> {
    public PostJobStep2ScopeAdapter(Context context, int view, List<String> objects) {
        super(context, view, objects);
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String item = getItem(position);

        convertView = inflater.inflate(R.layout.fragment_post_job_step2_item, null);
        TextView itemTV = (TextView) convertView.findViewById(R.id.itemTV);
        ImageView crossImageView = (ImageView) convertView.findViewById(R.id.crossBtn);
        crossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ScopeItemEvent(position));
            }
        });
        itemTV.setText(item);
        convertView.setTag(position);
        return convertView;
    }

    private Context context;
}
