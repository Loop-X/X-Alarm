package io.github.loop_x.yummywakeup.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Author UFreedom
 * Date : 2016 六月 10
 */
public class RightMenuLayout extends LinearLayout {
    
    public RightMenuLayout(Context context) {
        this(context, null);
    }

    public RightMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RightMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //find view and do click listener registering
    }
}
