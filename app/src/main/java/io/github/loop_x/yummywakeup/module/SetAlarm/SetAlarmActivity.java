package io.github.loop_x.yummywakeup.module.SetAlarm;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.infrastructure.BaseActivity;
import io.github.loop_x.yummywakeup.module.AlarmModule.Alarms;
import io.github.loop_x.yummywakeup.module.AlarmModule.model.Alarm;
import io.github.loop_x.yummywakeup.module.AlarmModule.model.DaysOfWeek;
import io.github.loop_x.yummywakeup.view.YummyTextView;
import io.github.loop_x.yummywakeup.view.YummyTimePicker;

public class SetAlarmActivity extends BaseActivity {

    private Alarm mAlarm;

    private YummyTimePicker timePickerHour;
    private YummyTimePicker timePickerMinute;
    private YummyTimePicker timePickerAMPM;

    private Button btnMON;
    private Button btnTUE;
    private Button btnWED;
    private Button btnTHU;
    private Button btnFRI;
    private Button btnSAT;
    private Button btnSUN;

    private YummyTextView tvMON;
    private YummyTextView tvTUE;
    private YummyTextView tvWED;
    private YummyTextView tvTHU;
    private YummyTextView tvFRI;
    private YummyTextView tvSAT;
    private YummyTextView tvSUN;

    private ImageView btnAccept;

    private YummyTextView tvCurrentAlarmTime;
    private YummyTextView tvCurrentAlarmAMPM;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_alarm;
    }

    @Override
    public void onViewInitial() {

        /** Get alarm object from intent **/

        mAlarm = getIntent().getParcelableExtra(Alarms.ALARM_INTENT_EXTRA);

        /** Init top bar **/

        tvCurrentAlarmTime = (YummyTextView) findViewById(R.id.tv_set_alarm_current_alarm_time);
        tvCurrentAlarmAMPM = (YummyTextView) findViewById(R.id.tv_set_alarm_current_alarm_ampm);

        tvCurrentAlarmTime.setText(mAlarm.hour + ":" + mAlarm.minutes);

        // ToDo 12 / 24
        if(mAlarm.hour > 12) {
            tvCurrentAlarmAMPM.setText("PM");
        } else {
            tvCurrentAlarmAMPM.setText("AM");
        }

        /** Init Time Picker **/

        timePickerHour = (YummyTimePicker) findViewById(R.id.tp_set_alarm_hour);
        timePickerMinute = (YummyTimePicker) findViewById(R.id.tp_set_alarm_minute);
        timePickerAMPM = (YummyTimePicker) findViewById(R.id.tp_set_alarm_am_pm);

        timePickerHour.setHour();
        timePickerMinute.setMinute();
        timePickerAMPM.setAMPM();

        timePickerHour.setSelected("" + mAlarm.hour);
        timePickerMinute.setSelected("" + mAlarm.minutes);

        // ToDo 12 / 24
        if(mAlarm.hour > 12) {
            timePickerAMPM.setSelected("PM");
        }

        timePickerHour.setOnSelectListener(new YummyTimePicker.onSelectListener() {
            @Override
            public void onSelect(String text) {
                mAlarm.hour = Integer.valueOf(text);
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

            }
        });

        btnMON = (Button) findViewById(R.id.btn_monday);
        btnTUE = (Button) findViewById(R.id.btn_tuesday);
        btnWED = (Button) findViewById(R.id.btn_wednesday);
        btnTHU = (Button) findViewById(R.id.btn_thursday);
        btnFRI = (Button) findViewById(R.id.btn_friday);
        btnSAT = (Button) findViewById(R.id.btn_saturday);
        btnSUN = (Button) findViewById(R.id.btn_sunday);

        tvMON = (YummyTextView) findViewById(R.id.tv_monday);
        tvTUE = (YummyTextView) findViewById(R.id.tv_tuesday);
        tvWED = (YummyTextView) findViewById(R.id.tv_wednesday);
        tvTHU = (YummyTextView) findViewById(R.id.tv_thursday);
        tvFRI = (YummyTextView) findViewById(R.id.tv_friday);
        tvSAT = (YummyTextView) findViewById(R.id.tv_saturday);
        tvSUN = (YummyTextView) findViewById(R.id.tv_sunday);

        btnAccept = (ImageView) findViewById(R.id.im_set_alarm_accept);

        initRepeat();

    }


    @Override
    public void onRefreshData() {

    }

    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.im_set_alarm_accept:
                Intent intent = new Intent();
                intent.putExtra(Alarms.ALARM_INTENT_EXTRA, mAlarm);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }

    }

    public void setRepeat(View view) {

        if (view.isActivated()) {
            view.setActivated(false);
        } else {
            view.setActivated(true);
        }

        switch (view.getId()) {
            case R.id.btn_monday:
                mAlarm.daysOfWeek.set(DaysOfWeek.MONDAY, view.isActivated());
                break;
            case R.id.btn_tuesday:
                mAlarm.daysOfWeek.set(DaysOfWeek.TUESDAY, view.isActivated());
                break;
            case R.id.btn_wednesday:
                mAlarm.daysOfWeek.set(DaysOfWeek.WEDNESDAY, view.isActivated());
                break;
            case R.id.btn_thursday:
                mAlarm.daysOfWeek.set(DaysOfWeek.THURSDAY, view.isActivated());
                break;
            case R.id.btn_friday:
                mAlarm.daysOfWeek.set(DaysOfWeek.FRIDAY, view.isActivated());
                break;
            case R.id.btn_saturday:
                mAlarm.daysOfWeek.set(DaysOfWeek.SATURDAY, view.isActivated());
                break;
            case R.id.btn_sunday:
                mAlarm.daysOfWeek.set(DaysOfWeek.SUNDAY, view.isActivated());
                break;
            default:
                break;
        }
    }

    private void initRepeat() {
        if (mAlarm.daysOfWeek.isRepeatSet()) {
            btnMON.setActivated(mAlarm.daysOfWeek.isSet(DaysOfWeek.MONDAY));
            btnTUE.setActivated(mAlarm.daysOfWeek.isSet(DaysOfWeek.TUESDAY));
            btnWED.setActivated(mAlarm.daysOfWeek.isSet(DaysOfWeek.WEDNESDAY));
            btnTHU.setActivated(mAlarm.daysOfWeek.isSet(DaysOfWeek.THURSDAY));
            btnFRI.setActivated(mAlarm.daysOfWeek.isSet(DaysOfWeek.FRIDAY));
            btnSAT.setActivated(mAlarm.daysOfWeek.isSet(DaysOfWeek.SATURDAY));
            btnSUN.setActivated(mAlarm.daysOfWeek.isSet(DaysOfWeek.SUNDAY));

            int colorActiv = ContextCompat.getColor(this, R.color.loopX_3);
            int colorNoActiv = ContextCompat.getColor(this, R.color.loopX_6);

            tvMON.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.MONDAY) ? colorActiv : colorNoActiv);
            tvTUE.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.TUESDAY) ? colorActiv : colorNoActiv);
            tvWED.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.WEDNESDAY) ? colorActiv : colorNoActiv);
            tvTHU.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.THURSDAY) ? colorActiv : colorNoActiv);
            tvFRI.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.FRIDAY) ? colorActiv : colorNoActiv);
            tvSAT.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.SATURDAY) ? colorActiv : colorNoActiv);
            tvSUN.setTextColor(mAlarm.daysOfWeek.isSet(DaysOfWeek.SUNDAY) ? colorActiv : colorNoActiv);
        }
    }
}
