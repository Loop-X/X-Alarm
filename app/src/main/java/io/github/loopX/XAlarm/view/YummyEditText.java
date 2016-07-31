package io.github.loopX.XAlarm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import io.github.loopX.XAlarm.tools.TypefaceHelp;


public class YummyEditText extends EditText {

    public YummyEditText(Context context) {
        this(context,null);
        style(context);
    }

    public YummyEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        style(context);
    }

    public YummyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        style(context);
    }
    
    private void style(Context context) {
        setTypeface(TypefaceHelp.get(context,"fonts/BebasNeue.otf"));
    }

}
