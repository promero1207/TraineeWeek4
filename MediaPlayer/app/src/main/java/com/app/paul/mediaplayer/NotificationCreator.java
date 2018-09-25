package com.app.paul.mediaplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import static android.provider.Settings.System.getString;

/**
 * Class creator of media player notification
 */
public class NotificationCreator {

    static final String CHANNEL_ID = "CHANNEL_1";
    public static final String NOTIFY_PLAY = "com.app.paul.mediaplayer.PLAY";

    public void notificationBar(Context context){

        //Intent for the hole notification
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //Intent for the play button
        Intent play = new Intent(context,ServiceMediaCreator.class);
        play.putExtra(NOTIFY_PLAY,"PLAY");
        PendingIntent intentPlay = PendingIntent.getService(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);

        //Intent for the pause button
        Intent pause = new Intent(context,ServiceMediaCreator.class);
        pause.putExtra(NOTIFY_PLAY,"PAUSE");
        PendingIntent intentPause = PendingIntent.getService(context, 1, pause, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews customView = new RemoteViews(context.getPackageName(), R.layout.view_notification);
        customView.setImageViewResource(R.id.img_player,R.drawable.radio);


        //Building the notification
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_play)
                .setSubText("Streaming")
                .setContentTitle("CVS")
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle())
                .setAutoCancel(true)
                .addAction(R.drawable.ic_play, "PLAY", intentPlay)
                .addAction(R.drawable.ic_pause, "PAUSE", intentPause)
                .setContentIntent(pendingIntent);
        Notification builderNc = notification.build();

        NotificationManagerCompat.from(context).notify(1, builderNc);

    }






}
