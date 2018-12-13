package com.robot.mr.makanduluyuk;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Notification {
    public void notificationCall(){
        Intent intent = new Intent(this, InputKegiatan.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle("Hallo, im notification")
                .setContentText("Click Me!")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_person_outline_black_24dp)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent);

      //  NotificationManager notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
      //  notificationManager.notify(1, builder.build());
    }
}
