package io.github.loop_x.yummywakeup.module.UnlockTypeModule.alarmType;

import io.github.loop_x.yummywakeup.module.UnlockTypeModule.UnlockTypeEnum;

public class UnlockFragmentFactory {

    public static UnlockFragment create(int unlocktype) {

        if (unlocktype == UnlockTypeEnum.Normal.getID()){
            return new NormalAlarm();
        } else if (unlocktype == UnlockTypeEnum.Math.getID()){
            return MathAlarm.newInstance();
        } else if (unlocktype == UnlockTypeEnum.Puzzle.getID()){
            return PuzzleAlarm.newInstance();
        } else if (unlocktype == UnlockTypeEnum.Shake.getID()){
            return ShakeAlarm.newInstance();
        }

        return new NormalAlarm();
    }
}
