package com.jiffyjob.nimblylabs.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browseCategories.BrowseCategories;

import java.util.ArrayList;

public class JiffyJobMainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiffy_job_main);

        init();
        populateDrawerItems();
        createFragment(savedInstanceState);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new DrawerItemAdapter(this, R.layout.custom_drawer_item, drawerItems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        BrowseCategories fragment = new BrowseCategories();
        fragment.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    private void init() {
        this.setTitle("JiffyJob");
        menuItems = getResources().getStringArray(R.array.nav_drawer_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
    }

    private void populateDrawerItems() {
        drawerItems = new ArrayList<DrawerItemObject>();

        drawerItems.add(new DrawerItemObject());//create userInfo
        drawerItems.add(new DrawerItemObject(R.drawable.xhdpi_search, "Browse jobs"));
        drawerItems.add(new DrawerItemObject(R.drawable.xhdpi_post, "Post a job"));
        drawerItems.add(new DrawerItemObject(R.drawable.xhdpi_manage, "Manage"));
        drawerItems.add(new DrawerItemObject(R.drawable.xhdpi_preferences, "Preferences"));
        drawerItems.add(new DrawerItemObject(R.drawable.xhdpi_settings, "App settings"));
        drawerItems.add(new DrawerItemObject(R.drawable.xhdpi_contact, "Contact us"));
    }

    //default fragment should be loaded in here
    private void createFragment(Bundle savedInstanceState) {
        if (findViewById(R.id.fragment_container) != null) {
            // restored from a previous state
            if (savedInstanceState != null) {
                return;
            }
            //create the default fragment
            BrowseCategories browseCategories = new BrowseCategories();
            browseCategories.setArguments(getIntent().getExtras());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_jiffy_job_main, menu);

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
