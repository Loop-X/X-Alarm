package io.github.loop_x.yummywakeup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.rebound.Spring;

import java.util.Calendar;

import io.github.loop_x.yummywakeup.config.PreferenceKeys;
import io.github.loop_x.yummywakeup.infrastructure.BaseActivity;
import io.github.loop_x.yummywakeup.module.AlarmModule.Alarms;
import io.github.loop_x.yummywakeup.module.AlarmModule.model.Alarm;
import io.github.loop_x.yummywakeup.module.SetAlarmModule.SetAlarmActivity;
import io.github.loop_x.yummywakeup.module.SettingModule.AlarmPreferenceSettingsMenuLayout;
import io.github.loop_x.yummywakeup.module.UnlockTypeModule.UnlockTypeActivity;
import io.github.loop_x.yummywakeup.module.UnlockTypeModule.UnlockTypeEnum;
import io.github.loop_x.yummywakeup.tools.BaseSpringListener;
import io.github.loop_x.yummywakeup.tools.ReboundAnimation;
import io.github.loop_x.yummywakeup.tools.ToastMaster;
import io.github.loop_x.yummywakeup.view.DragMenuLayout;
import io.github.loop_x.yummywakeup.view.UnlockTypeMenuLayout;
import io.github.loop_x.yummywakeup.view.YummyTextView;

public class MainActivity extends BaseActivity implements View.OnClickListener, DragMenuLayout.DragMenuStateListener {

    private static final String TAG = "yummywakeup.MainActivity";

    private static final int SET_ALARM_REQUEST_CODE = 1;
    public static final int UNLOCK_TYPE_REQUEST_CODE = 2;

    private Alarm mAlarm;
    private int alarmId;

    private final static String M24 = "kk:mm";

    private View openRightDrawerView;
    private View openLeftDrawerView;
    private DragMenuLayout loopXDragMenuLayout;

    private YummyTextView tvAlarmTime;
    private TextView tvWakeUp;
    private View setAlarmView;
    protected Handler mHandler;
    private Spring translationYSpring;
    private int translationYEndValue;
    private AlarmPreferenceSettingsMenuLayout rightMenu;
    private ListView lvRingtoneList;
    private SeekBar sbAlarmVibration;
    
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onViewInitial() {
        mHandler = new Handler();

        tvAlarmTime = (YummyTextView) findViewById(R.id.tv_alarm_time);
        setAlarmView = findViewById(R.id.im_set_alarm);
        tvWakeUp = (TextView) findViewById(R.id.tv_wake_up);
        loopXDragMenuLayout = (DragMenuLayout) findViewById(R.id.dragMenuLayout);
        openRightDrawerView = findViewById(R.id.openRightDrawer);
        openLeftDrawerView = findViewById(R.id.openLeftDrawer);

        rightMenu = (AlarmPreferenceSettingsMenuLayout) loopXDragMenuLayout.findViewById(R.id.menuRight);
        sbAlarmVibration = (SeekBar) loopXDragMenuLayout.findViewById(R.id.sb_alarm_vibration);
        lvRingtoneList = (ListView) loopXDragMenuLayout.findViewById(R.id.lv_ringtone_list);

        loopXDragMenuLayout.setDragMenuStateListener(this);

        openLeftDrawerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loopXDragMenuLayout.getMenuStatus() == DragMenuLayout.MenuStatus.Close){
                    loopXDragMenuLayout.openLeftMenuWithAnimation();
                }else {
                    loopXDragMenuLayout.closeLeftMenuWithAnimation();
                }
            }
        });

        openRightDrawerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (loopXDragMenuLayout.getMenuStatus() == DragMenuLayout.MenuStatus.Close){
                    loopXDragMenuLayout.openRightMenuWithAnimation();
                }else {
                    loopXDragMenuLayout.closeRightMenuWithAnimation();
                }
                
            }
        });

        UnlockTypeMenuLayout unlockTypeMenuLayout = (UnlockTypeMenuLayout) loopXDragMenuLayout.getMenuView(DragMenuLayout.MenuDirection.LEFT);
        unlockTypeMenuLayout.setOnUnlockTypeMenuClickListener(new UnlockTypeMenuLayout.OnUnlockTypeMenuClickListener() {
            @Override
            public void onClick(UnlockTypeEnum unlockTypeEnum) {
                Intent intent = new Intent(MainActivity.this,UnlockTypeActivity.class);
                intent.putExtra("unlockType",unlockTypeEnum.getID());
                startActivityForResult(intent,UNLOCK_TYPE_REQUEST_CODE);        
            }
        });

        translationYSpring = ReboundAnimation.getInstance().createSpringFromBouncinessAndSpeed(12,9,new BaseSpringListener(){
            @Override
            public void onSpringUpdate(Spring spring) {
                int translationY = (int) transition((float) spring.getCurrentValue(),translationYEndValue,0);
                setAlarmView.setTranslationY(translationY);
                tvAlarmTime.setTranslationY(translationY);
                tvWakeUp.setTranslationY(translationY);
            }
        });
        
        setAlarmView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setAlarmView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int[] location = new int[2];
                setAlarmView.getLocationOnScreen(location);
                translationYEndValue = UIUtils.getScreenHeight() -  location[1];
                
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translationYSpring.setEndValue(1f);
                    }
                },100);
            }
        });

    }

    @Override
    public void onRefreshData() {
        initAlarm();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            // Go to Set Alarm Activity
            case R.id.im_set_alarm:
                Intent intent = new Intent(MainActivity.this, SetAlarmActivity.class);
                intent.putExtra(Alarms.ALARM_INTENT_EXTRA, mAlarm);
                startActivityForResult(intent, SET_ALARM_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case SET_ALARM_REQUEST_CODE:
                mAlarm = data.getParcelableExtra(Alarms.ALARM_INTENT_EXTRA);

                long newTime = Alarms.setAlarm(MainActivity.this, mAlarm);
                setAlarmTimeOnTextView(mAlarm);

                saveAlarm();

                Toast toast = Toast.makeText(MainActivity.this,
                        Alarms.formatToast(MainActivity.this, newTime),
                        Toast.LENGTH_SHORT);
                ToastMaster.setToast(toast);
                ToastMaster.showToast();
                break;
            case UNLOCK_TYPE_REQUEST_CODE:

                // Show content view instead of left menu
                loopXDragMenuLayout.restoreToMainContentView();

                int unlockTypeId = data.getIntExtra("unlockType", UnlockTypeEnum.Normal.getID());
                mAlarm.unlockType = unlockTypeId;
                Alarms.setAlarm(MainActivity.this, mAlarm);
                saveAlarm();

                // ToDo 更新左边栏
                ToastMaster.setToast(Toast.makeText(MainActivity.this,
                        "解锁方式已更新",
                        Toast.LENGTH_SHORT));
                ToastMaster.showToast();
            default:
                break;
        }
    }

    /**
     * Init alarm
     */
    private void initAlarm() {

        // Read saved alarm time from sharedPreference
        alarmId = readSavedAlarm();

        if (alarmId == -1) {
            // If no alarm available, set a default alarm with current time
            mAlarm = new Alarm();
            alarmId = Alarms.addAlarm(this, mAlarm);
            saveAlarm();
        } else {
            // ToDo 之前闹钟不灵 可不可能是CONTEXT的问题？
            mAlarm = Alarms.getAlarm(getContentResolver(), alarmId);
        }

        // Set alarm time on TextView
        setAlarmTimeOnTextView(mAlarm);
    }

    /**
     * Set current alarm time on TextView
     *
     * @param alarm
     */
    private void setAlarmTimeOnTextView(Alarm alarm) {

        final Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, alarm.hour);
        cal.set(Calendar.MINUTE, alarm.minutes);

        tvAlarmTime.setText(DateFormat.format(M24, cal));

    }

    /**
     * Save alarm time in sharedPreference
     */
    private void saveAlarm() {
        SharedPreferences.Editor editor =
                this.getSharedPreferences(PreferenceKeys.SHARE_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.putInt(PreferenceKeys.KEY_ALARM_ID, alarmId).commit();
    }

    /**
     * Read saved alarm time from sharedPreference
     *
     * @return Id of alarm time
     */
    private int readSavedAlarm() {
        SharedPreferences sharedPreferences =
                this.getSharedPreferences(PreferenceKeys.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PreferenceKeys.KEY_ALARM_ID, -1);
    }

    @Override
    public void onMenuOpened(DragMenuLayout.MenuDirection direction) {
        if(direction == DragMenuLayout.MenuDirection.RIGHT) {

            sbAlarmVibration.setProgress(mAlarm.vibrate? 1 : 0);

            String[] tmp = mAlarm.alert.toString().split("ringtone_");

            if(tmp.length == 2) {
                String ringtoneId = mAlarm.alert.toString().split("ringtone_")[1];
                rightMenu.setInitRingtone(Integer.valueOf(ringtoneId));
            } else {
                // ToDo whether to force the alarm url
                rightMenu.setInitRingtone(0);
            }
        }

    }

    @Override
    public void onMenuClosed(DragMenuLayout.MenuDirection direction) {
        if(direction == DragMenuLayout.MenuDirection.RIGHT) {

            rightMenu.stopRingtone();

            /** Update Vibration **/
            mAlarm.vibrate = rightMenu.getVibrationSetting();
            mAlarm.alert =
                    Uri.parse("android.resource://io.github.loop_x.yummywakeup/raw/ringtone_"
                            + rightMenu.getRingtone());


            // Save alarm
            Alarms.setAlarm(MainActivity.this, mAlarm);
            saveAlarm();

            ToastMaster.setToast(Toast.makeText(this, "Setting Updated", Toast.LENGTH_SHORT));
            ToastMaster.showToast();
        }
    }

}
