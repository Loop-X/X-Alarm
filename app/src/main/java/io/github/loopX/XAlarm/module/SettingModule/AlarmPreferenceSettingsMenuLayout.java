package io.github.loopX.XAlarm.module.SettingModule;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import com.kyleduo.switchbutton.SwitchButton;

import io.github.loopX.XAlarm.R;
import io.github.loopX.XAlarm.XAlarmApp;
import io.github.loopX.XAlarm.module.Alarm.AlarmRingtonePlayer;

public class AlarmPreferenceSettingsMenuLayout extends LinearLayout {

    private Context mContext;

    private AudioManager mAudioManager;

    private SeekBar sbAlarmVolume;
    private SwitchButton sbAlarmVibration;
    private ListView lvRingtoneList;
    private RingtoneAdapter mAdapter;

    private RingtoneManager mRingtoneManager;
    private AlarmRingtonePlayer mAlarmRingtonePlayer;
    private Ringtone mRingtone;

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
        sbAlarmVibration = (SwitchButton) findViewById(R.id.sb_alarm_vibration);

        initVolumeSeekBar();

        /** Init Vibration **/

        sbAlarmVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sbAlarmVibration.performHapticFeedback(
                            HapticFeedbackConstants.LONG_PRESS,
                            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                } else {
                    // The toggle is disabled
                }
            }
        });

        /** Init Ringtone **/

        mAlarmRingtonePlayer = new AlarmRingtonePlayer(XAlarmApp.getAppContext());

        lvRingtoneList = (ListView) findViewById(R.id.lv_ringtone_list);

        mAdapter = new RingtoneAdapter(mContext, R.layout.ringtone_list_item);
        lvRingtoneList.setAdapter(mAdapter);
        lvRingtoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Uri uri = Uri.parse(XAlarmApp.getResourcePath() + "/raw/ringtone_" + i);

                if (mAdapter.getSelectedPosition() != i) {
                    mAlarmRingtonePlayer.stop();
                    mAlarmRingtonePlayer.play(uri);

                    mAdapter.setSelectedPosition(i);
                    mAdapter.notifyDataSetChanged();
                } else {
                    if(mAlarmRingtonePlayer.isPlaying()) {
                        mAlarmRingtonePlayer.stop();
                    } else {
                        mAlarmRingtonePlayer.play(uri);
                    }
                }
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
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopRingtone();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void stopRingtone() {
        mAlarmRingtonePlayer.stop();
    }

    public void setInitRingtone(int i) {
        mAdapter.setSelectedPosition(i);
        mAdapter.notifyDataSetChanged();
    }

    public int getRingtone(){
        return mAdapter.getSelectedPosition();
    }

    public String getRingtoneName(int position) {
        return mAdapter.getRingtoneName(position);
    }
    
    public void setInitVibration(boolean isVibration) {
        sbAlarmVibration.setChecked(isVibration);
    }

    public boolean getVibrationSetting() {
        return sbAlarmVibration.isChecked();
    }

   

}
