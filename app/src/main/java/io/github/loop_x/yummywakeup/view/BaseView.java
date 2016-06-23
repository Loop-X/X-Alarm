package io.github.loop_x.yummywakeup.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author UFreedom
 * Date : 2016 六月 23
 */
public abstract  class BaseView extends View implements ViewPainter{
    
    
    public BaseView(Context context) {
        this(context,null);
    }

    public BaseView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInitial();

    }
    
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        onPrepareDraw();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw(canvas);
    }
}
