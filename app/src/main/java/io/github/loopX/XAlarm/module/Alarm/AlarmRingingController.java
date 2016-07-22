package io.github.loopX.XAlarm.module.Alarm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.UUID;

import io.github.loopX.XAlarm.XAlarmApp;
import io.github.loopX.XAlarm.database.AlarmDBService;
import io.github.loopX.XAlarm.module.AlarmModule.AlarmAlertFullScreen;

public final class AlarmRingingController {

    private Context mContext;
    private Alarm mCurrentAlarm;
    private boolean mAllowDismissRequested;

    public AlarmRingingController(Context context) {
        mContext = context;
    }

    protected void registerAlarm(Intent intent) {
        SharedWakeLock.getInstance(mContext).acquireFullWakeLock();

        if (intent != null) {

            // Get alarm ID from intent
            UUID alarmId = (UUID) intent.getExtras().getSerializable(AlarmScheduler.X_ALARM_ID);

            // Get alarm instance from DB
            AlarmDBService alarmDBService = AlarmDBService.getInstance(XAlarmApp.getAppContext());
            mCurrentAlarm = alarmDBService.getAlarm(alarmId);

            // Start ringtone and Vibrator
            // startAlarmRinging();

            // Launch alarm unlock UI
            launchRingingUX(alarmId);

            AlarmNotificationManager
                    .getInstance(mContext)
                    .handleAlarmRunningNotificationStatus(alarmId);
        }
    }

    protected void alarmRingingSessionCompleted() {
        // We need to handle the case where the alarm timed out. In that case we
        // wont get an explicit call from the AlarmRingingActivity to silence the alarm
        // silenceAlarmRinging();
        mCurrentAlarm = null;

        // Cleanup all states
        // mVibrator.cleanup();
        // mRingtonePlayer.cleanup();

        SharedWakeLock.getInstance(mContext).releaseFullWakeLock();
    }

    public void requestAllowDismiss() {
        mAllowDismissRequested = true;
    }

    // alarmRingingSessionCompleted should always be called before this method.  If not, we should
    // restart the AlarmRingingActivity so that we can successfully finish the alarm session
    public void alarmRingingSessionDismissed() {
        if (mAllowDismissRequested) {
            mAllowDismissRequested = false;
        } else {
            if (mCurrentAlarm != null) {
                launchRingingUX(mCurrentAlarm.getId());
            }
        }
    }

    /**
     * Send intent to launch alarm unlock UI
     * @param alarmId
     */
    private void launchRingingUX(UUID alarmId) {
        Intent intent = new Intent(mContext, AlarmAlertFullScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
                Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        intent.putExtra(AlarmScheduler.X_ALARM_ID, alarmId);
        mContext.startActivity(intent);
    }
}