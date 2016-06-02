package io.github.loop_x.yummywakeup.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.github.loop_x.yummywakeup.R;

public class YummyTimePicker extends View {

    public static final float MARGIN_ALPHA = 1.3f; // Margin / TextSize
    public static final float SPEED = 2; // Scroll speed

    private List<String> mDataList;
    private int mCurrentSelected;
    private Paint mPaint;
    private float mTextSize;
    private int mViewHeight;
    private int mViewWidth;
    private float mLastDownY;
    private float mMoveLen = 0;
    private boolean isInit = false;
    private onSelectListener mSelectListener;
    private Timer timer;
    private MyTimerTask mTask;

    Handler updateHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else {
                // mMoveLen / Math.abs(mMoveLen) is to get +/- of mMoveLen which indicates top/bottom
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
            }

            invalidate();
        }

    };

    /*
    Constructors
     */

    public YummyTimePicker(Context context) {
        super(context);
        init(context);
    }

    public YummyTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        timer = new Timer();
        mDataList = new ArrayList<>();

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/BebasNeue.otf");

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.loopX_3));
        mPaint.setTypeface(tf);

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
        mCurrentSelected = datas.size() / 2;
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

    /*
    Functions
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();

        mTextSize = mViewHeight / 4.0f;

        isInit = true;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInit) {

            mPaint.setTextSize(mTextSize);

            float x = (float) (mViewWidth / 2);
            float y = mViewHeight / 2 + mMoveLen;

            // http://www.open-open.com/lib/view/open1459931221098.html
            Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
            float baseline = (float) (y - (fontMetricsInt.top / 2 + fontMetricsInt.bottom / 2));

            canvas.drawText(mDataList.get(mCurrentSelected), x, baseline, mPaint);

            // Draw items above mCurrentSelected
            for (int i = 1; i <= mCurrentSelected; i++)
            {
                drawOtherText(canvas, i, -1);
            }

            // Draw items below mCurrentSelected
            for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++)
            {
                drawOtherText(canvas, i, 1);
            }
        }
    }

    /**
     * @param canvas
     * @param position
     * @param type 1 indicates to bottom，-1 indicates to top
     */
    private void drawOtherText(Canvas canvas, int position, int type) {

        float d = MARGIN_ALPHA * mTextSize * position + type * mMoveLen;

        mPaint.setTextSize(mTextSize);

        float y = mViewHeight / 2 + type * d;
        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
        float baseline = y - (fontMetricsInt.top / 2 + fontMetricsInt.bottom / 2);

        // String to draw
        String text = mDataList.get(mCurrentSelected + type * position);

        // Draw Text
        canvas.drawText(text, (float) (mViewWidth / 2), baseline, mPaint);
    }


    private void performSelect() {
        if (mSelectListener != null) {
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
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
            for (int i = 0; i < -distance; i++)
            {
                moveHeadToTail();
                mCurrentSelected --;
            }
        } else if (distance > 0) {
            for (int i = 0; i < distance; i++)
            {
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
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }

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

    private void doUp(MotionEvent event) {

        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }

        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }

        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }

    }

    public interface onSelectListener {
        void onSelect(String text);
    }

}
