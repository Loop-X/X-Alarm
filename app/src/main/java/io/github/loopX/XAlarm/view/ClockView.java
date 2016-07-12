package io.github.loopX.XAlarm.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.facebook.rebound.Spring;

import java.util.Calendar;
import java.util.TimeZone;

import io.github.loopX.XAlarm.R;
import io.github.loopX.XAlarm.UIUtils;
import io.github.loopX.XAlarm.tools.BaseSpringListener;
import io.github.loopX.XAlarm.tools.ReboundAnimation;

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
    
    private ViewPainter clockBackgroundDraw;
    private ViewPainter clockMainDraw;
    private ClockTimeIndicatorDraw  clockTimeIndicatorDraw;

    private boolean mAttached;
    private Calendar mCalendar;
    private final Handler mHandler = new Handler();
    private Spring mScaleAnimation;
    private Spring mTranslateAnimationSpring;
    private ValueAnimator mAlphaAnimation;

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
        
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Log.e("ClockView","OnGlobalLayoutListener");
                doTimeIndicatorAnimation();
            }
        });
    }

    
    private void initClockDraw() {
        clockBackgroundDraw = new ClockBackgroundDraw();
        clockMainDraw = new ClockMainDraw();
        clockTimeIndicatorDraw = new ClockTimeIndicatorDraw();

        mScaleAnimation = ReboundAnimation.getInstance().createSpringFromBouncinessAndSpeed(12, 12,new BaseSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = transition((float) spring.getCurrentValue(),0f,1f);
                setScaleY(value);
                setScaleY(value);
            }
        });

        mTranslateAnimationSpring = ReboundAnimation.getInstance().createSpringFromBouncinessAndSpeed(12, 16,new BaseSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float  mTranslateY = (UIUtils.getScreenHeight() - mClockHeight) / 2 + mClockHeight;
                float value =   transition((float) spring.getCurrentValue(),-mTranslateY,0);   
                setTranslationY(value);
            }
        });

        mAlphaAnimation = ObjectAnimator.ofFloat(0.0f,1.0f);
        mAlphaAnimation.setDuration(100);
        mAlphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setAlpha((Float) animation.getAnimatedValue());
            }
        });
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

        clockBackgroundDraw.onPrepareDraw();
        clockMainDraw.onPrepareDraw();
        clockTimeIndicatorDraw.onPrepareDraw();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        clockBackgroundDraw.draw(canvas);
        clockMainDraw.draw(canvas);
        clockTimeIndicatorDraw.draw(canvas);
    }

    public class ClockBackgroundDraw implements ViewPainter {

        private int bgColor;

        public ClockBackgroundDraw() {
            bgColor = Color.parseColor("#00000000");
        }

        @Override
        public void onPrepareDraw() {}

        @Override
        public void draw(Canvas canvas) {
            canvas.drawColor(bgColor);
        }
    }

    public  class ClockMainDraw implements ViewPainter {
        private Drawable clockMainDrawable;

        public ClockMainDraw() {
            clockMainDrawable = ContextCompat.getDrawable(getContext(),R.drawable.clock_main_bg);
        }

        @Override
        public void onPrepareDraw() {
            clockMainDrawable.setBounds(0, 0, mClockWidth, mClockHeight);
        }

        @Override
        public void draw(Canvas canvas) {
            clockMainDrawable.draw(canvas);
        }
    }

    public class ClockTimeIndicatorDraw implements ViewPainter {

        private Drawable hourIndicatorDrawable;
        private Drawable minuteIndicatorDrawable;
        private Drawable secondsIndicatorDrawable;

        private Drawable nighttimeIndicatorDrawable;
        private Drawable daytimeIndicatorDrawable;

        private float translate;
        private float hourAngle;
        private float minuteAngle;
        private float secondsAngle;

        private float translateX;
        private float translateY;
        private Calendar calendar;
        private int hourOfDay;
        
        public ClockTimeIndicatorDraw() {
            hourIndicatorDrawable = ContextCompat.getDrawable(getContext(), R.drawable.clock_hour_hand);
            minuteIndicatorDrawable = ContextCompat.getDrawable(getContext(), R.drawable.clock_minute_hand);
            secondsIndicatorDrawable = ContextCompat.getDrawable(getContext(), R.drawable.clock_second_hand);
            nighttimeIndicatorDrawable = ContextCompat.getDrawable(getContext(),R.drawable.clock_night);
            daytimeIndicatorDrawable = ContextCompat.getDrawable(getContext(),R.drawable.clock_daitime);
        }

        @Override
        public void onPrepareDraw() {
            int rightAndBottom = (int) (mClockWidth * 0.6f);
            hourIndicatorDrawable.setBounds(0,0,rightAndBottom,rightAndBottom);
            minuteIndicatorDrawable.setBounds(0,0,rightAndBottom,rightAndBottom);
            secondsIndicatorDrawable.setBounds(0,0,rightAndBottom,rightAndBottom);
            float radius = rightAndBottom * 1.0f / 2;
            translate = mClockRadius - radius;


            rightAndBottom = (int) (mClockWidth * 0.12f);
            nighttimeIndicatorDrawable.setBounds(0,0,rightAndBottom,rightAndBottom);
            daytimeIndicatorDrawable.setBounds(0,0,rightAndBottom,rightAndBottom);
            radius = rightAndBottom * 1.0f / 2;
            translateX = mClockRadius - radius;
            translateY = mClockRadius - ( mClockWidth * 0.78f / 2 ) - radius ;

            
            hourAngle = 40 / 60.0f * 360.0f;
            minuteAngle = 30 / 60.0f * 360.0f;
            secondsAngle = 20 / 60.0f * 360.0f;



        }
        
        @Override
        public void draw(Canvas canvas) {
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

            calendar = Calendar.getInstance();
            hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            if ( hourOfDay> 6 && hourOfDay <= 18){
                daytimeIndicatorDrawable.draw(canvas);
            }else {
                nighttimeIndicatorDrawable.draw(canvas);
            }
            canvas.restore();
        }
        
        public void updateTime(int hour,int minute,int seconds){
            hourAngle =  getHourAngleByHour(hour);
            minuteAngle = getMinuteAngleByMinute(minute);
            secondsAngle = getSecondAngleBySecond(seconds);
        }
        
        public void updateHourAngle(float hour ){
            hourAngle =  hour;
        }

        public void updateMinuteAngle(float minute){
            minuteAngle = minute;
        }

        public void updateSecondAngle(float seconds ){
            secondsAngle = seconds;
        }


    }
    
    private float getHourAngleByHour(int hour){
     return  hour / 12.0f * 360.0f;
    }

    private float getMinuteAngleByMinute(int minute){
        return minute / 60.0f * 360.0f;
    }
    
    private float getSecondAngleBySecond(int seconds ){
        return seconds / 60.0f * 360.0f;
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
        /* mCalendar = Calendar.getInstance();
        // Make sure we update to the current time
        onTimeChanged();
        doTimeIndicatorAnimation();
        // tick the seconds
        postDelayed(mClockTick,1000);*/

        Log.e("ClockView","onAttachedToWindow");
    }

    private void doTimeIndicatorAnimation() {
        mCalendar = Calendar.getInstance();
        
        final float hourAngle = getHourAngleByHour(mCalendar.get(Calendar.HOUR));
        final float minuteAngle = getMinuteAngleByMinute(mCalendar.get(Calendar.MINUTE));
        final float secondsAngle = getSecondAngleBySecond(mCalendar.get(Calendar.SECOND));

        ValueAnimator hourAnimator = ValueAnimator.ofInt(-35,0);
        hourAnimator.setInterpolator(new DecelerateInterpolator());
        hourAnimator.setDuration(700);
        hourAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                clockTimeIndicatorDraw.updateHourAngle(hourAngle + value);
                invalidate();
            }
        });

        ValueAnimator minuteAnimator = ValueAnimator.ofInt(-35,0);
        minuteAnimator.setInterpolator(new LinearInterpolator());
        minuteAnimator.setDuration(900);
        minuteAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                post(mClockTick);
            }
        });
        minuteAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                clockTimeIndicatorDraw.updateMinuteAngle( minuteAngle + value);
                invalidate();
            }
        });

        ValueAnimator secondAnimator = ValueAnimator.ofInt(-35,0);
        secondAnimator.setInterpolator(new DecelerateInterpolator());
        secondAnimator.setDuration(1100);
        secondAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                post(mClockTick);
            }
        });
        secondAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                clockTimeIndicatorDraw.updateSecondAngle( secondsAngle + value);
                invalidate();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(hourAnimator,minuteAnimator,secondAnimator);
        animatorSet.start();
        mAlphaAnimation.start();
        mTranslateAnimationSpring.setEndValue(1f);
        mScaleAnimation.setEndValue(1f);
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
