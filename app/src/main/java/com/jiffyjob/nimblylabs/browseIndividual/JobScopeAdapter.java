package com.jiffyjob.nimblylabs.browseIndividual;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;

import java.util.List;

/**
 * Created by himur on 1/6/2016.
 */
public class JobScopeAdapter extends ArrayAdapter<String> {
    public JobScopeAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.browse_individual_item, null);
        }
        TextView textview = (TextView) convertView.findViewById(R.id.text1);
        textview.setText(getItem(position));
        return convertView;
    }

    private Context context;
}
