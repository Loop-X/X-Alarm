package io.github.loopX.XAlarm;

import android.app.Application;
import android.content.Context;


/**
 * Specialization of the Application class to enable:
 *      Application context access from non-Android framework classes
 */
public class XAlarmApp extends Application{

    private static Context mContext;

    public static Context getAppContext() {
        return XAlarmApp.mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        XAlarmApp.mContext = getApplicationContext();
    }
}
