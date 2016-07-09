package io.github.loopX.XAlarm.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.os.Handler;

import java.util.Calendar;
import java.util.TimeZone;

import io.github.loopX.XAlarm.R;

/**
 * This widget display an analogic clock with two hands for hours and
 * minutes.
 */
public class AnalogClock extends View {

    private static final String TAG = "yummywakeup.AnalogClock";

    private Calendar mCalendar;
    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private Drawable mSecondHand;
    private Drawable mDial;
    private Drawable mNightIcon;
    private int mDialWidth;
    private int mDialHeight;
    private boolean mAttached;
    private final Handler mHandler = new Handler();
    private float mSeconds;
    private float mMinutes;
    private float mHour;
    private boolean mChanged;
    private final Context mContext;

    /*
    Constructor
     */

    public AnalogClock(Context context) {
        this(context, null);
    }

    public AnalogClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
        initData();
    }

    /*
    Tasks
     */

    /**
     * Task running every second to update time and re-draw the view with new updates
     */
    private final Runnable mClockTick = new Runnable () {
        @Override
        public void run() {
            onTimeChanged();
            invalidate();

            // Run the same task 1 second late
            AnalogClock.this.postDelayed(mClockTick, 1000);
        }
    };

    /*
    BroadcastReceiver
     */

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

    /*
    Functions
     */

    private void initView() {
        mDial = ContextCompat.getDrawable(mContext, R.drawable.clock_main);
        mHourHand = ContextCompat.getDrawable(mContext, R.drawable.clock_hour_hand);
        mMinuteHand = ContextCompat.getDrawable(mContext, R.drawable.clock_minute_hand);
        mSecondHand = ContextCompat.getDrawable(mContext, R.drawable.clock_second_hand);
        mNightIcon = ContextCompat.getDrawable(mContext, R.drawable.clock_night);
    }

    private void initData() {
        mCalendar = Calendar.getInstance();
        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize =  MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize =  MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float) heightSize / (float) mDialHeight;
        }

        float scale = Math.min(hScale, vScale);
        setMeasuredDimension(resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0),
                resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean changed = mChanged;

        if (changed) {
            mChanged = false;
        }

        int availableWidth = getWidth();
        int availableHeight = getHeight();
        int x = availableWidth / 2;
        int y = availableHeight / 2;
        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();
        boolean scaled = false;

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        if (changed) {
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }

        dial.draw(canvas);

        drawHand(canvas, mHourHand, x, y, mHour / 12.0f * 360.0f, changed);
        drawHand(canvas, mMinuteHand, x, y, mMinutes / 60.0f * 360.0f, changed);
        drawHand(canvas, mSecondHand, x, y, mSeconds / 60.0f * 360.0f, changed);
        drawIcon(canvas, mNightIcon, x, y, mSeconds / 60.0f * 360.0f, changed);

        if (scaled) {
            canvas.restore();
        }
    }

    private void drawHand(Canvas canvas, Drawable hand, int x, int y, float angle,
                          boolean changed) {
        canvas.save();
        canvas.rotate(angle, x, y);
        if (changed) {
            final int w = hand.getIntrinsicWidth();
            final int h = hand.getIntrinsicHeight();
            hand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        hand.draw(canvas);
        canvas.restore();
    }

    private void drawIcon(Canvas canvas, Drawable hand, int x, int y, float angle,
                          boolean changed) {

        canvas.save();

        if (changed) {
            final int w = hand.getIntrinsicWidth();
            final int h = hand.getIntrinsicHeight();
            hand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }

        int iconX = mDial.getIntrinsicWidth() / 2 - 20;
        int iconY = mDial.getIntrinsicHeight() / 2 - 20;

        float a = (float) (iconX * Math.sin(Math.toRadians(angle)));
        float b = (float) (-iconY * Math.cos(Math.toRadians(angle)));

        canvas.translate(a, b);
        hand.draw(canvas);
        canvas.restore();

    }

    /**
     * Invoked when time changed. To update hour/minute/second with current time.
     */
    private void onTimeChanged() {

        mCalendar = Calendar.getInstance();

        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);
        int second = mCalendar.get(Calendar.SECOND);

        mSeconds = second;
        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;

        mChanged = true;

    }

}
