package io.github.loop_x.yummywakeup.module.UnlockTypeModule.alarmType;

import io.github.loop_x.yummywakeup.infrastructure.BaseFragment;

public abstract class UnlockFragment extends BaseFragment {


    public abstract boolean checkUnlockAlarm();

    /**
     * Callback interface to unlock and close alarm
     */
    public interface OnAlarmAction{

        void closeAlarm();
    }
}
