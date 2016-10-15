package com.jiffyjob.nimblylabs.updateBasicInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by himur on 4/24/2016.
 */
public class ProfileAdapter extends ArrayAdapter<ProfileDetailModel> {
    public ProfileAdapter(Context context, int resource, List<ProfileDetailModel> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_item, null);
        }
        ProfileDetailModel model = getItem(position);
        TextView nameTV = (TextView) convertView.findViewById(R.id.titleTV);
        TextView contentTV = (TextView) convertView.findViewById(R.id.contentTV);
        nameTV.setText(model.getTitle());
        contentTV.setText(model.getContent());
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
