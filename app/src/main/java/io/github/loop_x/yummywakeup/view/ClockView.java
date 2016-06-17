package io.github.loop_x.yummywakeup.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.UIUtils;

/**
 * Author UFreedom
 * Date : 2016 六月 17
 */
public class ClockView extends View {

    private int defaultWidth;
    private int defaultHeight;
    private int bgColor;
    private int mWidth;
    private int mHeight;
    private float clockBgRadius;
    private float clockLineRadius;

    private Drawable clockBgDrawable;
    private Drawable clockLineDrawable;
    
    private Paint blurRingPaint;
    private float blurRingRadius;
    private float mRadius;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        defaultWidth = UIUtils.dip2px(200);
        defaultHeight = UIUtils.dip2px(200);

        init();
    }

    private void init() {
        bgColor = Color.parseColor("#1A000000");
        clockBgDrawable = ContextCompat.getDrawable(getContext(),R.drawable.clock_bg);
        clockLineDrawable = ContextCompat.getDrawable(getContext(),R.drawable.clock_circle_line);

        blurRingPaint = new Paint();
        blurRingPaint.setColor(Color.parseColor("#E60044"));
        blurRingPaint.setAntiAlias(true);
        blurRingPaint.setMaskFilter(new BlurMaskFilter(16, BlurMaskFilter.Blur.NORMAL));
        blurRingPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int widthPaddingOffset = getPaddingLeft() + getPaddingRight();
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultWidth, defaultHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultWidth, heightSize + widthPaddingOffset);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize + widthPaddingOffset, defaultHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

   
}
