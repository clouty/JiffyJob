package com.jiffyjob.nimblylabs.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.customui.ImageHelper;

import java.util.List;

/**
 * Created by NimblyLabs on 20/1/2015.
 */
public class DrawerItemAdapter extends ArrayAdapter<DrawerItemObject> {

    private Context mContext;
    private int resourceId; //custom drawer item
    private List<DrawerItemObject> drawerItems;

    public DrawerItemAdapter(Context context, int resource, List<DrawerItemObject> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resourceId = resource;
        this.drawerItems = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(resourceId, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.drawer_icon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.drawer_itemName);

        DrawerItemObject drawItem = this.drawerItems.get(position);

        imageViewIcon.setImageResource(drawItem.icon);
        textViewName.setText(drawItem.name);

        LinearLayout drawerSelection = (LinearLayout) listItem.findViewById(R.id.drawerSelection);
        LinearLayout userInfoLayout = (LinearLayout) listItem.findViewById(R.id.userInfoLayout);
        if (position == 0) {
            drawerSelection.setVisibility(View.GONE);
            userInfoLayout.setVisibility(View.VISIBLE);
            CreateUserInfoView(listItem);
        } else {
            drawerSelection.setVisibility(View.VISIBLE);
            userInfoLayout.setVisibility(View.GONE);
        }

        return listItem;
    }

    private void CreateUserInfoView(View listItem) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.swagdog2);
        Bitmap convertedBitmap = ImageHelper.getRoundedCornerBitmap(bitmap, 100);
        ImageView userImage = (ImageView) listItem.findViewById(R.id.userImageView);
        userImage.setImageBitmap(convertedBitmap);
    }
}

