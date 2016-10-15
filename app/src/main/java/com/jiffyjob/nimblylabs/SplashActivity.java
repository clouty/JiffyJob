package com.jiffyjob.nimblylabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jiffyjob.nimblylabs.app.R;

/**
 * Created by NielPC on 9/28/2016.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getApplicationContext(), BeforeLoginActivityV2.class);
        startActivity(intent);
        finish();
    }
}
