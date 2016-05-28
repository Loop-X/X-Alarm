package io.github.loop_x.yummywakeup.module.AnalogClock;

import java.util.Calendar;
import java.util.TimeZone;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import io.github.loop_x.yummywakeup.R;

public class AnalogClockView extends RelativeLayout {

	private Context mContext;

	/* views */
	private ImageView ivHourHand;
	private ImageView ivMinuteHand;
	private ImageView ivSecondHand;

	/* state */
	private boolean isRunning = false;
	private boolean isFirstTick = true;
	
	/* angle */
	private int mHourAngle = INVALID_ANGLE;
	private int mMinuteAngle = INVALID_ANGLE;
	private int mSecondAngle = INVALID_ANGLE;
	
	/* resources */
	private int mDialResourceID;
	private int mHourHandResourceID;
	private int mMinuteHandResourceID;
	private int mSecondHandResourceID;
	
	private static final int INVALID_ANGLE = -1;
    private static final int DEGREE_SECOND  = 6; // 360 / 60s
    private static final int DEGREE_MINUTE  = 6; // 360 / 60m
    private static final int DEGREE_HOUR = 30; // 360 / 12h
	private static final float DEGREE_HOUR_MINUTE = 0.5f; // 360 / 12h / 60m

	/*
	Constructor
	 */

	public AnalogClockView(Context context) {
		super(context);
		initView(context);
	}
	public AnalogClockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttributes(context, attrs);
		initView(context);
	}
	public AnalogClockView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttributes(context, attrs);
		initView(context);
	}

	/*
	Functions
	 */

	private void initAttributes(Context context, AttributeSet attrs){
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AnalogClockView);

		mDialResourceID = array.getResourceId(R.styleable.AnalogClockView_dial, R.drawable.clock_main);
		mHourHandResourceID = array.getResourceId(R.styleable.AnalogClockView_hand_hour, R.drawable.clock_hour_hand);
		mMinuteHandResourceID = array.getResourceId(R.styleable.AnalogClockView_hand_minute, R.drawable.clock_min_hand);
		mSecondHandResourceID = array.getResourceId(R.styleable.AnalogClockView_hand_second, R.drawable.clock_second_hand);

		array.recycle();
	}

	private void initView(Context context){
		this.mContext = context;

		RelativeLayout.LayoutParams lp;
		lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.addRule(CENTER_IN_PARENT);

		// Add Hour Hand
		ivHourHand = new ImageView(mContext);
		ivHourHand.setImageResource(mHourHandResourceID);
		ivHourHand.setScaleType(ScaleType.CENTER_INSIDE);
		this.addView(ivHourHand, lp);

		// Add Minute Hand
		ivMinuteHand = new ImageView(mContext);
		ivMinuteHand.setImageResource(mMinuteHandResourceID);
		ivMinuteHand.setScaleType(ScaleType.CENTER_INSIDE);
		this.addView(ivMinuteHand, lp);

		// Add Second Hand
		ivSecondHand = new ImageView(mContext);
		ivSecondHand.setImageResource(mSecondHandResourceID);
		ivSecondHand.setScaleType(ScaleType.CENTER_INSIDE);
		this.addView(ivSecondHand, lp);

		// Add Dial
		setBackgroundResource(mDialResourceID);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

        // Start Clock
        if (isRunning)
            return;

        isRunning = true;
        isFirstTick = true;

        // To be sure starting with the exact second
        Calendar tempCal = Calendar.getInstance();
        int timeToWaitInMilli = (int)tempCal.getTimeInMillis() % 1000;

        mHandler.sendEmptyMessageDelayed(0, timeToWaitInMilli);
	}

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // Stop clock
        if (!isRunning)
            return;
        isRunning = false;
    }

	private void proceed(){
		if (!isRunning)
			return;

		TimeZone tz = TimeZone.getDefault();

		Calendar currentTime = Calendar.getInstance();
		currentTime.setTimeZone(tz);
		
		int hour = currentTime.get(Calendar.HOUR);
		int min = currentTime.get(Calendar.MINUTE);
		int sec = currentTime.get(Calendar.SECOND);

		int newHourAngle = hour * DEGREE_HOUR + (int) (min * DEGREE_HOUR_MINUTE);
		int newMinuteAngle = min * DEGREE_MINUTE;
		int newSecondAngle = sec * DEGREE_SECOND;

		if (isFirstTick){
			if (mHourAngle != INVALID_ANGLE && mMinuteAngle != INVALID_ANGLE && mSecondAngle != INVALID_ANGLE){
				rotate(ivHourHand, mHourAngle, newHourAngle);
				rotate(ivMinuteHand, mMinuteAngle, newMinuteAngle);
				rotate(ivSecondHand, mSecondAngle, newSecondAngle);
			}
			else{
				rotate(ivHourHand, newHourAngle, newHourAngle);
				rotate(ivMinuteHand, newMinuteAngle, newMinuteAngle);
				rotate(ivSecondHand, newSecondAngle, newSecondAngle);
			}
			isFirstTick = false;
		}
		else{
            /*
            When clock passes the first tick. Second hand keeps rotating.
            Minute hand won't rotate until second hand pass 0.
            Hour hand won't rotate until both second hand and minute hand pass 0
             */
			if (min == 0 && sec == 0)
				rotate(ivHourHand, newHourAngle - DEGREE_MINUTE, newHourAngle);
			if (sec == 0)
				rotate(ivMinuteHand, newMinuteAngle - DEGREE_MINUTE, newMinuteAngle);

			rotate(ivSecondHand, newSecondAngle - DEGREE_SECOND, newSecondAngle);
		}

		mHourAngle = newHourAngle;
		mMinuteAngle = newMinuteAngle;
		mSecondAngle = newSecondAngle;
	}

	private void rotate(ImageView view, int fromAngle, int toAngle){

        Animation anim = new RotateAnimation(fromAngle, toAngle,
				RotateAnimation.RELATIVE_TO_PARENT, 0.5f,
				RotateAnimation.RELATIVE_TO_PARENT, 0.5f);

		int gap = Math.abs(toAngle - fromAngle);

		if (gap != DEGREE_MINUTE){
			anim.setDuration(600);
			anim.setInterpolator(AnimationUtils.loadInterpolator(mContext,
					android.R.anim.accelerate_interpolator));
		} else {
			anim.setDuration(150);
			anim.setInterpolator(AnimationUtils.loadInterpolator(mContext,
					android.R.anim.overshoot_interpolator));
		}

        // Persist animation when finished.
		anim.setFillAfter(true);

        // Begin animation
		view.startAnimation(anim);
	}

	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if (isRunning){
				proceed();
				mHandler.sendEmptyMessageDelayed(0, 1000);
            }
		}
	};
}
