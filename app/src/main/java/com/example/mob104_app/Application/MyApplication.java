package com.example.mob104_app.Application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplication extends Application {

    public static final String CHANNEL_ID = "push_notification_id";

    @Override
    public void onCreate() {
        super.onCreate();
       createChannelID();
    }

    private void createChannelID() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"pushNofication",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }
}
