package io.github.loop_x.yummywakeup.module.AlarmModule;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import java.io.IOException;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.module.UnlockTypeModule.alarmType.UnlockFragment;
import io.github.loop_x.yummywakeup.module.UnlockTypeModule.alarmType.UnlockFragmentFactory;


public class AlarmAlertFullScreenToTest extends FragmentActivity implements UnlockFragment.OnAlarmAction {

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle icicle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(icicle);
        setContentView(R.layout.framelayout_alarm_unlock);

        int unlockType = getIntent().getIntExtra("UnlockType", 1);

        /**
         * Set ringtone
         */
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(this, Uri.parse("android.resource://io.github.loop_x.yummywakeup/raw/ringtone_0"));
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Pop-up unlock fragment according to unlock type
         */
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        UnlockFragment unlockFragment = UnlockFragmentFactory.create(unlockType);
        fragmentTransaction.replace(R.id.fg_alarm, unlockFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void closeAlarm() {
        if(mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        finish();
    }

}
