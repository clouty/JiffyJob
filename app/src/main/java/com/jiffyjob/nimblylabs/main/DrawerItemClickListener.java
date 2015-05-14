package com.jiffyjob.nimblylabs.main;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browsepage.BrowsePageView;


/**
 * Created by NimblyLabs on 25/2/2015.
 */
public class DrawerItemClickListener implements AdapterView.OnItemClickListener {

    public DrawerItemClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {
            TextView nickNameTextView = (TextView) view.findViewById(R.id.nickNameTextView);
            Toast.makeText(context, "Position:" + position + " Name:" + nickNameTextView.getText(), Toast.LENGTH_SHORT).show();
        } else {
            switch (position){
                case 1: CreateBrowsePagViewFragment(view, position);
                    break;
                default: break;
            }
        }
    }

    //methods to create all fragments
    private void CreateBrowsePagViewFragment(View view, int position){
        TextView drawer_itemName = (TextView) view.findViewById(R.id.drawer_itemName);
        Toast.makeText(context, "Position:" + position + " Name:" + drawer_itemName.getText(), Toast.LENGTH_SHORT).show();

        BrowsePageView browsePageView = new BrowsePageView();
        browsePageView.setArguments(((Activity) context).getIntent().getExtras());
        updateFragment(browsePageView, "Browse page");
    }

    //update other fragment and set title
    private void updateFragment(android.support.v4.app.Fragment fragment, String title) {
        ((Activity) context).setTitle(title);

        FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    private Context context;
}
