package dev.ddzmitry.studenttracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;
import static dev.ddzmitry.studenttracker.utilities.Constans.INTENT_LOGGER;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_ALARM_ID;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_ALERT;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_CHANNEL;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_DATE;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_OBJECT;
import static dev.ddzmitry.studenttracker.utilities.Constans.NOTIFICATION_OBJECT_ID;

public class MessageReciever extends BroadcastReceiver {

    static int notificationID;
    String channel_id="test";
    @Override
    public void onReceive(Context context, Intent intent) {
        // Intent should have values for
        // Name, Type ,title
        /*
        *   NOTIFICATION_OBJECT = "notification_object";
            NOTIFICATION_OBJECT_ID = "notification_object_id";
            NOTIFICATION_DATE = "notification_date";
            NOTIFICATION_ALERT = "notification_alert";
            NOTIFICATION_ALARM_ID = "notification_alarm_id";
        * */
        String type = intent.getStringExtra(NOTIFICATION_OBJECT);
        Integer id = intent.getIntExtra(NOTIFICATION_OBJECT_ID,0);
        String date = intent.getStringExtra(NOTIFICATION_DATE);
        String alert_end_start = intent.getStringExtra(NOTIFICATION_ALERT);
        int alarmId =  intent.getIntExtra(NOTIFICATION_ALARM_ID,0);

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Log.e(INTENT_LOGGER, key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
            }
        }

        String message = String.format("Your %s: ID %s is %s on %s",type,id,alert_end_start,date);
        System.out.println(message);

        Toast.makeText(context,"Notification from " + NOTIFICATION_CHANNEL,Toast.LENGTH_LONG).show();
        createNotificationChannel(context,NOTIFICATION_CHANNEL);
        Notification notification= new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentText(message)
                .setContentTitle(String.format("%s notification",type)).build();
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(alarmId,notification);

    }
    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getResources().getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
