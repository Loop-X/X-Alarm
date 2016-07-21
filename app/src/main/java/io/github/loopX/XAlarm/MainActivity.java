package io.github.loopX.XAlarm;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.rebound.Spring;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.github.loopX.XAlarm.database.AlarmDBService;
import io.github.loopX.XAlarm.infrastructure.BaseActivity;
import io.github.loopX.XAlarm.module.Alarm.AlarmScheduler;
import io.github.loopX.XAlarm.module.Alarm.Alarm;
import io.github.loopX.XAlarm.module.SetAlarmModule.SetAlarmActivity;
import io.github.loopX.XAlarm.module.SettingModule.AlarmPreferenceSettingsMenuLayout;
import io.github.loopX.XAlarm.module.UnlockTypeModule.UnlockTypeActivity;
import io.github.loopX.XAlarm.module.UnlockTypeModule.UnlockTypeEnum;
import io.github.loopX.XAlarm.tools.BaseSpringListener;
import io.github.loopX.XAlarm.tools.ReboundAnimation;
import io.github.loopX.XAlarm.tools.ToastMaster;
import io.github.loopX.XAlarm.view.DragMenuLayout;
import io.github.loopX.XAlarm.view.UnlockTypeMenuLayout;
import io.github.loopX.XAlarm.view.YummyTextView;

public class MainActivity extends BaseActivity implements View.OnClickListener, DragMenuLayout.DragMenuStateListener {

    private static final String TAG = "ywp.MainActivity";

    private static final int SET_ALARM_REQUEST_CODE = 1;
    public static final int UNLOCK_TYPE_REQUEST_CODE = 2;

    private Alarm mAlarm;
    private int alarmId;

    public final static String M12 = "hh:mm";
    public final static String M24 = "kk:mm";

    private ImageView ivRightMenuIndicator;
    private ImageView ivLeftMenuIndicator;
    private ImageView ivMainContentIndicator;
    private DragMenuLayout loopXDragMenuLayout;

    private YummyTextView tvAlarmTime;
    private YummyTextView tvAlarmAMPM;

    private TextView tvWakeUp;
    private View setAlarmView;
    protected Handler mHandler;
    private Spring translationYSpring;
    private int translationYEndValue;
    private AlarmPreferenceSettingsMenuLayout rightMenu;
    private UnlockTypeMenuLayout leftMenu;
    private ListView lvRingtoneList;
    private Window mWindow;
    
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onViewInitial() {

        mWindow = this.getWindow();

        mHandler = new Handler();

        tvAlarmTime = (YummyTextView) findViewById(R.id.tv_alarm_time);
        tvAlarmAMPM = (YummyTextView) findViewById(R.id.tv_alarm_am_pm);
        tvWakeUp    = (YummyTextView) findViewById(R.id.tv_wake_up);

        loopXDragMenuLayout    = (DragMenuLayout) findViewById(R.id.dragMenuLayout);
        ivRightMenuIndicator   = (ImageView) findViewById(R.id.iv_right_menu_indicator);
        ivRightMenuIndicator.setTag(R.drawable.main_right);
        ivLeftMenuIndicator    = (ImageView) findViewById(R.id.iv_left_menu_indicator);
        ivMainContentIndicator = (ImageView) findViewById(R.id.iv_top_main_content_indicator);

        setAlarmView = findViewById(R.id.im_set_alarm);

        rightMenu = (AlarmPreferenceSettingsMenuLayout) loopXDragMenuLayout.findViewById(R.id.menuRight);
        leftMenu  = (UnlockTypeMenuLayout) loopXDragMenuLayout.findViewById(R.id.menuLeft);

        lvRingtoneList = (ListView) loopXDragMenuLayout.findViewById(R.id.lv_ringtone_list);

        loopXDragMenuLayout.setDragMenuStateListener(this);

        leftMenu.setOnUnlockTypeMenuClickListener(new UnlockTypeMenuLayout.OnUnlockTypeMenuClickListener() {
            @Override
            public void onClick(UnlockTypeEnum unlockTypeEnum,int currentUnlockType) {
                Intent intent = new Intent(MainActivity.this,UnlockTypeActivity.class);
                intent.putExtra("unlockType",unlockTypeEnum.getID());
                intent.putExtra("currentUnlockType",currentUnlockType);
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
        if(mAlarm.isEnabled()) {
            ivMainContentIndicator.setImageResource(R.drawable.main_mid);
        } else {
            ivMainContentIndicator.setImageResource(R.drawable.main_mid_off);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            // Go to Set Alarm Activity
            case R.id.im_set_alarm:
                Intent intent = new Intent(MainActivity.this, SetAlarmActivity.class);
                intent.putExtra(AlarmScheduler.X_ALARM_ID, mAlarm.getId());
                startActivityForResult(intent, SET_ALARM_REQUEST_CODE);
                break;
            case R.id.iv_top_main_content_indicator:
                if(mAlarm.isEnabled()) {
                    mAlarm.setEnabled(false);
                    ivMainContentIndicator.setImageResource(R.drawable.main_mid_off);
                    AlarmScheduler.cancelAlarm(this, mAlarm);
                    ToastMaster.setToast(Toast.makeText(MainActivity.this,
                            getString(R.string.turn_off_alarm),
                            Toast.LENGTH_SHORT));
                } else {
                    mAlarm.setEnabled(true);
                    ivMainContentIndicator.setImageResource(R.drawable.main_mid);
                    AlarmScheduler.scheduleAlarm(this, mAlarm);
                    ToastMaster.setToast(Toast.makeText(MainActivity.this,
                            getString(R.string.turn_on_alarm),
                            Toast.LENGTH_SHORT));
                }
                ToastMaster.showToast();
                AlarmDBService.getInstance(this).updateAlarm(mAlarm);
                break;
            case R.id.iv_left_menu_indicator:
                if (loopXDragMenuLayout.getMenuStatus() == DragMenuLayout.MenuStatus.Close){
                    loopXDragMenuLayout.openLeftMenuWithAnimation();
                }else {
                    loopXDragMenuLayout.closeMenuWithAnimation();
                }
                break;
            case R.id.iv_right_menu_indicator:
                if (loopXDragMenuLayout.getMenuStatus() == DragMenuLayout.MenuStatus.Close){
                    loopXDragMenuLayout.openRightMenuWithAnimation();
                }else {
                    loopXDragMenuLayout.closeMenuWithAnimation();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case SET_ALARM_REQUEST_CODE:
                mAlarm = data.getParcelableExtra(AlarmScheduler.X_ALARM_ID);

                AlarmDBService.getInstance(this).updateAlarm(mAlarm);

                setAlarmTimeOnTextView(mAlarm);

                String text = null;

                if(mAlarm.isEnabled()) {
                    long newTime = AlarmScheduler.scheduleAlarm(this, mAlarm);
                    ivMainContentIndicator.setImageResource(R.drawable.main_mid);
                    text = AlarmScheduler.formatToast(MainActivity.this, newTime);
                } else {
                    AlarmScheduler.cancelAlarm(this, mAlarm);
                    ivMainContentIndicator.setImageResource(R.drawable.main_mid_off);
                    text = getString(R.string.turn_off_alarm);
                }

                ToastMaster.setToast(Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT));
                ToastMaster.showToast();
                break;
            case UNLOCK_TYPE_REQUEST_CODE:

                // Show content view instead of left menu
                loopXDragMenuLayout.closeMenuWithoutAnimation();

                // Restore status of icon
                ivLeftMenuIndicator.setImageResource(R.drawable.main_left);

                int unlockTypeId = data.getIntExtra("unlockType", UnlockTypeEnum.Type.getID());
                mAlarm.setUnlockType(unlockTypeId);
                AlarmDBService.getInstance(this).updateAlarm(mAlarm);

                // Update left menu with chosen unlock type
                setLeftMenuStatus();

                ToastMaster.setToast(Toast.makeText(MainActivity.this,
                        getString(R.string.unlock_type_updated),
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
        Log.d(TAG, "-----------> initAlarm");

        List<Alarm> alarms = AlarmDBService.getInstance(this).getAlarms();

        if(alarms.isEmpty()) {
            mAlarm = new Alarm();
            AlarmDBService.getInstance(this).addAlarm(mAlarm);
        } else {
            // Always force to get first alarm in list
            mAlarm = alarms.get(0);
        }

        // Set alarm time on TextView
        setAlarmTimeOnTextView(mAlarm);

        // Set unlock type on left menu
        setLeftMenuStatus();

        // Set Right Menu Status
        setRightMenuStatus();

        Log.d(TAG, "<----------- initAlarm");
    }

    /**
     * Set current alarm time on TextView
     * @param alarm
     */
    private void setAlarmTimeOnTextView(Alarm alarm) {

        final Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, alarm.getTimeHour());
        cal.set(Calendar.MINUTE, alarm.getTimeMinute());

        Locale locale = new Locale("en");

        if(DateFormat.is24HourFormat(this)) {
            SimpleDateFormat dateFormatTime = new SimpleDateFormat(M24, locale);
            tvAlarmTime.setText(dateFormatTime.format(cal.getTime()));
            tvAlarmAMPM.setVisibility(View.GONE);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(M12, locale);
            SimpleDateFormat dateFormatAMPM = new SimpleDateFormat("a", locale);
            tvAlarmTime.setText(dateFormat.format(cal.getTime()));
            tvAlarmAMPM.setVisibility(View.VISIBLE);
            tvAlarmAMPM.setText(dateFormatAMPM.format(cal.getTime()));
        }

    }

    @Override
    public void onMenuOpened(DragMenuLayout.MenuDirection direction) {
        if(direction == DragMenuLayout.MenuDirection.RIGHT) {
            ivRightMenuIndicator.setImageResource(R.drawable.main_right_press);
            ivRightMenuIndicator.setTag(R.drawable.main_right_press);
        } else {
            // To avoid user slide too quickly from right to left
            if((int) ivRightMenuIndicator.getTag() == R.drawable.main_right_press) {
                onMenuClosed(DragMenuLayout.MenuDirection.RIGHT);
            }

            ivLeftMenuIndicator.setImageResource(R.drawable.main_left_press);
        }
    }

    @Override
    public void onMenuClosed(DragMenuLayout.MenuDirection direction) {
        if(direction == DragMenuLayout.MenuDirection.RIGHT) {

            ivRightMenuIndicator.setImageResource(R.drawable.main_right);
            ivRightMenuIndicator.setTag(R.drawable.main_right);

            rightMenu.stopRingtone();

            /** Update Vibration **/
            mAlarm.setVibrate(rightMenu.getVibrationSetting());
            mAlarm.setAlarmTone(Uri.parse(XAlarmApp.getResourcePath() + "/raw/ringtone_"
                            + rightMenu.getRingtone()));

            // Save alarm
            AlarmDBService.getInstance(this).updateAlarm(mAlarm);

            // Update right menu
            setRightMenuStatus();

            ToastMaster.setToast(Toast.makeText(this, getString(R.string.setting_updated), Toast.LENGTH_SHORT));
            ToastMaster.showToast();
        } else {
            ivLeftMenuIndicator.setImageResource(R.drawable.main_left);
        }
    }

    private void setRightMenuStatus() {
        rightMenu.setInitRingtone(
                Integer.valueOf(mAlarm.getAlarmTone().toString().split("ringtone_")[1]));
        rightMenu.setInitVibration(mAlarm.isVibrate());
    }

    private void setLeftMenuStatus() {
        // Set unlock type on left menu
        leftMenu.resetChosenStatus();
        leftMenu.setChosenStatue(mAlarm.getUnlockType());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            mWindow.setStatusBarColor(getColor(R.color.loopX_1_50_alpha));
        }


        /*
        if (hasFocus) {
            final View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        */

    }

}
