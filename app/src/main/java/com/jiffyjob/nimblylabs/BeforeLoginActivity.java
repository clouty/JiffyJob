package com.jiffyjob.nimblylabs;

import android.app.Activity;
import android.os.Bundle;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.joinUsFragmentView.JoinUsFragmentView;

/**
 * Created by NimblyLabs on 13/6/2015.
 */
public class BeforeLoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.before_login_activity);

    }

    @Override
    protected void onStart() {
        super.onStart();
        JoinUsFragmentView joinUsFragmentView = JoinUsFragmentView.getInstance();
        getFragmentManager().beginTransaction().add(R.id.fragment_container, joinUsFragmentView).commit();
    }
}
