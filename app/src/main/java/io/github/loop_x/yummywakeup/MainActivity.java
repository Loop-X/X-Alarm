package io.github.loop_x.yummywakeup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import io.github.loop_x.yummywakeup.config.PreferenceKeys;
import io.github.loop_x.yummywakeup.infrastructure.BaseActivity;
import io.github.loop_x.yummywakeup.module.AlarmModule.Alarms;
import io.github.loop_x.yummywakeup.module.AlarmModule.model.Alarm;
import io.github.loop_x.yummywakeup.module.SetAlarmModule.SetAlarmActivity;
import io.github.loop_x.yummywakeup.view.LoopXDragMenuLayout;
import io.github.loop_x.yummywakeup.view.YummyTextView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "yummywakeup.MainActivity";

    private static final int SET_ALARM_REQUEST_CODE = 1;

    private Alarm mAlarm;
    private int alarmId;

    private final static String M24 = "kk:mm";

    private View openRightDrawerView;
    private View openLeftDrawerView;
    private LoopXDragMenuLayout loopXDragMenuLayout;

    private YummyTextView tvAlarmTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onViewInitial() {

        tvAlarmTime = (YummyTextView) findViewById(R.id.tv_alarm_time);

        loopXDragMenuLayout = (LoopXDragMenuLayout) findViewById(R.id.dragMenuLayout);
        openRightDrawerView = findViewById(R.id.openRightDrawer);
        openLeftDrawerView = findViewById(R.id.openLeftDrawer);

        openLeftDrawerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loopXDragMenuLayout.getMenuStatus() == LoopXDragMenuLayout.MenuStatus.Close){
                    loopXDragMenuLayout.openLeftMenuWithAnimation();
                }else {
                    loopXDragMenuLayout.closeLeftMenuWithAnimation();
                }
            }
        });

        openRightDrawerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (loopXDragMenuLayout.getMenuStatus() == LoopXDragMenuLayout.MenuStatus.Close){
                    loopXDragMenuLayout.openRightMenuWithAnimation();
                }else {
                    loopXDragMenuLayout.closeRightMenuWithAnimation();
                }
                
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

        switch (requestCode) {
            case SET_ALARM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mAlarm = data.getParcelableExtra(Alarms.ALARM_INTENT_EXTRA);

                    long newTime = Alarms.setAlarm(MainActivity.this, mAlarm);
                    setAlarmTimeOnTextView(mAlarm);

                    saveAlarm();

                    Toast.makeText(MainActivity.this,
                            Alarms.formatToast(MainActivity.this, newTime),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /*   private void changeFragment(Fragment targetFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }*/

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

   
}
