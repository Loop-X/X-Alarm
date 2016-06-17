package io.github.loop_x.yummywakeup.module.UnlockTypeModule.alarmType;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.module.AlarmModule.AlarmAlertFullScreen;

public class ShakeAlarm extends UnlockFragment implements SensorEventListener{

    private TextView tvShakeTime;
    private ImageView ivFlagResult;
    private Button btnCloseAlarm;

    private OnAlarmAction mListener;
    private SensorManager mSensorManager;
    private int shakeTimes;

    private int mShakeCount;
    private long mShakeTimestamp;
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_STOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    public ShakeAlarm() {}

    public static ShakeAlarm newInstance() {
        return new ShakeAlarm();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AlarmAlertFullScreen) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_unlock_shake_alarm;
    }

    @Override
    public void onViewInitial() {
        tvShakeTime = (TextView) findViewById(R.id.tv_shake_time);
        ivFlagResult = (ImageView) findViewById(R.id.iv_shake_flag_result);
        btnCloseAlarm = (Button) findViewById(R.id.btn_shake_close_alarm);
        btnCloseAlarm.setVisibility(View.GONE);
        shakeTimes = 10;
        tvShakeTime.setText("Please shake " + shakeTimes + " times");

        mSensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);

        mShakeTimestamp = System.currentTimeMillis();

        btnCloseAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.closeAlarm();
            }
        });
    }

    @Override
    public void onRefreshData() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean checkUnlockAlarm() {
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        // gForce will be close to 1 when there is no movement.
        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            final long currentTime = System.currentTimeMillis();
            // ignore shake events too close to each other (500ms)
            if (mShakeTimestamp + SHAKE_STOP_TIME_MS > currentTime ) {
                return;
            }
            // reset the shake count after 3 seconds of no shakes
            if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < currentTime ) {
                mShakeCount = 0;
            }

            mShakeTimestamp = currentTime;
            mShakeCount++;

            if(mShakeCount == shakeTimes) {
                btnCloseAlarm.setVisibility(View.VISIBLE);
                btnCloseAlarm.setEnabled(true);
                ivFlagResult.setBackgroundResource(R.drawable.icon_active);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }
}
