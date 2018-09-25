package com.app.paul.mediaplayer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static com.app.paul.mediaplayer.NotificationCreator.CHANNEL_ID;
import static com.app.paul.mediaplayer.NotificationCreator.NOTIFY_PLAY;
import static com.app.paul.mediaplayer.ServiceMediaCreator.CURRENTLY_PLAYING;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity {

    //Constants
    public static final String PLAYING =  "PLAYING";
    public static final String BOOLEAN_STARTED = "BOOLEAN_STARTED";

    //Fields
    FloatingActionButton mFloatingPlay;
    private Boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.activity_main);


        FloatingActionButton floattingInfo = findViewById(R.id.floatting_info);
        floattingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToDetail = new Intent(MainActivity.this, ActivityDetail.class);
                goToDetail.putExtra(BOOLEAN_STARTED, started);
                startActivity(goToDetail);
            }
        });

        //Floating Button Play/Pause
        mFloatingPlay = findViewById(R.id.floatting_play2);
        mFloatingPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServiceMediaCreator.class);
                if(started){
                    started = false;
                    mFloatingPlay.setImageResource(R.drawable.ic_play);
                    intent.putExtra(NOTIFY_PLAY, "PAUSE");
                    startService(intent);
                }
                else{
                    started = true;
                    intent.putExtra(NOTIFY_PLAY, "PLAY");
                    startService(intent);
                    mFloatingPlay.setImageResource(R.drawable.ic_pause);
                }
            }
        });


        //Getting bundle boolean
        if(savedInstanceState != null){
            started = savedInstanceState.getBoolean(BOOLEAN_STARTED);
        }

        //Image to use on Floating Button
        if(!started){
            mFloatingPlay.setImageResource(R.drawable.ic_play);
        }
        else {
            mFloatingPlay.setImageResource(R.drawable.ic_pause);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(PLAYING));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Unregister the receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

    }

    /**
     * Actions to do when app stops
     */
    @Override
    protected void onStop() {
        super.onStop();

        //Place the notification when App Stops
        NotificationCreator notificationCreator = new NotificationCreator();
        notificationCreator.notificationBar(this);
    }

    /**
     * Receiving broadcast from other activity to change image of buttons
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Bundle myBundle = intent.getExtras();
            if(myBundle != null) {
                String val = myBundle.getString(CURRENTLY_PLAYING);
                if(val != null) {
                    if (!val.equals("PLAY")) {
                        started = false;
                        mFloatingPlay.setImageResource(R.drawable.ic_play);
                    } else {
                        started = true;
                        mFloatingPlay.setImageResource(R.drawable.ic_pause);
                    }
                }
            }
        }
    };


    /**
     * Saving app state
     * @param outState Bundle to save data
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BOOLEAN_STARTED, started);
    }

    /**
     * Method for creating notification channel
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

}
