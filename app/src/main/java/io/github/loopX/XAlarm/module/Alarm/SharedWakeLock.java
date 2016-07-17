package io.github.loopX.XAlarm.module.Alarm;

import android.content.Context;
import android.os.PowerManager;

public class SharedWakeLock {

    private static SharedWakeLock sWakeLock;

    private PowerManager.WakeLock mFullWakeLock;
    private PowerManager.WakeLock mPartialWakeLock;

    private SharedWakeLock(Context context) {

        Context appContext = context.getApplicationContext();
        String TAG = this.getClass().getSimpleName(); // For debug purpose

        PowerManager powerManager = (PowerManager) appContext.getSystemService(Context.POWER_SERVICE);

        mFullWakeLock = powerManager.newWakeLock((PowerManager.FULL_WAKE_LOCK |
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE), TAG);

        mPartialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    }

    public static SharedWakeLock get(Context context) {
        if (sWakeLock == null) {
            sWakeLock = new SharedWakeLock(context);
        }
        return sWakeLock;
    }

    public void acquireFullWakeLock() {
        if (!mFullWakeLock.isHeld()) {
            mFullWakeLock.acquire();
        }
    }

    public void releaseFullWakeLock() {
        if (mFullWakeLock.isHeld()) {
            mFullWakeLock.release();
        }
    }

    public void acquirePartialWakeLock() {
        if (!mPartialWakeLock.isHeld()) {
            mPartialWakeLock.acquire();
        }
    }

    public void releasePartialWakeLock() {
        if (mPartialWakeLock.isHeld()) {
            mPartialWakeLock.release();
        }
    }

}
