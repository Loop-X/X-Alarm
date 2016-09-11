package io.github.loopX.XAlarm;

import android.app.Application;
import android.content.Context;

import com.tendcloud.tenddata.TCAgent;


/**
 * Specialization of the Application class to enable:
 *      Application context access from non-Android framework classes
 *      Package Name access from non-Android framework classes
 *      Resource Path access from non-Android framework classes
 */
public class XAlarmApp extends Application{

    private static Context mContext;
    private static String mPackageName;

    public static Context getAppContext() {
        return XAlarmApp.mContext;
    }

    public static String getPackagePath() {
        return XAlarmApp.mPackageName;
    }

    public static String getResourcePath() {
        return "android.resource://" + XAlarmApp.mPackageName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        XAlarmApp.mContext = getApplicationContext();
        XAlarmApp.mPackageName = XAlarmApp.mContext.getPackageName();

        if (!BuildConfig.DEBUG) {
            TCAgent.LOG_ON = true;
            TCAgent.init(this, BuildConfig.TALKING_DATA_ID, BuildConfig.CHANNEL);
            TCAgent.setReportUncaughtExceptions(true);
        }
    }
}
