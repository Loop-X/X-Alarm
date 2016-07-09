package io.github.loopX.XAlarm.module.UnlockTypeModule.alarmType;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class WaterExpandAnimation extends Animation{

    int targetHeight;
    int initialHeight;
    View view;

    public WaterExpandAnimation(View view, int targetHeight) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.initialHeight = view.getHeight();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        int newHeight;

        newHeight = this.initialHeight
                + (int) ((this.targetHeight - this.initialHeight) * interpolatedTime);

        view.getLayoutParams().height = newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

}
