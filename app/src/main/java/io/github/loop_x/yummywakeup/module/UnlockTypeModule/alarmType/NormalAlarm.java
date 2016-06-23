package io.github.loop_x.yummywakeup.module.UnlockTypeModule.alarmType;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.module.AlarmModule.AlarmAlertFullScreen;

public class NormalAlarm extends UnlockFragment {

    private Button btnCloseAlarm;
    private OnAlarmAction mListener;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_unlock_normal_alarm;
    }

    @Override
    public void onViewInitial() {
        btnCloseAlarm = (Button) findViewById(R.id.btn_close_alarm);
    }

    @Override
    public void onRefreshData() {
        btnCloseAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.closeAlarm();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnAlarmAction) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean checkUnlockAlarm() {
        // Nothing to do for normal alarm
        return true;
    }
}
