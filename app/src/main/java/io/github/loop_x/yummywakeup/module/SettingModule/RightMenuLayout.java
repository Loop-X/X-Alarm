package io.github.loop_x.yummywakeup.module.SettingModule;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import io.github.loop_x.yummywakeup.R;

public class RightMenuLayout extends LinearLayout {

    private Context mContext;

    private AudioManager mAudioManager;

    private SeekBar sbAlarmVolume;
    private SeekBar sbAlarmVibration;
    private ListView lvRingtoneList;

    String[] ringtoneNames = {"WARM BREEZE", "FOREST GLADE", "MORNING MIST", "SUNRISE"};

    public RightMenuLayout(Context context) {
        this(context, null);
    }

    public RightMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RightMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.menu_right, this, true);

        sbAlarmVolume = (SeekBar) findViewById(R.id.sb_alarm_volume);
        sbAlarmVibration = (SeekBar) findViewById(R.id.sb_alarm_vibration);
        lvRingtoneList = (ListView) findViewById(R.id.lv_ringtone_list);

        initVolumeSeekBar();
        initVibrationSeekBar();

        CustomAdapter customAdapter = new CustomAdapter(mContext, ringtoneNames);
        lvRingtoneList.setAdapter(customAdapter);

    }

    private void initVolumeSeekBar() {

        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);

        sbAlarmVolume.setMax(maxVolume);
        sbAlarmVolume.setProgress(currentVolume);
        sbAlarmVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM,
                        progress,
                        AudioManager.FLAG_PLAY_SOUND);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void initVibrationSeekBar() {
        sbAlarmVibration.setMax(1);
    }
}
