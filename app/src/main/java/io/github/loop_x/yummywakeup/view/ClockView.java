package io.github.loop_x.yummywakeup.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;

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
    private ClockElementDraw clockMainDraw;
    private ClockTimeIndicatorDraw  clockTimeIndicatorDraw;

    private boolean mAttached;
    private Calendar mCalendar;
    private final Handler mHandler = new Handler();


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
        clockMainDraw = new ClockMainDraw();
        clockTimeIndicatorDraw = new ClockTimeIndicatorDraw();
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
        clockMainDraw.prepareDraw();
        clockTimeIndicatorDraw.prepareDraw();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        clockBackgroundDraw.drawSelf(canvas);
        clockMainDraw.drawSelf(canvas);
        clockTimeIndicatorDraw.drawSelf(canvas);
    }


    /**
     * Draw clock view element step:
     * 
     * 
     * 
     * 
     * 1.prepareDraw() : 
     *  
     *  any numerical calculation should be there.If the clock view size was changed ,this method
     *  will be invoke in order to do numerical calculation .
     *                
     * 
     * 2.drawSelf(Canvas canvas) : this can draw element
     * 
     */
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
    
    
    
    
    public  class ClockMainDraw implements ClockElementDraw{
        private Drawable clockMainDrawable;

        public ClockMainDraw() {
            clockMainDrawable =  ContextCompat.getDrawable(getContext(),R.drawable.clock_main_bg); 
        }

        @Override
        public void prepareDraw() {
            clockMainDrawable.setBounds(0,0,mClockWidth,mClockHeight);
        }

        @Override
        public void drawSelf(Canvas canvas) {
            clockMainDrawable.draw(canvas);
        }
    }
    


    public class ClockTimeIndicatorDraw implements ClockElementDraw{

        private Drawable hourIndicatorDrawable;
        private Drawable minuteIndicatorDrawable;
        private Drawable secondsIndicatorDrawable;

        private Drawable dayAndNightIndicatorDrawable;

        private float translate;
        private float hourAngle;
        private float minuteAngle;
        private float secondsAngle;

        private float translateX;
        private float translateY;


        public ClockTimeIndicatorDraw() {
            hourIndicatorDrawable = ContextCompat.getDrawable(getContext(), R.drawable.clock_hour_hand);
            minuteIndicatorDrawable = ContextCompat.getDrawable(getContext(), R.drawable.clock_minute_hand);
            secondsIndicatorDrawable = ContextCompat.getDrawable(getContext(), R.drawable.clock_second_hand);
            dayAndNightIndicatorDrawable = ContextCompat.getDrawable(getContext(),R.drawable.clock_night);
        }

        @Override
        public void prepareDraw() {
            int rightAndBottom = (int) (mClockWidth * 0.6f);
            hourIndicatorDrawable.setBounds(0,0,rightAndBottom,rightAndBottom);
            minuteIndicatorDrawable.setBounds(0,0,rightAndBottom,rightAndBottom);
            secondsIndicatorDrawable.setBounds(0,0,rightAndBottom,rightAndBottom);
            float radius = rightAndBottom * 1.0f / 2;
            translate = mClockRadius - radius;


            rightAndBottom = (int) (mClockWidth * 0.12f);
            dayAndNightIndicatorDrawable.setBounds(0,0,rightAndBottom,rightAndBottom);
            radius = rightAndBottom * 1.0f / 2;
            translateX = mClockRadius - radius;
            translateY = mClockRadius - ( mClockWidth * 0.78f / 2 ) - radius ;

            
            hourAngle = 40 / 60.0f * 360.0f;
            minuteAngle = 30 / 60.0f * 360.0f;
            secondsAngle = 20 / 60.0f * 360.0f;

        }

        
        @Override
        public void drawSelf(Canvas canvas) {
            canvas.save();
            canvas.rotate(hourAngle,mClockRadius,mClockRadius);
            canvas.translate(translate,translate);
            hourIndicatorDrawable.draw(canvas);
            canvas.restore();

            canvas.save();
            canvas.rotate(minuteAngle,mClockRadius,mClockRadius);
            canvas.translate(translate,translate);
            minuteIndicatorDrawable.draw(canvas);
            canvas.restore();

            canvas.save();
            canvas.rotate(secondsAngle,mClockRadius,mClockRadius);
            canvas.translate(translate,translate);
            secondsIndicatorDrawable.draw(canvas);
            canvas.restore();


            canvas.save();
            canvas.rotate(secondsAngle,mClockRadius,mClockRadius);
            canvas.translate(translateX,translateY);
            dayAndNightIndicatorDrawable.draw(canvas);
            canvas.restore();
        }
        
        public void updateTime(int hour,int minute,int seconds){
            hourAngle =  hour / 12.0f * 360.0f;
            minuteAngle = minute / 60.0f * 360.0f;
            secondsAngle = seconds / 60.0f * 360.0f;
        }


    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;

            IntentFilter filter = new IntentFilter();

            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

            getContext().registerReceiver(mIntentReceiver, filter, null, mHandler);
        }
        // NOTE: It's safe to do these after registering the receiver since the receiver always runs
        // in the main thread, therefore the receiver can't run before this method returns.
        // The time zone may have changed while the receiver wasn't registered, so update the Time
        mCalendar = Calendar.getInstance();
        // Make sure we update to the current time
        onTimeChanged();
        // tick the seconds
        post(mClockTick);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            removeCallbacks(mClockTick);
            mAttached = false;
        }
    }
    

    /**
     * Task running every second to update time and re-draw the view with new updates
     */
    private final Runnable mClockTick = new Runnable () {
        @Override
        public void run() {
            onTimeChanged();
            invalidate();

            // Run the same task 1 second late
            ClockView.this.postDelayed(mClockTick, 1000);
        }
    };

    /**
     * Listen to ACTION_TIME_TICK | ACTION_TIME_CHANGED | ACTION_TIMEZONE_CHANGED.
     * Update time with new updates
     */
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // If time-zone changed. Update time with new time zone
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar.setTimeZone(TimeZone.getTimeZone(tz));
            }

            onTimeChanged();
            invalidate();
        }
    };



    /**
     * Invoked when time changed. To update hour/minute/second with current time.
     */
    private void onTimeChanged() {
        mCalendar = Calendar.getInstance();
        clockTimeIndicatorDraw.updateTime(mCalendar.get(Calendar.HOUR),mCalendar.get(Calendar.MINUTE),mCalendar.get(Calendar.SECOND));
        
    }











}
