package com.jiffyjob.nimblylabs.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browsePage.BrowsePageView;
import com.jiffyjob.nimblylabs.postJob.PostJobHostView;
import com.jiffyjob.nimblylabs.updateBasicInfo.UpdateBasicInfo;


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
            createUpdateBasicInfo(view, position);
        } else {
            switch (position) {
                case 1:
                    createBrowsePagViewFragment(view, position);
                    break;
                case 2:
                    createPostJobViewFragment(view, position);
                    break;
                default:
                    break;
            }
        }
    }

    //methods to create all fragments
    private void createBrowsePagViewFragment(View view, int position) {
        TextView drawer_itemName = (TextView) view.findViewById(R.id.drawer_itemName);
        Toast.makeText(context, "Position:" + position + " Name:" + drawer_itemName.getText(), Toast.LENGTH_SHORT).show();

        BrowsePageView browsePageView = new BrowsePageView();
        browsePageView.setArguments(((Activity) context).getIntent().getExtras());
        updateFragment(browsePageView, "Browse page");
    }

    private void createUpdateBasicInfo(View view, int position) {
        TextView drawer_itemName = (TextView) view.findViewById(R.id.drawer_itemName);
        Toast.makeText(context, "Position:" + position + " Name:" + drawer_itemName.getText(), Toast.LENGTH_SHORT).show();

        UpdateBasicInfo updateBasicInfo = new UpdateBasicInfo();
        updateBasicInfo.setArguments(((Activity) context).getIntent().getExtras());
        updateFragment(updateBasicInfo, "User info");
    }

    private void createPostJobViewFragment(View view, int position) {
        PostJobHostView postJobHostView = new PostJobHostView();
        postJobHostView.setArguments(((Activity) context).getIntent().getExtras());
        updateFragment(postJobHostView, "Post a job");
    }

    //update other fragment and set title
    private void updateFragment(Fragment fragment, String title) {
        ((Activity) context).setTitle(title);

        FragmentTransaction transaction = ((Activity) context).getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    private Context context;
}
