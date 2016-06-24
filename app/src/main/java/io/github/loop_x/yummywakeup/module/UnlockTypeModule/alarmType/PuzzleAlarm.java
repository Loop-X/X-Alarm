package io.github.loop_x.yummywakeup.module.UnlockTypeModule.alarmType;

import android.app.Activity;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.module.AlarmModule.AlarmAlertFullScreen;
import io.github.loop_x.yummywakeup.view.PuzzleLayout;

public class PuzzleAlarm extends UnlockFragment {

    private PuzzleLayout puzzleLayout;
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

        puzzleLayout.setPuzzleListener(new PuzzleLayout.PuzzleListener() {
            @Override
            public void unlockAlarm() {
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
        mListener = (OnAlarmAction) activity;
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

}
