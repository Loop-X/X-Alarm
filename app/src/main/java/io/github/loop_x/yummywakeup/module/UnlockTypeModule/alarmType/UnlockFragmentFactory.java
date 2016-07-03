package io.github.loop_x.yummywakeup.module.UnlockTypeModule.alarmType;

import io.github.loop_x.yummywakeup.module.UnlockTypeModule.UnlockTypeEnum;

public class UnlockFragmentFactory {

    public static UnlockFragment create(int unlockType) {

        if (unlockType == UnlockTypeEnum.Type.getID()){
            return new TypeAlarm();
        } else if (unlockType == UnlockTypeEnum.Math.getID()){
            return MathAlarm.newInstance();
        } else if (unlockType == UnlockTypeEnum.Puzzle.getID()){
            return PuzzleAlarm.newInstance();
        } else if (unlockType == UnlockTypeEnum.Shake.getID()){
            return ShakeAlarm.newInstance();
        }

        return new TypeAlarm();
    }
}
