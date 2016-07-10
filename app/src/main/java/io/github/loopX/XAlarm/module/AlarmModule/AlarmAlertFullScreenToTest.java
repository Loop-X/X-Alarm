package io.github.loopX.XAlarm.module.AlarmModule;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

import io.github.loopX.XAlarm.R;
import io.github.loopX.XAlarm.XAlarmApp;
import io.github.loopX.XAlarm.module.UnlockTypeModule.alarmType.UnlockFragment;
import io.github.loopX.XAlarm.module.UnlockTypeModule.alarmType.UnlockFragmentFactory;


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
            mMediaPlayer.setDataSource(this, Uri.parse(XAlarmApp.getResourcePath() + "/raw/ringtone_0"));
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

        Vibrator mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mVibrator.vibrate(500);

        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Avoid closing alarm by the following keys
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_CAMERA:
            case KeyEvent.KEYCODE_FOCUS:
                return true;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = this.getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.loopX_3_60_alpha));
        }
    }
}
