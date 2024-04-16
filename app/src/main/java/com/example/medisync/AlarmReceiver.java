package com.example.medisync;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MedMinder", "Alarm received. Showing notification...");

        // Create notification channel (required for Android Oreo and higher)
        createNotificationChannel(context);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "med_minder_channel")
                .setSmallIcon(R.drawable.caduceus_symbol)
                .setContentTitle("Medication Reminder")
                .setContentText("Don't forget to take your medication!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create an explicit intent for the MainActivity
        Intent mainIntent = new Intent(context, MedMinder.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE); // Specify FLAG_IMMUTABLE

        builder.setContentIntent(pendingIntent);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel(Context context) {
        // Create notification channel only if the device is running Android Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Med Minder Channel";
            String description = "Channel for medication reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("med_minder_channel", name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
