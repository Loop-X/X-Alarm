package io.github.loopX.XAlarm.module.Alarm;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * Wrapper of the system media player.
 * It is called by AlarmRingingController.
 */
public class AlarmRingtonePlayer {

    private MediaPlayer mPlayer;
    private Context mContext;

    public AlarmRingtonePlayer(Context context) {
        mContext = context;
        mPlayer = new MediaPlayer();
    }

    public void play(Uri uri) {
        if (!mPlayer.isPlaying()) {

            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    // ToDo maybe not needed heres
                    mediaPlayer.start();
                }
            });

            try {
                mPlayer.setDataSource(mContext, uri);
                mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mPlayer.setLooping(true);
                mPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.reset();
        }
    }

    public void cleanup() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public boolean isPlaying() {
        if (mPlayer != null) {
           return mPlayer.isPlaying();
        }
        return false;
    }

}
