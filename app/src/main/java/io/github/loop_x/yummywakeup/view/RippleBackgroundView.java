package io.github.loop_x.yummywakeup.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;


/**
 * Author UFreedom
 * Date : 2016 一月 13
 */
public class RippleBackgroundView extends View implements ViewTreeObserver.OnGlobalLayoutListener {

    private Paint mPaint;
    private float mRippleRadius ;
    private int  mRippleOutBackgroundColor;
    private int  mRippleInBackgroundColor  ;
 /*   private int mRippleInColor = Color.RED;
    private int mRippleOutColor =  Color.RED;*/
    private int mRippleBackgroundColor = -1;
    private float mRipplePivotX = Float.MAX_VALUE;
    private float mRipplePivotY = Float.MAX_VALUE;
    
//    private int mMaxRadius = 0;
    private int mMinRadius = 0;
    private int mRippleColor;

    public RippleBackgroundView(Context context) {
        super(context);
        init(context);
    }

    public RippleBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRippleBackgroundColor != -1){
            canvas.drawColor(mRippleBackgroundColor);
        }
        canvas.drawCircle(mRipplePivotX, mRipplePivotY, mRippleRadius, mPaint);
    }
    

    public void setRippleRadius(float rippleRadius) {
        this.mRippleRadius = rippleRadius;
        invalidate();
    }
    
  /*  public void startRippleInAnim(){
        mRippleBackgroundColor = mRippleInBackgroundColor;
        mPaint.setColor(mRippleInColor);
        ObjectAnimator fadeOutAnimation = ObjectAnimator.ofFloat(this, "rippleRadius",getFinalRadius(),0);
        fadeOutAnimation.setDuration(350);
        fadeOutAnimation.setInterpolator(new DecelerateInterpolator());
        fadeOutAnimation.start();
    }
    
    public void startRippleOutAnim(){
        mRippleBackgroundColor = mRippleOutBackgroundColor;
        mPaint.setColor(mRippleOutColor);
        ObjectAnimator fadeOutAnimation = ObjectAnimator.ofFloat(this, "rippleRadius", mRippleRadius,getFinalRadius());
        fadeOutAnimation.setDuration(350);
        fadeOutAnimation.setInterpolator(new DecelerateInterpolator());
        fadeOutAnimation.start();
    }*/
    
    
    public void startRipple(RippleBuilder rippleBuilder){
        if (rippleBuilder == null) return;
        
        mRippleBackgroundColor = rippleBuilder.backgroundColor;
        mRipplePivotX = rippleBuilder.ripplePivotX;
        mRipplePivotY = rippleBuilder.ripplePivotY;
        
        int startRadius;
        int endRadius;
        
        mPaint.setColor(rippleBuilder.rippleColor);
        ObjectAnimator rippleAnimation = ObjectAnimator.ofFloat(this, "rippleRadius", rippleBuilder.startRippleRadius,rippleBuilder.finishRippleRadius);
        rippleAnimation.setDuration(350);
        rippleAnimation.setInterpolator(new DecelerateInterpolator());
        rippleAnimation.start();
        
    }
   

    @Override
    public void onGlobalLayout() {
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
        if (mRipplePivotX == Float.MAX_VALUE){
            mRipplePivotX = getMeasuredWidth() / 2;
        }
        
        if (mRipplePivotY == Float.MAX_VALUE){
            mRipplePivotY = getMeasuredHeight() / 2;
        }
     /*   if (mMaxRadius == 0){
            mMaxRadius = Math.max(getWidth(), getHeight());
        }*/
    }


    public static enum RippleDirection{
        EXPAND,SHRINK
    }


    public static class RippleBuilder{
        public Context context;
        private int rippleColor;
        private int backgroundColor;
        private RippleDirection rippleDirection;
        private int ripplePivotX;
        private int ripplePivotY;
        private int finishRippleRadius;
        private int startRippleRadius;

        public RippleBuilder(Context context) {
            this.context = context;

            backgroundColor = -1;
            rippleDirection = RippleDirection.EXPAND;
            startRippleRadius = 0;
        }

        public RippleBuilder setRippleColor(int rippleColor) {
            this.rippleColor = rippleColor;
            return this;
        }

        public RippleBuilder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public RippleBuilder setRippleDirection(RippleDirection rippleDirection) {
            this.rippleDirection = rippleDirection;
            return this;
        }

        public RippleBuilder setRipplePivotX(int ripplePivotX) {
            this.ripplePivotX = ripplePivotX;
            return this;
        }

        public RippleBuilder setRipplePivotY(int ripplePivotY) {
            this.ripplePivotY = ripplePivotY;
            return this;
        }

        public RippleBuilder setFinishRippleRadius(int maxRippleRadius) {
            this.finishRippleRadius = maxRippleRadius;
            return this;
        }

        public RippleBuilder setStartRippleRadius(int minRippleRadius) {
            this.startRippleRadius = minRippleRadius;
            return this;
        }

    }

}
