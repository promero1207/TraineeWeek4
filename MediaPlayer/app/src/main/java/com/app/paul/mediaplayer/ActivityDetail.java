package com.app.paul.mediaplayer;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.app.paul.mediaplayer.MainActivity.BOOLEAN_STARTED;
import static com.app.paul.mediaplayer.MainActivity.PLAYING;
import static com.app.paul.mediaplayer.NotificationCreator.NOTIFY_PLAY;
import static com.app.paul.mediaplayer.ServiceMediaCreator.CURRENTLY_PLAYING;

/**
 * Activity to show detail of the radio information.
 */
public class ActivityDetail extends AppCompatActivity {

    //Boolean to check if media player is started.
    private Boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Getting extras coming from Intent
        Bundle myBundle = getIntent().getExtras();

        //boolean value from Intent
        started = myBundle.getBoolean(BOOLEAN_STARTED);

        //Local variables
        TextView readiopath = findViewById(R.id.detail_radiopath);
        readiopath.setText("http://us5.internet-radio.com:8110/listen.pls&t=.m3u");
        TextView title = findViewById(R.id.detail_main_title);
        title.setText("VCS");
        TextView body = findViewById(R.id.detail_description);
        body.setText(R.string.Radio_info);

        //Play and pause button
        final ImageButton play = findViewById(R.id.btn_detail_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityDetail.this, ServiceMediaCreator.class);
                if(started){
                    started = false;
                    //mediaPlayer.pause();
                    play.setImageResource(R.drawable.ic_play_dark);
                    intent.putExtra(NOTIFY_PLAY, "PAUSE");
                    sendMessage("PLAY");
                    startService(intent);
                }
                else{
                    started = true;
                    //mediaPlayer.start();
                    intent.putExtra(NOTIFY_PLAY, "PLAY");
                    sendMessage("PAUSE");
                    startService(intent);
                    play.setImageResource(R.drawable.ic_pause_dark);
                }
            }
        });

        //Logic for placing button image
        if(!started){
            play.setImageResource(R.drawable.ic_play_dark);
        }
        else {
            play.setImageResource(R.drawable.ic_pause_dark);
        }

    }

    /**
     * Method for sending broadcast message
     * @param state PLAY for playing state, PAUSE for PAUSE State
     */
    private void sendMessage(String state) {
        Intent intent = new Intent(PLAYING);
        // You can also include some extra data.
        intent.putExtra(CURRENTLY_PLAYING, state);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Saving activity state
     * @param outState Bundle to store data
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Intent intent = getIntent();
        intent.putExtra(BOOLEAN_STARTED, started);
    }


}
