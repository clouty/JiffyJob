package com.jiffyjob.nimblylabs.notification;

/**
 * Created by himur on 3/12/2016.
 */
public class Config {


    /**
     * Flag to identify whether to show single line
     * or multi line text in push notification tray
     */
    public static boolean appendNotificationMessages = true;

    /**
     * Global topic to receive app wide push notifications
     */
    public static final String TOPIC_GLOBAL = "global";

    // type of push messages
    public static final int PUSH_TYPE_NEW_JOB = 1;
    public static final int PUSH_TYPE_JOB_APPLICATION_STATUS = 2;
    public static final int PUSH_TYPE_JOB_NOTICE_UPDATED = 3;

    // id to handle the notification in the notification try
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
}
