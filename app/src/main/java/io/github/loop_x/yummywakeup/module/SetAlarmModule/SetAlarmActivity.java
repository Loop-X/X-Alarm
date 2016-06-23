package io.github.loop_x.yummywakeup.module.SetAlarmModule;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.rebound.SpringUtil;

import java.util.Calendar;

import io.github.loop_x.yummywakeup.MainActivity;
import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.UIUtils;
import io.github.loop_x.yummywakeup.infrastructure.BaseActivity;
import io.github.loop_x.yummywakeup.module.AlarmModule.Alarms;
import io.github.loop_x.yummywakeup.module.AlarmModule.model.Alarm;
import io.github.loop_x.yummywakeup.module.AlarmModule.model.DaysOfWeek;
import io.github.loop_x.yummywakeup.view.RippleBackgroundView;
import io.github.loop_x.yummywakeup.view.YummyTextView;
import io.github.loop_x.yummywakeup.view.YummyTimePicker;

public class SetAlarmActivity extends BaseActivity {

    private Alarm mAlarm;

    private YummyTimePicker timePickerHour;
    private YummyTimePicker timePickerMinute;
    private YummyTimePicker timePickerAMPM;

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
    private Boolean isOn;
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

        is24hMode = Alarms.get24HourMode(this);

        /** Get alarm object from intent **/

        mAlarm = getIntent().getParcelableExtra(Alarms.ALARM_INTENT_EXTRA);
        isOn = mAlarm.enabled;

        /** Init top bar **/

        tvCurrentAlarmTime = (YummyTextView) findViewById(R.id.tv_set_alarm_current_alarm_time);
        tvCurrentAlarmAMPM = (YummyTextView) findViewById(R.id.tv_set_alarm_current_alarm_ampm);
        btnSwitchOnOffAlarm = (ImageView) findViewById(R.id.iv_switch_on_off_alarm);
        setAlarmTopBarView =  findViewById(R.id.rl_set_alarm_top_bar);
        rippleBackgroundView  = (RippleBackgroundView) findViewById(R.id.rippleBackground);

        maxRippleRadius = (int) Math.sqrt(UIUtils.getScreenWidth() * UIUtils.getScreenWidth() + UIUtils.dip2px(156) * UIUtils.dip2px(156));
        ripplePivotY = UIUtils.dip2px(156);
        ripplePivotX = UIUtils.getScreenWidth() - UIUtils.dip2px(100) / 2 - UIUtils.dip2px(16);
        
        
        if (isOn) {
            btnSwitchOnOffAlarm.setImageResource(R.drawable.switch_off);
            rippleBackgroundView.setBackgroundResource(R.color.alarm_set_top_alarm_on);

        } else {
            btnSwitchOnOffAlarm.setImageResource(R.drawable.switch_on);
            rippleBackgroundView.setBackgroundResource(R.color.alarm_set_top_alarm_off);
        }

        String hour = "";
        String min  = (mAlarm.minutes < 10 ? "0" : "") + mAlarm.minutes;
        String ampn = "";

        final Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, mAlarm.hour);
        cal.set(Calendar.MINUTE, mAlarm.minutes);

        tvCurrentAlarmTime.setText(DateFormat.format(is24hMode ? MainActivity.M24 : MainActivity.M12, cal));
        tvCurrentAlarmAMPM.setText(DateFormat.format(is24hMode ? "" : "a", cal));

        /** Init Time Picker **/

        timePickerHour = (YummyTimePicker) findViewById(R.id.tp_set_alarm_hour);
        timePickerMinute = (YummyTimePicker) findViewById(R.id.tp_set_alarm_minute);
        timePickerAMPM = (YummyTimePicker) findViewById(R.id.tp_set_alarm_am_pm);

        timePickerHour.setHour(is24hMode);
        timePickerMinute.setMinute();

        if(!is24hMode) {
            timePickerAMPM.setVisibility(View.VISIBLE);
            timePickerAMPM.setAMPM();
            if(mAlarm.hour <= 12) {
                timePickerAMPM.setSelected("AM");
                timePickerHour.setSelected(mAlarm.hour);
                AMPM = "AM";
            } else {
                timePickerAMPM.setSelected("PM");
                timePickerHour.setSelected(mAlarm.hour - 12);
                AMPM = "PM";
            }
        } else {
            timePickerAMPM.setVisibility(View.INVISIBLE);
            timePickerHour.setSelected(mAlarm.hour);
        }

        timePickerMinute.setSelected("" + mAlarm.minutes);

        timePickerHour.setOnSelectListener(new YummyTimePicker.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if(is24hMode) {
                    mAlarm.hour = Integer.valueOf(text);
                } else {
                    if(AMPM.equals("AM")) {
                        mAlarm.hour = Integer.valueOf(text);
                    } else {
                        mAlarm.hour = Integer.valueOf(text) + 12;
                        if(mAlarm.hour == 24) {
                            mAlarm.hour = 0;
                        }
                    }
                }
            }
        });

        timePickerMinute.setOnSelectListener(new YummyTimePicker.onSelectListener() {
            @Override
            public void onSelect(String text) {
                mAlarm.minutes = Integer.valueOf(text);
            }
        });

        timePickerAMPM.setOnSelectListener(new YummyTimePicker.onSelectListener() {
            @Override
            public void onSelect(String text) {
                AMPM = text;
                if(AMPM.equals("PM")) {
                    mAlarm.hour = mAlarm.hour + 12;
                    if(mAlarm.hour == 24) {
                        mAlarm.hour = 0;
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

    DayOfWeekSelectorView.DayOfWeekSelectorListener dayOfWeekSelectorListener = new DayOfWeekSelectorView.DayOfWeekSelectorListener() {
        @Override
        public void onDayOfWeekSelector(int dayOfWeek, boolean selected) {
            mAlarm.daysOfWeek.set(dayOfWeek,selected);
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
                if(isOn) {
                    btnSwitchOnOffAlarm.setImageResource(R.drawable.switch_on);
                    doAlarmOffRippleAnimation();

                
                    Toast.makeText(this, R.string.turn_off_alarm, Toast.LENGTH_SHORT).show();
                    mAlarm.enabled = false;
                    isOn = false;
                } else {
                    btnSwitchOnOffAlarm.setImageResource(R.drawable.switch_off);
                    doAlarmOnRippleAnimation();
                    Toast.makeText(this, R.string.turn_on_alarm, Toast.LENGTH_SHORT).show();
                    mAlarm.enabled = true;
                    isOn = true;
                }
                break;
            case R.id.im_set_alarm_accept:
                Intent intent = new Intent();
                intent.putExtra(Alarms.ALARM_INTENT_EXTRA, mAlarm);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }

    }


    private void initRepeat() {
        if (mAlarm.daysOfWeek.isRepeatSet()) {

            int colorActiv = ContextCompat.getColor(this, R.color.loopX_3);
            int colorNoActiv = ContextCompat.getColor(this, R.color.loopX_6);

            tvMON.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.MONDAY) ? colorActiv : colorNoActiv);
            tvTUE.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.TUESDAY) ? colorActiv : colorNoActiv);
            tvWED.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.WEDNESDAY) ? colorActiv : colorNoActiv);
            tvTHU.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.THURSDAY) ? colorActiv : colorNoActiv);
            tvFRI.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.FRIDAY) ? colorActiv : colorNoActiv);
            tvSAT.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.SATURDAY) ? colorActiv : colorNoActiv);
            tvSUN.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.SUNDAY) ? colorActiv : colorNoActiv);

            mondaySelectorView.setDay(DaysOfWeek.MONDAY,mAlarm.daysOfWeek.isSet(DaysOfWeek.MONDAY));
            tuesdaySelector.setDay(DaysOfWeek.TUESDAY,mAlarm.daysOfWeek.isSet(DaysOfWeek.TUESDAY));
            wednesdaySelector.setDay(DaysOfWeek.WEDNESDAY,mAlarm.daysOfWeek.isSet(DaysOfWeek.WEDNESDAY));
            thursdaySelector.setDay(DaysOfWeek.THURSDAY,mAlarm.daysOfWeek.isSet(DaysOfWeek.THURSDAY));
            fridaySelector.setDay(DaysOfWeek.FRIDAY,mAlarm.daysOfWeek.isSet(DaysOfWeek.FRIDAY));
            saturdaySelector.setDay(DaysOfWeek.SATURDAY,mAlarm.daysOfWeek.isSet(DaysOfWeek.SATURDAY));
            sundaySelector.setDay(DaysOfWeek.SUNDAY,mAlarm.daysOfWeek.isSet(DaysOfWeek.SUNDAY));
            
        }
    }
}
