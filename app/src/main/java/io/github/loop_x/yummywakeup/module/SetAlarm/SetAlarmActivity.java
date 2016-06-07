package io.github.loop_x.yummywakeup.module.SetAlarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.infrastructure.BaseActivity;
import io.github.loop_x.yummywakeup.module.AlarmModule.Alarms;
import io.github.loop_x.yummywakeup.module.AlarmModule.model.Alarm;
import io.github.loop_x.yummywakeup.view.YummyTextView;
import io.github.loop_x.yummywakeup.view.YummyTimePicker;

public class SetAlarmActivity extends BaseActivity {

    private Alarm mAlarm;

    private YummyTimePicker timePickerHour;
    private YummyTimePicker timePickerMinute;
    private YummyTimePicker timePickerAMPM;

    private Button btnMonday;
    private Button btnTuesday;
    private Button btnWednesday;
    private Button btnThursday;
    private Button btnFriday;
    private Button btnSaturday;
    private Button btnSunday;

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

        btnMonday = (Button) findViewById(R.id.btn_monday);
        btnTuesday = (Button) findViewById(R.id.btn_tuesday);
        btnWednesday = (Button) findViewById(R.id.btn_wednesday);
        btnThursday = (Button) findViewById(R.id.btn_thursday);
        btnFriday = (Button) findViewById(R.id.btn_friday);
        btnSaturday = (Button) findViewById(R.id.btn_saturday);
        btnSunday = (Button) findViewById(R.id.btn_sunday);

        btnAccept = (ImageView) findViewById(R.id.im_set_alarm_accept);

    }


    @Override
    public void onRefreshData() {

    }

    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.im_set_alarm_accept:
                Intent intent = new Intent();
                intent.putExtra("new_hour", mAlarm.hour);
                intent.putExtra("new_minute", mAlarm.minutes);
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
    }

}
