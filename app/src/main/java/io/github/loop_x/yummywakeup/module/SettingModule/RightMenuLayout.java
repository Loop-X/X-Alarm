package io.github.loop_x.yummywakeup.module.SettingModule;

import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.module.AlarmModule.model.Alarm;
import io.github.loop_x.yummywakeup.view.YummyTextView;

public class RightMenuLayout extends LinearLayout {

    private Context mContext;

    private AudioManager mAudioManager;

    private SeekBar sbAlarmVolume;
    private SeekBar sbAlarmVibration;
    private ListView lvRingtoneList;
    private YummyTextView tvRingtoneItem;
    private CheckBox cbRingtoneItem;

    private RingtoneManager mRingtoneManager;
    private int mRingtonePosition;


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

        /** Init Ringtone **/
        mRingtoneManager = new RingtoneManager(mContext);

        CustomAdapter customAdapter = new CustomAdapter(mContext, R.layout.ringtone_list_item);
        lvRingtoneList.setAdapter(customAdapter);

        lvRingtoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                tvRingtoneItem = (YummyTextView) view.findViewById(R.id.tv_ringtone_list_item);
                cbRingtoneItem = (CheckBox) view.findViewById(R.id.cb_ringtone_list_item);

                cbRingtoneItem.setChecked(true);

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
