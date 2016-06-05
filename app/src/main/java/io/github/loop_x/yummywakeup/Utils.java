package io.github.loop_x.yummywakeup;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import io.github.loop_x.yummywakeup.infrastructure.RunTime;

/**
 * Author UFreedom
 * Date : 2016 六月 05
 */
public class Utils {

    public static int getScreenWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) RunTime.getApp().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;

    }
}
