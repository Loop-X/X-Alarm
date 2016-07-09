package io.github.loopX.XAlarm;

import android.app.Application;

import io.github.loopX.XAlarm.config.RunTime;


/**
 * Author UFreedom
 * Date : 2015 八月 09
 */
public class YummyWakeUpApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        RunTime.init(this);
    }
}
