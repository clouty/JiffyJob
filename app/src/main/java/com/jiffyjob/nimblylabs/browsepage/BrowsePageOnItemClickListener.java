package com.jiffyjob.nimblylabs.browsepage;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;

import com.jiffyjob.nimblylabs.browseIndividual.BrowseIndividualView;
import com.jiffyjob.nimblylabs.app.R;

import java.util.ArrayList;

/**
 * Created by NimblyLabs on 22/3/2015.
 */
public class BrowsePageOnItemClickListener implements AdapterView.OnItemClickListener {
    public BrowsePageOnItemClickListener(Context context, ArrayList<BrowsePageModel> browsePageModelList) {
        if (browsePageModelList == null) {
            throw new IllegalArgumentException("browsePageModelList cannot be null");
        }
        this.context = context;
        this.browsePageModelList = browsePageModelList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO: get item from browsePageModelList, and call fragment for display
        context.getApplicationContext();
        BrowseIndividualView browseIndividualView = new BrowseIndividualView();
        browseIndividualView.setArguments(((Activity) context).getIntent().getExtras());
        updateFragment(browseIndividualView, "Browse Individual View");
    }

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

    private ArrayList<BrowsePageModel> browsePageModelList = null;
    private Context context;
}
