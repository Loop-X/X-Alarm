package io.github.loopX.XAlarm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

import io.github.loopX.XAlarm.R;

public class YummyTimePicker extends View {

    public static final float MARGIN_ALPHA = 1.3f; // Margin / TextSize

    private VelocityTracker mVelocityTracker;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;
    private Scroller mScroller;

    private List<String> mDataList;
    private int mCurrentSelected;
    
    private float mTextSize;
    private int mViewHeight;
    private int mViewWidth;
    private float mLastDownY;
    private float mMoveLen = 0;
    private onSelectListener mSelectListener;
    private int mLastEvent;
    private TextSelectorElement textSelectorElement;
    
    private DividerLineElement dividerLineElement;
    
    public YummyTimePicker(Context context) {
        super(context);
        init(context);
    }

    public YummyTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        mDataList = new ArrayList<>();
        
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mScroller = new Scroller(context);

        dividerLineElement = new DividerLineElement();
        textSelectorElement = new TextSelectorElement();
        setBackgroundColor(Color.GRAY);
    }

    /*
    Functions must be implemented by user
     */

    public void setOnSelectListener(onSelectListener listener)
    {
        mSelectListener = listener;
    }

    /**
     * Set list of items to show in picker. Must be implemented
     * @param datas List of items to show
     */
    public void setData(List<String> datas) {
        mDataList = datas;
        if (datas.size() < 2) {
            mCurrentSelected = 0;
        } else {
            mCurrentSelected = datas.size() / 2 - 1;
        }
        invalidate();
    }

    /**
     * Set selected item by its content. Optional.
     * By default, it's the middle item to show
     * @param mSelectItem Content of item
     */
    public void setSelected(String mSelectItem) {
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).equals(mSelectItem)) {
                setSelected(i);
                break;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        mTextSize = mViewHeight / 4.0f;
        dividerLineElement.onPrepareDraw();
        textSelectorElement.onPrepareDraw();
    }
    

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        textSelectorElement.draw(canvas);
        dividerLineElement.draw(canvas);

    }

    
    class DividerLineElement implements ViewPainter{

        private float topLineYPos;
        private float bottomLineYPos;
        private Paint mPaintDividerLine;


        public DividerLineElement() {
            mPaintDividerLine = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaintDividerLine.setStyle(Paint.Style.FILL);
            mPaintDividerLine.setColor(ContextCompat.getColor(getContext(), R.color.loopX_6));
        }
        
        @Override
        public void onPrepareDraw() {
            topLineYPos = mViewHeight / 2 - MARGIN_ALPHA * mTextSize / 2;
            bottomLineYPos = mViewHeight / 2 + MARGIN_ALPHA * mTextSize / 2;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawLine(0,topLineYPos, mViewWidth, topLineYPos, mPaintDividerLine);
            canvas.drawLine(0,bottomLineYPos, mViewWidth,bottomLineYPos, mPaintDividerLine);
        }
    }
    
    
    public class TextSelectorElement implements ViewPainter{
        
        private Paint selectedTextPaint;
        
        private Paint aboveTextPaint;
        
        private Paint bellowTextPaint;
        private float baseline;
        private float xPos;
        private float yPos;
        private float yOffset;

        public TextSelectorElement() {

            selectedTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            selectedTextPaint.setStyle(Paint.Style.FILL);
            selectedTextPaint.setTextAlign(Paint.Align.CENTER);
            selectedTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.loopX_3));
            selectedTextPaint.setTypeface(getTypeface());


            aboveTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            aboveTextPaint.setStyle(Paint.Style.FILL);
            aboveTextPaint.setTextAlign(Paint.Align.CENTER);
            aboveTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.loopX_3));
            aboveTextPaint.setTypeface(getTypeface());
            
            bellowTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bellowTextPaint.setStyle(Paint.Style.FILL);
            bellowTextPaint.setTextAlign(Paint.Align.CENTER);
            bellowTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.loopX_3));
            bellowTextPaint.setTypeface(getTypeface());
            
        }

        private Typeface getTypeface() {
            return Typeface.createFromAsset(YummyTimePicker.this.getContext().getAssets(),
                    "fonts/BebasNeue.otf");
        }

        @Override
        public void onPrepareDraw() {
            selectedTextPaint.setTextSize(mTextSize);
            aboveTextPaint.setTextSize(mTextSize);
            bellowTextPaint.setTextSize(mTextSize);
            
            aboveTextPaint.setShader(new LinearGradient(0, mTextSize / 4,
                    0, mTextSize * 2,
                    Color.BLACK, Color.WHITE, Shader.TileMode.CLAMP));
            bellowTextPaint.setShader(new LinearGradient(0, mViewHeight - mTextSize / 4,
                    0, mViewHeight - mTextSize * 2,
                    Color.BLACK, Color.WHITE, Shader.TileMode.CLAMP));
            
            xPos =  (float) (mViewWidth / 2);

            // http://www.open-open.com/lib/view/open1459931221098.html
            Paint.FontMetricsInt fontMetricsInt = selectedTextPaint.getFontMetricsInt();
            yOffset = (fontMetricsInt.top / 2 + fontMetricsInt.bottom / 2);
        }
        Rect textBounds = new Rect();

        @Override
        public void draw(Canvas canvas) {

            yPos = mViewHeight / 2 + mMoveLen;
            baseline = yPos - yOffset;
            
            if (mCurrentSelected < mDataList.size()){
                canvas.drawText(mDataList.get(mCurrentSelected), xPos, baseline, selectedTextPaint);
            }


            canvas.drawLine(0,baseline,mViewWidth , baseline,selectedTextPaint);
            
            for (int i = 1; i <= mCurrentSelected; i++)
            {
                drawText(canvas, i, -1);
            }

            for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++)
            {
                drawText(canvas, i, 1);
            }

        }

        /**
         * @param canvas
         * @param position
         * @param type 1 indicates to bottom，-1 indicates to top
         */
        private void drawText(Canvas canvas, int position, int type) {
            
            Paint paint = type == -1 ? aboveTextPaint : bellowTextPaint;
                    

            float d = MARGIN_ALPHA * mTextSize * position + type * mMoveLen;


            float y = mViewHeight / 2 + type * d;
            float baseline = y - yOffset;

            // String to draw
            String text = mDataList.get(mCurrentSelected + type * position);
            
            // Draw Text
            canvas.drawText(text, (float) (mViewWidth / 2), baseline, paint);
        }
    }

    
    

    /**
     * Selected given item
     * @param selected
     */
    public void setSelected(int selected)
    {
        mCurrentSelected = selected;

        int distance = mDataList.size() / 2 - mCurrentSelected;

        if (distance < 0) {
            for (int i = 0; i < -distance; i++) {
                moveHeadToTail();
                mCurrentSelected --;
            }
        } else if (distance > 0) {
            for (int i = 0; i < distance; i++) {
                moveTailToHead();
                mCurrentSelected ++;
            }
        }
        invalidate();
    }

    /**
     * Move first item to the end of list
     */
    private void moveHeadToTail() {
        String head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }

    /**
     * Move last item to the head of list
     */
    private void moveTailToHead() {
        String tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastEvent = MotionEvent.ACTION_DOWN;

                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear(); // Reset tracker back to its initial state.
                }

                mVelocityTracker.addMovement(event);
                mLastDownY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                mLastEvent = MotionEvent.ACTION_MOVE;
                mVelocityTracker.addMovement(event);
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                mLastEvent = MotionEvent.ACTION_UP;
                mVelocityTracker.addMovement(event);

                //doUp(event);

                float velocityY;

                mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                velocityY = mVelocityTracker.getYVelocity();

                // ToDo 如果不加这个的话 用户滑动太快会有奇怪的现象
                if(velocityY >= mMaximumFlingVelocity) {
                    velocityY = mMaximumFlingVelocity;
                }

                if ((Math.abs(velocityY) > mMinimumFlingVelocity)) {
                    mScroller.fling(0, (int)mLastDownY,
                            (int)mVelocityTracker.getXVelocity(), (int)velocityY,
                            0, 0,
                            0, mMaximumFlingVelocity);

                    ViewCompat.postInvalidateOnAnimation(this);
                }
            case MotionEvent.ACTION_CANCEL:
                if (mVelocityTracker != null) {
                    //mVelocityTracker.recycle();
                }

        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        // If scroll in progress
        if (mScroller.computeScrollOffset()){
            Log.e("YummyTimePicker", "---------> computeScrollOffset");

            int y = mScroller.getCurrY();
            
            mMoveLen += (y - mLastDownY);

            if (mMoveLen > MARGIN_ALPHA * mTextSize / 2) {

                // If direction is to bottom
                moveTailToHead();
                mMoveLen = mMoveLen - MARGIN_ALPHA * mTextSize;

            } else if (mMoveLen < -MARGIN_ALPHA * mTextSize / 2) {

                // If direction is to top
                moveHeadToTail();
                mMoveLen = mMoveLen + MARGIN_ALPHA * mTextSize;

            }

            mLastDownY = y;

            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            if(mLastEvent == MotionEvent.ACTION_UP) {
                mMoveLen = 0;
            }

            if (mSelectListener != null) {
                mSelectListener.onSelect(mDataList.get(mCurrentSelected));
            }
        }

        Log.e("YummyTimePicker","---------> computeScroll");
    }

    /**
     * When ACTION_UP
     * @param event
     */
    private void doUp(MotionEvent event) {

        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }

    }

    /**
     * When ACTION_MOVE
     * @param event
     */
    private void doMove(MotionEvent event) {

        mMoveLen += (event.getY() - mLastDownY);

        if (mMoveLen > MARGIN_ALPHA * mTextSize / 2) {

            // If direction is to bottom
            moveTailToHead();
            mMoveLen = mMoveLen - MARGIN_ALPHA * mTextSize;
        } else if (mMoveLen < -MARGIN_ALPHA * mTextSize / 2) {

            // If direction is to top
            moveHeadToTail();
            mMoveLen = mMoveLen + MARGIN_ALPHA * mTextSize;
        }

        mLastDownY = event.getY();
        invalidate();
    }

    public interface onSelectListener {
        void onSelect(String text);
    }

    /**
     * Set Time Picker to show hours
     */
    /**
     * Set Time Picker to show hours
     * @param is24hour If true, 24 hour mode. If false 12 hour mode
     */
    public void setHour(Boolean is24hour) {

        List<String> list = new ArrayList<>();


        for(int i = (is24hour? 0 : 1); i < (is24hour? 24 : 13); i++) {

            if(i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        
        setData(list);
    }

    /**
     * Set Time Picker to show minutes
     */
    public void setMinute() {

        List<String> list = new ArrayList<>();

        for(int i = 0; i < 60; i ++) {

            if(i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }

        setData(list);

    }

    /**
     * Set Time Picker to show AM / PM
     */
    public void setAMPM() {

        List<String> list = new ArrayList<>();

        list.add("AM");
        list.add("PM");

        setData(list);

    }

}
