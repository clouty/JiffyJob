package com.jiffyjob.nimblylabs.main;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.jiffyjob.nimblylabs.browseCategories.BrowseCategories;

import java.util.ArrayList;

public class JiffyJobMainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiffy_job_main);

        Init();
        PopulateDrawerItems();
        // Set the adapter for the list view
        mDrawerList.setAdapter(new DrawerItemAdapter(this, R.layout.custom_drawer_item, drawerItems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(this));

        CreateFragment(savedInstanceState);
    }

    private void Init() {
        this.setTitle("JiffyJob");
        menuItems = getResources().getStringArray(R.array.nav_drawer_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
    }

    private void PopulateDrawerItems() {
        drawerItems = new ArrayList<DrawerItemObject>();

        drawerItems.add(new DrawerItemObject());//create userInfo
        drawerItems.add(new DrawerItemObject(R.drawable.find_icon, "Browse jobs"));
        drawerItems.add(new DrawerItemObject(R.drawable.find_icon, "Post a job"));
        drawerItems.add(new DrawerItemObject(R.drawable.find_icon, "Manage"));
        drawerItems.add(new DrawerItemObject(R.drawable.find_icon, "Discovery preferences"));
        drawerItems.add(new DrawerItemObject(R.drawable.find_icon, "App settings"));
        drawerItems.add(new DrawerItemObject(R.drawable.find_icon, "Contact us"));
    }

    //default fragment should be loaded in here
    private void CreateFragment(Bundle savedInstanceState) {
        if (findViewById(R.id.fragment_container) != null) {
            // restored from a previous state
            if (savedInstanceState != null) {
                return;
            }
            BrowseCategories browseCategories = new BrowseCategories();
            browseCategories.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, browseCategories).commit();

        }
    }

    //update other fragment and set title
    private void updateFragment(android.support.v4.app.Fragment fragment, String title) {
        this.setTitle(title);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jiffy_job_main, menu);
        //test
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<DrawerItemObject> drawerItems;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] menuItems;
}
