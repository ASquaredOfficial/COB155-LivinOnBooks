package com.example.myapplayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder notiBuilder = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, notiBuilder.build());
    }
}
