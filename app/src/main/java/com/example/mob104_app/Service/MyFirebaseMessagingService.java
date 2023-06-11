package com.example.mob104_app.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.mob104_app.Activities.BillActivity;
import com.example.mob104_app.Application.MyApplication;
import com.example.mob104_app.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = MyFirebaseMessagingService.class.getName();
    public static final String  TOKEN= "TOKEN";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Map<String,String> stringMap = message.getData();
        String title = stringMap.get("tilte");
        String body = stringMap.get("body");
        String status = stringMap.get("status");
        sendNotification(title,body,(status!=null)?Integer.parseInt(status):0);
    }

    private void sendNotification(String title, String body, int status) {
        Intent intent = new Intent(this, BillActivity.class);
        intent.putExtra("status",status);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.shopping)
                .setTimeoutAfter(15000)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(1, builder.build());
        }
    }


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }


}
