package io.github.loopX.XAlarm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import io.github.loopX.XAlarm.tools.TypefaceHelp;


public class YummyTextView extends TextView {

    public YummyTextView(Context context) {
        this(context,null);
        style(context);
    }

    public YummyTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        style(context);
    }

    public YummyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        style(context);
    }
    
    private void style(Context context) {
        setTypeface(TypefaceHelp.get(context,"fonts/BebasNeue.otf"));
    }

}
