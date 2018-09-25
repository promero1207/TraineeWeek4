package com.app.paul.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

import static com.app.paul.mediaplayer.MainActivity.PLAYING;
import static com.app.paul.mediaplayer.NotificationCreator.NOTIFY_PLAY;

/**
 * Service for creating Media Player
 */
public class ServiceMediaCreator extends Service {

    MediaPlayer mediaPlayer;
    String stream = "http://us5.internet-radio.com:8110/listen.pls&t=.m3u";
    public static final String CURRENTLY_PLAYING = "CURRENTLY_PLAYING";



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Media player is created and prepared
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(stream);
            mediaPlayer.prepare();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Media player is released
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    /**
     * When service is started check for play or pause the streaming
     * @param intent the intent comming from the caller
     * @param flags flags
     * @param startId id
     * @return int
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle myBundle = intent.getExtras();
        if(myBundle != null) {
            String command = myBundle.getString(NOTIFY_PLAY);
            if(command != null) {
                if (command.equals("PLAY")) {
                    mediaPlayer.start();
                    sendMessage("PLAY");
                } else {
                    mediaPlayer.pause();
                    sendMessage("PAUSE");
                }

            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Message to be sent in broadcast PLAY for playing PAUSE for pause
     * @param state String state PLAY/PAUSE
     */
    private void sendMessage(String state) {
        Intent intent = new Intent(PLAYING);
        // You can also include some extra data.
        intent.putExtra(CURRENTLY_PLAYING, state);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
