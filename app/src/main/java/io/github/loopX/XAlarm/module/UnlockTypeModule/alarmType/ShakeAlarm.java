package io.github.loopX.XAlarm.module.UnlockTypeModule.alarmType;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.john.waveview.WaveView;

import java.util.Timer;
import java.util.TimerTask;

import io.github.loopX.XAlarm.R;
import io.github.loopX.XAlarm.tools.ToastMaster;
import io.github.loopX.XAlarm.view.YummyTextView;

public class ShakeAlarm extends UnlockFragment implements SensorEventListener{

    private YummyTextView tvShakeProgress;
    private WaveView ivWater;

    private OnAlarmAction mListener;
    private SensorManager mSensorManager;

    private int mShakeCount;
    private long mShakeTimestamp;
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.5F;
    private static final int SHAKE_STOP_TIME_MS = 300;

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            mListener.closeAlarm();
        }
    };

    public ShakeAlarm() {}

    public static ShakeAlarm newInstance() {
        return new ShakeAlarm();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnAlarmAction) activity;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_unlock_shake_alarm;
    }

    @Override
    public void onViewInitial() {

        tvShakeProgress = (YummyTextView) findViewById(R.id.tv_shake_progress);
        ivWater = (WaveView) findViewById(R.id.iv_water);
        ivWater.setProgress(0);

        mSensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);

        mShakeTimestamp = System.currentTimeMillis();

    }

    @Override
    public void onRefreshData() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.closeAlarm();
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

            mShakeTimestamp = currentTime;

            mShakeCount += (int) gForce * 1.5;

            if(mShakeCount >= 100) {
                mShakeCount = 100;
            }

            ivWater.setProgress(mShakeCount);
            tvShakeProgress.setText(mShakeCount + "%");

            if(mShakeCount == 100) {
                ToastMaster.setToast(Toast.makeText(getActivity(),
                        getString(R.string.puzzle_complete),
                        Toast.LENGTH_SHORT));
                ToastMaster.showToast();

                Timer timer = new Timer(true);
                timer.schedule(task, 1200);
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
