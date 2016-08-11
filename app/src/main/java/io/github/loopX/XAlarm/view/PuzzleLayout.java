package io.github.loopX.XAlarm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import io.github.loopX.XAlarm.BuildConfig;
import io.github.loopX.XAlarm.tools.ImagePiece;
import io.github.loopX.XAlarm.tools.ImageSplitUtil;

public class PuzzleLayout extends RelativeLayout implements View.OnClickListener {
    private ImageView[] mPuzzleItems;
    private int mColumn;
    private int mPadding;
    private int mMargin = 3; // Distance between each image piece
    private int mItemWidth;
    private int mWidth; // Width of game board

    private Bitmap mBitmap;
    private List<ImagePiece> mItemBitmaps;
    private boolean once;

    private ImageView mFirst;
    private ImageView mSecond;

    // Animation Layout
    private RelativeLayout mAnimLayout;
    private boolean isAniming;

    public interface PuzzleListener {
        void unlockAlarm();
    }

    public PuzzleListener mListener;

    public void setPuzzleListener(PuzzleListener mListener) {
        this.mListener = mListener;
    }

    public PuzzleLayout(Context context) {
        this(context, null);
    }

    public PuzzleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PuzzleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                3, getResources().getDisplayMetrics());
        mPadding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(),
                getPaddingBottom());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
        mColumn = 3;

        if (!once) {
            initBitmap();
            initItem();

            once = true;
        }

        setMeasuredDimension(mWidth, mWidth);
    }

    private void initBitmap() {
        Random random = new Random();
        String imgName = "puzzle_" + (random.nextInt(22) + 1);

        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier(imgName, "drawable", BuildConfig.APPLICATION_ID));
        }
        mItemBitmaps = ImageSplitUtil.splitImage(mBitmap, mColumn);

        Collections.sort(mItemBitmaps, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece a, ImagePiece b) {
                return Math.random() > 0.5 ? 1 : -1;
            }
        });
    }

    private void initItem() {
        // mColumn means also number of piece in one row
        mItemWidth = (mWidth - mPadding * 2 - mMargin * (mColumn - 1))
                / mColumn;
        mPuzzleItems = new ImageView[mColumn * mColumn];

        for (int i = 0; i < mPuzzleItems.length; i++)
        {
            ImageView item = new ImageView(getContext());
            item.setOnClickListener(this);
            item.setImageBitmap(mItemBitmaps.get(i).getBitmap());

            mPuzzleItems[i] = item;
            item.setId(i + 1);

            // Save index in tag
            item.setTag(i + "_" + mItemBitmaps.get(i).getIndex());

            LayoutParams lp = new LayoutParams(
                    mItemWidth, mItemWidth);

            // If not last column
            if ((i + 1) % mColumn != 0) {
                lp.rightMargin = mMargin;
            }
            // If not first column
            if (i % mColumn != 0) {
                lp.addRule(RelativeLayout.RIGHT_OF, mPuzzleItems[i - 1].getId());
            }
            // If not first row, set topMargin and rule
            if ((i + 1) > mColumn) {
                lp.topMargin = mMargin;
                lp.addRule(RelativeLayout.BELOW, mPuzzleItems[i - mColumn].getId());
            }
            addView(item, lp);
        }
    }

    @Override
    public void onClick(View v) {
        // If animation is running, user can not touch layout
        if (isAniming)
            return;

        // If click one item two times. Cancel selected status
        if (mFirst == v) {
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }
        if (mFirst == null) {
            mFirst = (ImageView) v;
            mFirst.setColorFilter(Color.parseColor("#55FF0000"));
        } else {
            mSecond = (ImageView) v;
            exchangeView();
        }
    }

    /**
     * Exchange two items
     */
    private void exchangeView() {
        mFirst.setColorFilter(null);

        // Set Animation Layout
        if (mAnimLayout == null) {
            mAnimLayout = new RelativeLayout(getContext());
            addView(mAnimLayout);
        }

        ImageView first = new ImageView(getContext());
        final Bitmap firstBitmap = mItemBitmaps.get(
                getImageIdByTag((String) mFirst.getTag())).getBitmap();
        first.setImageBitmap(firstBitmap);

        LayoutParams lp = new LayoutParams(mItemWidth, mItemWidth);
        lp.leftMargin = mFirst.getLeft() - mPadding;
        lp.topMargin = mFirst.getTop() - mPadding;
        first.setLayoutParams(lp);

        mAnimLayout.addView(first); // Add on animation layout

        ImageView second = new ImageView(getContext());
        final Bitmap secondBitmap = mItemBitmaps.get(
                getImageIdByTag((String) mSecond.getTag())).getBitmap();
        second.setImageBitmap(secondBitmap);

        LayoutParams lp2 = new LayoutParams(mItemWidth, mItemWidth);
        lp2.leftMargin = mSecond.getLeft() - mPadding;
        lp2.topMargin = mSecond.getTop() - mPadding;
        second.setLayoutParams(lp2);

        mAnimLayout.addView(second); // Add on animation layout

        // Set animation
        TranslateAnimation anim = new TranslateAnimation(0, mSecond.getLeft() - mFirst.getLeft(),
                0, mSecond.getTop() - mFirst.getTop());
        anim.setDuration(300);
        anim.setFillAfter(true);
        first.startAnimation(anim);

        TranslateAnimation anim2 = new TranslateAnimation(0, -mSecond.getLeft() + mFirst.getLeft(),
                0, -mSecond.getTop() + mFirst.getTop());
        anim2.setDuration(300);
        anim2.setFillAfter(true);
        second.startAnimation(anim2);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mFirst.setVisibility(View.INVISIBLE);
                mSecond.setVisibility(View.INVISIBLE);

                isAniming = true; // animation is running
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {

                String firstTag = (String) mFirst.getTag();
                String secondTag = (String) mSecond.getTag();

                mFirst.setImageBitmap(secondBitmap);
                mSecond.setImageBitmap(firstBitmap);

                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);

                mFirst.setVisibility(View.VISIBLE);
                mSecond.setVisibility(View.VISIBLE);

                mFirst = mSecond = null;
                mAnimLayout.removeAllViews();
                // 判断用户游戏是否成功
                checkSuccess();
                isAniming = false;
            }
        });
    }

    /**
     * 判断用户游戏是否成功
     */
    private void checkSuccess() {
        boolean isSuccess = true;

        for (int i = 0; i < mPuzzleItems.length; i++) {
            ImageView imageView = mPuzzleItems[i];
            if (getImageIndexByTag((String) imageView.getTag()) != i) {
                isSuccess = false;
            }
        }

        if (isSuccess) {
            mListener.unlockAlarm();
        }
    }

    /**
     * Get id in the tag
     * @param tag id_index
     * @return
     */
    public int getImageIdByTag(String tag)
    {
        String[] split = tag.split("_");
        return Integer.parseInt(split[0]);
    }

    /**
     * Get Index in the tag
     * @param tag id_index
     * @return
     */
    public int getImageIndexByTag(String tag)
    {
        String[] split = tag.split("_");
        return Integer.parseInt(split[1]);
    }

    /**
     * Get the min in several numbers
     * @param params
     * @return
     */
    private int min(int... params) {
        int min = params[0];

        for (int param : params) {
            if (param < min)
                min = param;
        }
        return min;
    }


}
