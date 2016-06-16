package io.github.loop_x.yummywakeup.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Author UFreedom
 * Date : 2016 六月 05
 */
public class TouchEventProcessLayout extends RelativeLayout{

    private DragMenuLayout loopXDragMenuLayout;


    public TouchEventProcessLayout(Context context) {
        super(context);
    }

    public TouchEventProcessLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchEventProcessLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDragMenuLayout(DragMenuLayout loopXDragMenuLayout) {
        this.loopXDragMenuLayout = loopXDragMenuLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (loopXDragMenuLayout.getMenuStatus() != DragMenuLayout.MenuStatus.Close) {
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (loopXDragMenuLayout.getMenuStatus() != DragMenuLayout.MenuStatus.Close.Close) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                loopXDragMenuLayout.openLeftMenuWithAnimation();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

}
