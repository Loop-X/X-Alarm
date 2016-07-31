package io.github.loopX.XAlarm.module.SetAlarmModule;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.rebound.SpringUtil;
import com.wx.wheelview.common.WheelConstants;
import com.wx.wheelview.widget.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.github.loopX.XAlarm.MainActivity;
import io.github.loopX.XAlarm.R;
import io.github.loopX.XAlarm.tools.UIUtils;
import io.github.loopX.XAlarm.database.AlarmDBService;
import io.github.loopX.XAlarm.infrastructure.BaseActivity;
import io.github.loopX.XAlarm.module.Alarm.Alarm;
import io.github.loopX.XAlarm.module.Alarm.AlarmScheduler;
import io.github.loopX.XAlarm.tools.ToastMaster;
import io.github.loopX.XAlarm.view.RippleBackgroundView;
import io.github.loopX.XAlarm.view.YummyTextView;

public class SetAlarmActivity extends BaseActivity {

    private Alarm mAlarm;
    
    private WheelView hourWheelView;
    private WheelView minuteWheelView;
    private WheelView amPmWheelView;
    private ArrayList<String> minutesStrList;
    private ArrayList<String> hourStrList;
    private ArrayList<String> amPMStrList;
    
    private DayOfWeekSelectorView mondaySelectorView;
    private DayOfWeekSelectorView tuesdaySelector;
    private DayOfWeekSelectorView wednesdaySelector;
    private DayOfWeekSelectorView thursdaySelector;
    private DayOfWeekSelectorView fridaySelector;
    private DayOfWeekSelectorView saturdaySelector;
    private DayOfWeekSelectorView sundaySelector;

    private YummyTextView tvMON;
    private YummyTextView tvTUE;
    private YummyTextView tvWED;
    private YummyTextView tvTHU;
    private YummyTextView tvFRI;
    private YummyTextView tvSAT;
    private YummyTextView tvSUN;

    private YummyTextView tvCurrentAlarmTime;
    private YummyTextView tvCurrentAlarmAMPM;

    private ImageView btnAccept;
    private ImageView btnSwitchOnOffAlarm;
    private View setAlarmTopBarView;
    
    private RippleBackgroundView rippleBackgroundView;
    
    private Boolean is24hMode;
    private String AMPM;
    
    private int maxRippleRadius;
    private int ripplePivotY;
    private int ripplePivotX;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_alarm;
    }

    @Override
    public void onViewInitial() {

        /** Check 12 or 24 hour mode **/

        is24hMode = DateFormat.is24HourFormat(this);

        /** Get alarm object from intent **/

        UUID alarmId = (UUID) getIntent().getSerializableExtra(AlarmScheduler.X_ALARM_ID);
        mAlarm = AlarmDBService.getInstance(this).getAlarm(alarmId);

        /** Init top bar **/

        tvCurrentAlarmTime = (YummyTextView) findViewById(R.id.tv_set_alarm_current_alarm_time);
        tvCurrentAlarmAMPM = (YummyTextView) findViewById(R.id.tv_set_alarm_current_alarm_ampm);
        btnSwitchOnOffAlarm = (ImageView) findViewById(R.id.iv_switch_on_off_alarm);
        setAlarmTopBarView =  findViewById(R.id.rl_set_alarm_top_bar);
        rippleBackgroundView  = (RippleBackgroundView) findViewById(R.id.rippleBackground);

        maxRippleRadius = (int) Math.sqrt(UIUtils.getScreenWidth() * UIUtils.getScreenWidth() + UIUtils.dip2px(156) * UIUtils.dip2px(156));
        ripplePivotY = UIUtils.dip2px(156);
        ripplePivotX = UIUtils.getScreenWidth() - UIUtils.dip2px(100) / 2 - UIUtils.dip2px(16);
        
        
        if (mAlarm.isEnabled()) {
            btnSwitchOnOffAlarm.setImageResource(R.drawable.switch_off);
            rippleBackgroundView.setBackgroundResource(R.color.alarm_set_top_alarm_on);

        } else {
            btnSwitchOnOffAlarm.setImageResource(R.drawable.switch_on);
            rippleBackgroundView.setBackgroundResource(R.color.alarm_set_top_alarm_off);
        }

        final Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, mAlarm.getTimeHour());
        cal.set(Calendar.MINUTE, mAlarm.getTimeMinute());

        Locale locale = new Locale("en");

        if(is24hMode) {
            SimpleDateFormat dateFormatTime = new SimpleDateFormat(MainActivity.M24, locale);
            tvCurrentAlarmTime.setText(dateFormatTime.format(cal.getTime()));
            tvCurrentAlarmAMPM.setVisibility(View.GONE);
        } else {
            SimpleDateFormat dateFormatTime = new SimpleDateFormat(MainActivity.M12, locale);
            SimpleDateFormat dateFormatAMPM = new SimpleDateFormat("a", locale);
            tvCurrentAlarmTime.setText(dateFormatTime.format(cal.getTime()));
            tvCurrentAlarmAMPM.setVisibility(View.VISIBLE);
            tvCurrentAlarmAMPM.setText(dateFormatAMPM.format(cal.getTime()));
        }


        /** Init Time Picker **/
        amPmWheelView = (WheelView) findViewById(R.id.am_pm_wheelview);
        minuteWheelView = (WheelView) findViewById(R.id.minute_wheelview);
        hourWheelView = (WheelView) findViewById(R.id.hour_wheelview);

        WheelView.WheelViewStyle style = new WheelView.WheelStyleBuilder(this)
                .selectedTextSize(60)
                .unselectedTextSize(60)
                .selectedTextColor( ContextCompat.getColor(this, R.color.loopX_3))
                .unselectedTextColor( ContextCompat.getColor(this, R.color.loopX_3))
                .wheelForegroundMask(new TimePickWheelMask(this))
                .build();

        hourWheelView.setStyle(style);
        hourWheelView.setWheelAdapter(new TimePickWheelAdapter(this));
        hourWheelView.setWheelSize(3);
        hourWheelView.setWheelData(createHours(is24hMode));
        hourWheelView.setLoop(true);

        WheelView.WheelViewStyle minuteStyle = new WheelView.WheelStyleBuilder(this)
                .selectedTextSize(60)
                .unselectedTextSize(60)
                .selectedTextColor( ContextCompat.getColor(this, R.color.loopX_3))
                .unselectedTextColor( ContextCompat.getColor(this, R.color.loopX_3))
                .wheelForegroundMask(new TimePickWheelMask(this))
                .build();
        minuteWheelView.setStyle(minuteStyle);
        minuteWheelView.setWheelAdapter(new TimePickWheelAdapter(this));
        minuteWheelView.setWheelSize(3);
        minuteWheelView.setWheelData(createMinutes());
        minuteWheelView.setLoop(true);


        WheelView.WheelViewStyle amPMStyle = new WheelView.WheelStyleBuilder(this)
                .selectedTextSize(30)
                .unselectedTextSize(30)
                .selectedTextColor( ContextCompat.getColor(this, R.color.loopX_3))
                .unselectedTextColor( ContextCompat.getColor(this, R.color.loopX_3_40_alpha))
                .build();
        amPmWheelView.setStyle(amPMStyle);
        amPmWheelView.setWheelSize(3);
        amPmWheelView.setWheelAdapter(new TimePickWheelAdapter(this));
        amPmWheelView.setWheelData(createAmPMStrList());
        amPmWheelView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (amPmWheelView.getFirstVisiblePosition() == 0){
                        View itemView = amPmWheelView.getChildAt(1);
                        float deltaY = itemView.getY();
                        if (deltaY >= 0) {
                            int d = amPmWheelView.getSmoothDistance(deltaY);
                            amPmWheelView.smoothScrollBy(d, WheelConstants
                                    .WHEEL_SMOOTH_SCROLL_DURATION);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount != 0) {
                    amPmWheelView.refreshCurrentPosition(false);
                }
            }
        });
        
        
        SimpleDateFormat dateFormatHour = null;

        if(is24hMode) {
            amPmWheelView.setVisibility(View.INVISIBLE);
            AMPM = "";
            dateFormatHour = new SimpleDateFormat("HH", locale);
        } else {
            amPmWheelView.setVisibility(View.VISIBLE);
            SimpleDateFormat dateFormatAMPM = new SimpleDateFormat("a", locale);
            AMPM = dateFormatAMPM.format(cal.getTime());
            amPmWheelView.setSelection((AMPM.equals("AM") ? 1 : 2));
            dateFormatHour = new SimpleDateFormat("hh", locale);
        }

        minuteWheelView.setSelection(minutesStrList.indexOf("" + mAlarm.getTimeMinute()));
        hourWheelView.setSelection(hourStrList.indexOf(dateFormatHour.format(cal.getTime())));

        hourWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                String text = hourStrList.get(position);
                if(is24hMode) {
                    mAlarm.setTimeHour(Integer.valueOf(text));
                } else {
                    if(AMPM.equals("AM")) {
                        mAlarm.setTimeHour(Integer.valueOf(text));
                    } else {
                        mAlarm.setTimeHour(Integer.valueOf(text) + 12);
                    }
                }
            }
        });

        minuteWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mAlarm.setTimeMinute(Integer.valueOf(minutesStrList.get(position)));

            }
        });

        amPmWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                // If AMPM select, it must be 12h mode
                AMPM = amPMStrList.get(position);
                if(AMPM.equals("PM")) {
                    if(mAlarm.getTimeHour() <= 12) {
                        mAlarm.setTimeHour(mAlarm.getTimeHour() + 12);
                    }
                } else {
                    if(mAlarm.getTimeHour() > 12) {
                        mAlarm.setTimeHour(mAlarm.getTimeHour() - 12);
                    }
                }
            }
        });
        
        
        tvMON = (YummyTextView) findViewById(R.id.tv_monday);
        tvTUE = (YummyTextView) findViewById(R.id.tv_tuesday);
        tvWED = (YummyTextView) findViewById(R.id.tv_wednesday);
        tvTHU = (YummyTextView) findViewById(R.id.tv_thursday);
        tvFRI = (YummyTextView) findViewById(R.id.tv_friday);
        tvSAT = (YummyTextView) findViewById(R.id.tv_saturday);
        tvSUN = (YummyTextView) findViewById(R.id.tv_sunday);
        
        btnAccept = (ImageView) findViewById(R.id.im_set_alarm_accept);

        mondaySelectorView = (DayOfWeekSelectorView) findViewById(R.id.mondaySelector);
        tuesdaySelector = (DayOfWeekSelectorView) findViewById(R.id.tuesdaySelector);
        wednesdaySelector = (DayOfWeekSelectorView) findViewById(R.id.wednesdaySelector);
        thursdaySelector = (DayOfWeekSelectorView) findViewById(R.id.thursdaySelector);
        fridaySelector = (DayOfWeekSelectorView) findViewById(R.id.fridaySelector);
        saturdaySelector = (DayOfWeekSelectorView) findViewById(R.id.saturdaySelector);
        sundaySelector = (DayOfWeekSelectorView) findViewById(R.id.sundaySelector);
        
        mondaySelectorView.setDayOfWeekSelectorListener(dayOfWeekSelectorListener);
        tuesdaySelector.setDayOfWeekSelectorListener(dayOfWeekSelectorListener);
        wednesdaySelector.setDayOfWeekSelectorListener(dayOfWeekSelectorListener);
        thursdaySelector.setDayOfWeekSelectorListener(dayOfWeekSelectorListener);
        fridaySelector.setDayOfWeekSelectorListener(dayOfWeekSelectorListener);
        saturdaySelector.setDayOfWeekSelectorListener(dayOfWeekSelectorListener);
        sundaySelector.setDayOfWeekSelectorListener(dayOfWeekSelectorListener);

        initRepeat();

    }

    @NonNull
    private List<String> createAmPMStrList() {
        amPMStrList = new ArrayList<>();
        amPMStrList.addAll(Arrays.asList(new String []{"","AM","PM"}));
        return amPMStrList;
    }

    private ArrayList<String> createHours(boolean is24hMode) {

        hourStrList = new ArrayList<>();
        for (int i = (is24hMode ? 0 : 1); i < (is24hMode ? 24 : 13); i++) {
            if (i < 10) {
                hourStrList.add("0" + i);
            } else {
                hourStrList.add("" + i);
            }
        }
        return hourStrList;

    }
    private ArrayList<String> createMinutes() {
        minutesStrList = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minutesStrList.add("0" + i);
            } else {
                minutesStrList.add("" + i);
            }
        }
        return minutesStrList;
    }

    DayOfWeekSelectorView.DayOfWeekSelectorListener dayOfWeekSelectorListener = new DayOfWeekSelectorView.DayOfWeekSelectorListener() {
        @Override
        public void onDayOfWeekSelector(int dayOfWeek, boolean selected) {
            mAlarm.setRepeatingDay(dayOfWeek, selected);
        }
    };

    @Override
    public void onRefreshData() {

    }
    
    private void doAlarmOnRippleAnimation(){
        rippleBackgroundView.startRipple(new RippleBackgroundView.RippleBuilder(this)
                .setRippleColor(UIUtils.getColor(R.color.alarm_set_top_alarm_on))
                .setStartRippleRadius(0)
                .setFinishRippleRadius(maxRippleRadius)
                .setRipplePivotX(ripplePivotX)
                .setRipplePivotY(ripplePivotY)
                .setBackgroundColor(UIUtils.getColor(R.color.alarm_set_top_alarm_off))
        );
    }
    
    private void doAlarmOffRippleAnimation(){
        rippleBackgroundView.startRipple(new RippleBackgroundView.RippleBuilder(this)
                .setRippleColor(UIUtils.getColor(R.color.alarm_set_top_alarm_on))
                .setStartRippleRadius(maxRippleRadius)
                .setFinishRippleRadius(0)
                .setRipplePivotX(ripplePivotX)
                .setRipplePivotY(ripplePivotY)
                .setBackgroundColor(UIUtils.getColor(R.color.alarm_set_top_alarm_off))
        );
    }

    protected float transition(float progress, float startValue, float endValue) {
        return (float) SpringUtil.mapValueFromRangeToRange(progress, 0, 1, startValue, endValue);
    }

    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.iv_switch_on_off_alarm:
                if(mAlarm.isEnabled()) {
                    btnSwitchOnOffAlarm.setImageResource(R.drawable.switch_on);
                    doAlarmOffRippleAnimation();
                    ToastMaster.setToast(
                            Toast.makeText(this, R.string.turn_off_alarm, Toast.LENGTH_SHORT));
                    ToastMaster.showToast();
                    mAlarm.setEnabled(false);
                } else {
                    btnSwitchOnOffAlarm.setImageResource(R.drawable.switch_off);
                    doAlarmOnRippleAnimation();
                    ToastMaster.setToast(
                            Toast.makeText(this, R.string.turn_on_alarm, Toast.LENGTH_SHORT));
                    ToastMaster.showToast();
                    mAlarm.setEnabled(true);
                }
                break;
            case R.id.im_set_alarm_accept:
                Intent intent = new Intent();
                intent.putExtra(AlarmScheduler.X_ALARM_ID, mAlarm.getId());
                setResult(RESULT_OK, intent);

                // Update alarm in DB
                AlarmDBService.getInstance(this).updateAlarm(mAlarm);

                finish();
                break;
        }

    }

    private void initRepeat() {

        int colorActiv = ContextCompat.getColor(this, R.color.loopX_3);
        int colorNoActiv = ContextCompat.getColor(this, R.color.loopX_6);

        tvMON.setTextColor(mAlarm.getRepeatingDay(Calendar.MONDAY - 1) ? colorActiv : colorNoActiv);
        tvTUE.setTextColor(mAlarm.getRepeatingDay(Calendar.TUESDAY - 1) ? colorActiv : colorNoActiv);
        tvWED.setTextColor(mAlarm.getRepeatingDay(Calendar.WEDNESDAY - 1) ? colorActiv : colorNoActiv);
        tvTHU.setTextColor(mAlarm.getRepeatingDay(Calendar.THURSDAY - 1) ? colorActiv : colorNoActiv);
        tvFRI.setTextColor(mAlarm.getRepeatingDay(Calendar.FRIDAY - 1) ? colorActiv : colorNoActiv);
        tvSAT.setTextColor(mAlarm.getRepeatingDay(Calendar.SATURDAY - 1) ? colorActiv : colorNoActiv);
        tvSUN.setTextColor(mAlarm.getRepeatingDay(Calendar.SUNDAY - 1) ? colorActiv : colorNoActiv);

        mondaySelectorView.setDay(Calendar.MONDAY, mAlarm.getRepeatingDay(Calendar.MONDAY - 1));
        tuesdaySelector.setDay(Calendar.TUESDAY, mAlarm.getRepeatingDay(Calendar.TUESDAY - 1));
        wednesdaySelector.setDay(Calendar.WEDNESDAY, mAlarm.getRepeatingDay(Calendar.WEDNESDAY - 1));
        thursdaySelector.setDay(Calendar.THURSDAY, mAlarm.getRepeatingDay(Calendar.THURSDAY - 1));
        fridaySelector.setDay(Calendar.FRIDAY, mAlarm.getRepeatingDay(Calendar.FRIDAY - 1));
        saturdaySelector.setDay(Calendar.SATURDAY, mAlarm.getRepeatingDay(Calendar.SATURDAY - 1));
        sundaySelector.setDay(Calendar.SUNDAY, mAlarm.getRepeatingDay(Calendar.SUNDAY - 1));

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = this.getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.loopX_2));
        }*/
    }
}
