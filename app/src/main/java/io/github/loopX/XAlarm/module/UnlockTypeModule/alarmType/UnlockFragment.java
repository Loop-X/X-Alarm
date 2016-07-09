package io.github.loopX.XAlarm.module.UnlockTypeModule.alarmType;

import io.github.loopX.XAlarm.infrastructure.BaseFragment;

public abstract class UnlockFragment extends BaseFragment {


    public abstract boolean checkUnlockAlarm();

    /**
     * Callback interface to unlock and close alarm
     */
    public interface OnAlarmAction{
        void closeAlarm();
    }
}
