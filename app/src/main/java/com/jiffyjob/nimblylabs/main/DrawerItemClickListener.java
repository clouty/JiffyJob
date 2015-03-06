package com.jiffyjob.nimblylabs.main;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by NimblyLabs on 25/2/2015.
 */
public class DrawerItemClickListener implements AdapterView.OnItemClickListener {

    public DrawerItemClickListener(Context context)
    {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)
        {
            TextView nickNameTextView = (TextView)view.findViewById(R.id.nickNameTextView);
            Toast.makeText(context, "Position:" + position + " Name:" + nickNameTextView.getText(), Toast.LENGTH_SHORT).show();
        }else{
            TextView drawer_itemName = (TextView)view.findViewById(R.id.drawer_itemName);
            Toast.makeText(context, "Position:" + position + " Name:" + drawer_itemName.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    private Context context;
}
