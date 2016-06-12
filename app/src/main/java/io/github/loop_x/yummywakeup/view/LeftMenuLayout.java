package io.github.loop_x.yummywakeup.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import io.github.loop_x.yummywakeup.R;

/**
 * Author UFreedom
 * Date : 2016 六月 10
 */
public class LeftMenuLayout extends LinearLayout {
    
    public LeftMenuLayout(Context context) {
        this(context, null);
    }

    public LeftMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LeftMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.menu_left,this,true);
        //find view and do click listener registering
    }
}
