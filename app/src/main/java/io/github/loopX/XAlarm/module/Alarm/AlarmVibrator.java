package io.github.loopX.XAlarm.module.Alarm;

import android.content.Context;
import android.media.AudioAttributes;
import android.os.Vibrator;

/**
 * Wrapper of the system vibrator.
 * It is called by AlarmRingingController.
 */
public class AlarmVibrator {

    private Vibrator mVibrator;
    private boolean mIsVibrating;

    public AlarmVibrator(Context context) {
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrate() {
        if (!mIsVibrating) {

            // Start immediately
            // Vibrate for 200 milliseconds
            // Sleep for 500 milliseconds
            long[] vibrationPattern = {0, 200, 500};

            // Start to vibrate which repeats indefinitely
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mVibrator.vibrate(vibrationPattern, 0,
                        new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
            } else {
                mVibrator.vibrate(vibrationPattern, 0);
            }

            mIsVibrating = true;
        }
    }

    public void stop() {
        if (mIsVibrating) {
            mVibrator.cancel();
            mIsVibrating = false;
        }
    }

    public void cleanup() {
        stop();
        mVibrator = null;
    }
}

