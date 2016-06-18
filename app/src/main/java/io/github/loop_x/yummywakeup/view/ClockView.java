package io.github.loop_x.yummywakeup.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
    
    private int mClockWidth;
    private int mClockHeight;
    private float mClockRadius;
    
    private ClockElementDraw clockBackgroundDraw;
    private ClockElementDraw circleBackgroundDraw;
    private CircleLineDraw circleLineDraw;
    private ClockElementDraw secondsIndicatorDraw;




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

        initClockDraw();
    }

    private void initClockDraw() {
        clockBackgroundDraw = new ClockBackgroundDraw();
        circleBackgroundDraw = new CircleBackgroundDraw();
        circleLineDraw = new CircleLineDraw();
        secondsIndicatorDraw = new SecondsIndicatorDraw();
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mClockWidth = getMeasuredWidth();
        mClockHeight = getMeasuredHeight();
        mClockRadius = mClockWidth * 1.0f / 2;

        clockBackgroundDraw.prepareDraw();
        circleBackgroundDraw.prepareDraw();
        circleLineDraw.prepareDraw();
        secondsIndicatorDraw.prepareDraw();
        
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        clockBackgroundDraw.drawSelf(canvas);
        circleBackgroundDraw.drawSelf(canvas);
        circleLineDraw.drawSelf(canvas);
        secondsIndicatorDraw.drawSelf(canvas);
    }
    
    
    
    public interface ClockElementDraw{
        
        public void prepareDraw();
        
        public void drawSelf(Canvas canvas);
        
    }
    
    public class ClockBackgroundDraw implements ClockElementDraw{
        private int bgColor;

        public ClockBackgroundDraw() {
            bgColor = Color.parseColor("#1A000000");
        }

        @Override
        public void prepareDraw() {
            
        }

        @Override
        public void drawSelf(Canvas canvas) {
            canvas.drawColor(bgColor);
        }
    }
    
    
    
    public class CircleBackgroundDraw implements ClockElementDraw{

        private Drawable clockBgDrawable;
        private float translate;

        public CircleBackgroundDraw(){
            clockBgDrawable = ContextCompat.getDrawable(getContext(),R.drawable.clock_bg);
        }


        @Override
        public void prepareDraw() {
            int rightAndBottom = (int) (mClockWidth *  0.9161f);
            float clockBgRadius = rightAndBottom * 1.0f / 2;
            clockBgDrawable.setBounds(0,0,rightAndBottom ,rightAndBottom);
            translate = mClockRadius - clockBgRadius;
        }

        @Override
        public void drawSelf(Canvas canvas) {
            canvas.save();
            canvas.translate(translate,translate);
            clockBgDrawable.draw(canvas);
            canvas.restore();
        }
    } 
    
    
    public class CircleLineDraw implements ClockElementDraw{
        private Drawable lineDrawable;
        private float translate;
        private float radius;

        public CircleLineDraw() {
            lineDrawable = ContextCompat.getDrawable(getContext(),R.drawable.clock_circle_line);
             
        }
        
        public float getRadius(){
            return radius;
        }

        @Override
        public void prepareDraw() {
            int rightAndBottom = (int) (mClockWidth * 0.78f);
            lineDrawable.setBounds(0,0,rightAndBottom,rightAndBottom);
            radius = rightAndBottom * 1.0f / 2;
            translate = mClockRadius - radius;
        }

        @Override
        public void drawSelf(Canvas canvas) {
            canvas.save();
            canvas.translate(translate,translate);
            lineDrawable.draw(canvas);
            canvas.restore();
        }
    }
    
    public class SecondsIndicatorDraw implements ClockElementDraw{
        
        private Drawable secondsIndicatorDrawable;
        private float translate;
        private float translateY;
        private float angle;


        public SecondsIndicatorDraw() {
            secondsIndicatorDrawable = ContextCompat.getDrawable(getContext(),R.drawable.clock_night);
        }

        @Override
        public void prepareDraw() {
            int rightAndBottom = (int) (mClockWidth * 0.1f);
            secondsIndicatorDrawable.setBounds(0,0,rightAndBottom,rightAndBottom);
            float radius = rightAndBottom * 1.0f / 2;

            angle = 0 / 60.0f * 360.0f;
                    
            translate = mClockRadius - radius;

            translateY = mClockRadius - circleLineDraw.getRadius() - radius / 1.7f;
        }

        @Override
        public void drawSelf(Canvas canvas) {
            canvas.save();
            canvas.rotate(angle,mClockRadius,mClockRadius);
            canvas.translate(translate,translateY);
            secondsIndicatorDrawable.draw(canvas);
            
            canvas.restore();
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
