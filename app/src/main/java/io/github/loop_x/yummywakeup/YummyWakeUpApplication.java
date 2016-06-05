package io.github.loop_x.yummywakeup;

import android.app.Application;

import io.github.loop_x.yummywakeup.infrastructure.RunTime;


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
