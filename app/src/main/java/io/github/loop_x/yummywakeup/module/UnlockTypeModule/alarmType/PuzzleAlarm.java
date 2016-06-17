package io.github.loop_x.yummywakeup.module.UnlockTypeModule.alarmType;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.module.AlarmModule.AlarmAlertFullScreen;
import io.github.loop_x.yummywakeup.view.PuzzleLayout;

public class PuzzleAlarm extends UnlockFragment {

    private PuzzleLayout puzzleLayout;
    private ImageView ivFlagResult;
    private Button btnCloseAlarm;
    private OnAlarmAction mListener;

    public PuzzleAlarm() {}

    public static PuzzleAlarm newInstance() {
        return new PuzzleAlarm();
    }

    @Override
    public int getLayoutId() { return R.layout.fragment_unlock_puzzle_alarm;}

    @Override
    public void onViewInitial() {
        puzzleLayout = (PuzzleLayout) findViewById(R.id.rl_puzzle);
        ivFlagResult = (ImageView) findViewById(R.id.iv_puzzle_flag_result);
        btnCloseAlarm = (Button) findViewById(R.id.btn_puzzle_close_alarm);
        btnCloseAlarm.setVisibility(View.GONE);

        puzzleLayout.setPuzzleListener(new PuzzleLayout.PuzzleListener() {
            @Override
            public void unlockAlarm() {
                btnCloseAlarm.setVisibility(View.VISIBLE);
                btnCloseAlarm.setEnabled(true);
                ivFlagResult.setBackgroundResource(R.drawable.icon_active);
            }
        });

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean checkUnlockAlarm() {
        return false;
    }

}
