package com.jiffyjob.nimblylabs.notification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by NielPC on 9/3/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.e(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.e(TAG, "FCM Data Message: " + remoteMessage.getData());

        JJNotificationManager jjNotificationManager = new JJNotificationManager(getApplicationContext());
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String msg = remoteMessage.getNotification().getBody();
            jjNotificationManager.smallNotification(title, msg);
        }

        //TODO: show notifcation from data content
    }

    private static String TAG = "MyFirebaseMessagingService";
}
