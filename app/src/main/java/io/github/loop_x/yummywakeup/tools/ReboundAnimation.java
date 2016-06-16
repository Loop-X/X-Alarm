package io.github.loop_x.yummywakeup.tools;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

/**
 * Author UFreedom
 * Date : 2016 六月 16
 */
public class ReboundAnimation {

    private SpringSystem mSpringSystem; 
    
    public Spring createSpringFromBouncinessAndSpeed(double bounciness, double speed, SpringListener springListener){
        return mSpringSystem.createSpring().setSpringConfig(
                SpringConfig.fromBouncinessAndSpeed(bounciness,speed))
                .addListener(springListener);
    }
    
    private ReboundAnimation(){
        mSpringSystem = SpringSystem.create();
    }
    
    public static ReboundAnimation getInstance(){
        return SingletonCreator.sInstance;
    }
    
    private static class SingletonCreator{
        private static ReboundAnimation sInstance = new ReboundAnimation();
    }
    
    
    
}
