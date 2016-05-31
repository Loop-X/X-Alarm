package io.github.loop_x.yummywakeup.infrastructure;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;


public class YummyTextView extends TextView {

    public YummyTextView(Context context) {
        super(context);
        style(context);
    }

    public YummyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context);
    }

    public YummyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        style(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public YummyTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        style(context);
    }

    private void style(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/BebasNeue.otf");
        setTypeface(tf);
    }

}
