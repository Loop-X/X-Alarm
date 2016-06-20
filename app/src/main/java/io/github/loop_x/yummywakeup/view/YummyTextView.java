package io.github.loop_x.yummywakeup.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


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
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/BebasNeue.otf");
        setTypeface(tf);
    }

}
