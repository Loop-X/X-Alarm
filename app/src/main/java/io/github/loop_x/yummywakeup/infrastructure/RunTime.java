package io.github.loop_x.yummywakeup.infrastructure;

import android.content.Context;

/**
 * Author SunMeng
 * Date : 2015 八月 09
 */
public class RunTime {
    
    private static Context context;

    public static void init(Context context){
        RunTime.context = context;
    }

    public static Context getApp() {
        if (context == null) {
            throw new IllegalAccessError("RT init error!");
        }
        return context;
    }
}
