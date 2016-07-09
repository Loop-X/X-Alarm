package io.github.loopX.XAlarm.tools;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringUtil;

/**
 * Author UFreedom
 * Date : 2016 六月 16
 */
public class BaseSpringListener implements SpringListener {

    public float transition(float progress, float startValue, float endValue) {
        return (float) SpringUtil.mapValueFromRangeToRange(progress, 0, 1, startValue, endValue);
    }
    
    @Override
    public void onSpringUpdate(Spring spring) {
        
    }

    @Override
    public void onSpringAtRest(Spring spring) {

    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }
}
