package io.github.loopX.XAlarm.module.Alarm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import java.util.UUID;

import io.github.loopX.XAlarm.R;
import io.github.loopX.XAlarm.database.AlarmDBService;
import io.github.loopX.XAlarm.module.UnlockTypeModule.alarmType.UnlockFragment;
import io.github.loopX.XAlarm.module.UnlockTypeModule.alarmType.UnlockFragmentFactory;


/**
 * Alarm Clock alarm alert: pops visible indicator and plays alarm
 * tone. This activity is the full screen version which shows over the lock
 * screen with the wallpaper as the background.
 */
public class AlarmAlertFullScreen extends FragmentActivity implements UnlockFragment.OnAlarmAction {

    private final static String TAG = "AlarmAlertFullScreen";

    protected static final String SCREEN_OFF = "screen_off";

    protected Alarm mAlarm;
    private AlarmVibrator mVibrator;
    private AlarmRingtonePlayer mRingtonePlayer;

    @Override
    protected void onCreate(Bundle icicle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(icicle);

        setContentView(R.layout.framelayout_alarm_unlock);

        UUID alarmID = (UUID) getIntent().getSerializableExtra(AlarmScheduler.X_ALARM_ID);
        mAlarm = AlarmDBService.getInstance(this).getAlarm(alarmID);

        /**
         * Pop-up unlock fragment according to unlock type
         */
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        UnlockFragment unlockFragment = UnlockFragmentFactory.create(mAlarm.getUnlockType());
        fragmentTransaction.replace(R.id.fg_alarm, unlockFragment);
        fragmentTransaction.commit();

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        // Turn on the screen unless we are being launched from the AlarmAlert
        // subclass.
        if (!getIntent().getBooleanExtra(SCREEN_OFF, false)) {
            win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        }

        mVibrator = new AlarmVibrator(this);
        mRingtonePlayer = new AlarmRingtonePlayer(this);

        // Register to get the alarm killed/snooze/dismiss intent.
        // IntentFilter filter = new IntentFilter(Alarms.ALARM_KILLED);
        // filter.addAction(Alarms.ALARM_DISMISS_ACTION);
        // registerReceiver(mReceiver, filter);
    }

    /**
     * this is called when a second alarm is triggered while a
     * previous alert window is still active.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        UUID alarmID = (UUID) getIntent().getSerializableExtra(AlarmScheduler.X_ALARM_ID);
        mAlarm = AlarmDBService.getInstance(this).getAlarm(alarmID);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAlarm.isVibrate()) {
            mVibrator.vibrate();
        }

        mRingtonePlayer.play(mAlarm.getAlarmTone());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // No longer care about the alarm being killed.
        // unregisterReceiver(mReceiver);
        mVibrator.cleanup();
        mRingtonePlayer.cleanup();
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
    public void onBackPressed() {
        // Don't allow back to dismiss. This method is overridden by AlarmAlert
        // so that the dialog is dismissed.
        return;
    }

    @Override
    public void closeAlarm() {

        // If alarm not vibrate mode. So vibrate 0.5s in the end
        if (mAlarm.isVibrate()) {
            mVibrator.stop();
        } else {
            mVibrator.vibrate(500);
        }

        mRingtonePlayer.stop();

        AlarmNotificationManager.getInstance(this).disableNotifications();

        // Set next alarm
        mAlarm.schedule();

        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = this.getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.loopX_1));
        }
    }

}
