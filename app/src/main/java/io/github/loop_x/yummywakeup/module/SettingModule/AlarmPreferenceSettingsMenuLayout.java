package io.github.loop_x.yummywakeup.module.SettingModule;

import android.content.Context;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import io.github.loop_x.yummywakeup.R;

public class AlarmPreferenceSettingsMenuLayout extends LinearLayout {

    private Context mContext;

    private AudioManager mAudioManager;

    private SeekBar sbAlarmVolume;
    private SeekBar sbAlarmVibration;
    private ListView lvRingtoneList;

    private RingtoneManager mRingtoneManager;

    public AlarmPreferenceSettingsMenuLayout(Context context) {
        this(context, null);
    }

    public AlarmPreferenceSettingsMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlarmPreferenceSettingsMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.menu_right, this, true);

        /** Init Seekbar **/

        sbAlarmVolume = (SeekBar) findViewById(R.id.sb_alarm_volume);
        sbAlarmVibration = (SeekBar) findViewById(R.id.sb_alarm_vibration);

        initVolumeSeekBar();
        initVibrationSeekBar();

        /** Init Ringtone **/

        lvRingtoneList = (ListView) findViewById(R.id.lv_ringtone_list);
        mRingtoneManager = new RingtoneManager(mContext);

        final CustomAdapter customAdapter = new CustomAdapter(mContext, R.layout.ringtone_list_item);
        lvRingtoneList.setAdapter(customAdapter);

        lvRingtoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CustomAdapter.mLastSelectPosition = i;
                customAdapter.notifyDataSetChanged();

            }
        });

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
