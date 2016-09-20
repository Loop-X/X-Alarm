package io.github.loopX.XAlarm;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tendcloud.tenddata.TCAgent;


/**
 * Specialization of the Application class to enable:
 * Application context access from non-Android framework classes
 * Package Name access from non-Android framework classes
 * Resource Path access from non-Android framework classes
 */
public class XAlarmApp extends Application {

    private static final String TAG = "XAlarmApp";
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
            try {
                ApplicationInfo appInfo = getPackageManager()
                        .getApplicationInfo(getPackageName(),
                                PackageManager.GET_META_DATA);
                String appId = appInfo.metaData.getString("TD_APP_ID");
                if(appId != null && !"".equals(appId)){
                    TCAgent.LOG_ON = true;
                    TCAgent.init(this, appId, BuildConfig.CHANNEL);
                    TCAgent.setReportUncaughtExceptions(true);
                    Log.e(TAG, "talking data app id:" + appId);
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Can not get talking data app id");
                e.printStackTrace();
            }
        }
    }
}
