package io.github.loopX.XAlarm.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.SimpleArrayMap;

/*
    Each call to Typeface.createFromAsset will load a new instance of the typeface into memory,
    and this memory is not consistently get garbage collected
    http://code.google.com/p/android/issues/detail?id=9904
    (It states released but even on Lollipop you can see the typefaces accumulate even after
    multiple GC passes)

    You can detect this by running:
    adb shell dumpsys meminfo com.your.packagenage

    You will see output like:

     Asset Allocations
        zip:/data/app/com.your.packagenage-1.apk:/assets/Roboto-Medium.ttf: 125K
        zip:/data/app/com.your.packagenage-1.apk:/assets/Roboto-Medium.ttf: 125K
        zip:/data/app/com.your.packagenage-1.apk:/assets/Roboto-Medium.ttf: 125K
        zip:/data/app/com.your.packagenage-1.apk:/assets/Roboto-Regular.ttf: 123K
        zip:/data/app/com.your.packagenage-1.apk:/assets/Roboto-Medium.ttf: 125K
*/
/**
 * 优化字体资源获取方式，每次获取字体时先从缓存中拿，如果没法命中再去创建
 *
 * Author UFreedom
 * 
 */
public class TypefaceHelp {

    private static final SimpleArrayMap<String, Typeface> CACHE = new SimpleArrayMap<String, Typeface>();

    public static Typeface get(Context context, String name) {
        synchronized (CACHE) {
            if (!CACHE.containsKey(name)) {
                Typeface t = Typeface.createFromAsset(context.getAssets(), name);
                CACHE.put(name, t);
            }
            return CACHE.get(name);
        }
    }
}
