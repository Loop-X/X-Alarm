package io.github.loop_x.yummywakeup.view;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import io.github.loop_x.yummywakeup.UIUtils;

public class LoopXDragMenuLayout extends FrameLayout {
    
    private GestureDetectorCompat gestureDetector;
    private ViewDragHelper dragHelper;
    private int range; //手指滑动主界面,被判定为可以打开菜单的距离范围
    private int leftMenuWidth;
    private int leftMenuHeight;
    private int rightMenuWidth;
    private int rightMenuHeight;
    private int shadowViewWidth;
    private int shadowViewHeight;
    private View leftMenuView;
    private View mainContentView;
    private View rightMenuView;
    private View shadowView;
    private int mainViewRelativeToMenu;

    public enum MenuStatus {
        Drag, Open, Close
    }

    public MenuStatus getMenuStatus() {
        if (mainViewRelativeToMenu == 0) {
            return MenuStatus.Close;
        } else if (Math.abs(mainViewRelativeToMenu) == range) {
            return MenuStatus.Open;
        } else {
            return MenuStatus.Drag;
        }
    }

    private ViewDragHelper.Callback dragHelperCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            // dx > 0 to right. dx < 0 to left
            int result = mainViewRelativeToMenu + dx;

            if (result >= range) {
                return range;
            } else if (result < -range) {
                return -range;
            } else {
                return left;
            }

        }

        /**
         * 设置水平方向滑动的最远距离,根据左右Menu的宽判断
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return child == leftMenuView ? leftMenuWidth : rightMenuWidth;
        }

        /**
         * 当拖拽的子View，手势释放的时候回调的方法， 然后根据左滑或者右滑的距离进行判断打开或者关闭
         * @param releasedChild
         * @param xvel 大于0 表示向右滑动,小于0表示向左滑动,等于0表示释放前没有滑动动作
         * @param yvel 
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            Log.e("ViewDragHelper", " --------> onViewReleased xvel :" + xvel + ": mainViewRelativeToMenu : " + mainViewRelativeToMenu + " range:" + range);

            if (xvel > 0) {
                if (mainViewRelativeToMenu < 0) {
                    if (mainViewRelativeToMenu > -range) {
                        closeRightMenuWithAnimation();
                    } else {
                        openRightMenuWithAnimation();
                    }
                } else {
                    openLeftMenuWithAnimation();
                }
            } else if (xvel < 0) {

                if (mainViewRelativeToMenu > 0) {
                    if (mainViewRelativeToMenu < range) {
                        closeLeftMenuWithAnimation();
                    } else {
                        openLeftMenuWithAnimation();
                    }
                } else {
                    openRightMenuWithAnimation();

                }

            } else {
                if (releasedChild == mainContentView) {
                    if (mainViewRelativeToMenu > 0) {
                        //左边菜单正在打开
                        if (mainViewRelativeToMenu > range * 0.3) {
                            openLeftMenuWithAnimation();
                        } else {
                            closeLeftMenuWithAnimation();
                        }

                    } else {
                        //右边菜单正在打开

                        if (mainViewRelativeToMenu < -range * 0.3) {
                            openRightMenuWithAnimation();
                        } else {
                            closeRightMenuWithAnimation();
                        }
                    }
                } else if (releasedChild == leftMenuView) {
                    if (mainViewRelativeToMenu > range * 0.3) {
                        openLeftMenuWithAnimation();
                    } else {
                        closeLeftMenuWithAnimation();
                    }
                } else if (releasedChild == rightMenuView) {
                    if (mainViewRelativeToMenu < -range * 0.3) {
                        openRightMenuWithAnimation();
                    } else {
                        closeRightMenuWithAnimation();
                    }
                }
            }
        }


        /**
         * 子View被拖拽 移动的时候回调的方法
         * @param changedView View whoseposition changed
         * @param left New X coordinate of theleft edge of the view
         * @param top New Y coordinate of thetop edge of the view
         * @param dx Change in X position fromthe last call
         * @param dy Change in Y position fromthe last call
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {

            if (changedView == mainContentView) {
                mainViewRelativeToMenu = left;

                if (mainViewRelativeToMenu > range) {
                    mainViewRelativeToMenu = range;
                } else if (mainViewRelativeToMenu < -range) {
                    mainViewRelativeToMenu = -range;
                }
            } else if (changedView == leftMenuView) {

                mainViewRelativeToMenu = mainViewRelativeToMenu + dx;

                if (mainViewRelativeToMenu < 0) {
                    mainViewRelativeToMenu = 0;
                }

                if (mainViewRelativeToMenu >= range) {
                    mainViewRelativeToMenu = range;
                }

            } else if (changedView == rightMenuView) {
                mainViewRelativeToMenu = mainViewRelativeToMenu + dx;

                if (mainViewRelativeToMenu < -range) {
                    mainViewRelativeToMenu = -range;
                }
            }
           
            //layout shadow view
            shadowView.layout(mainViewRelativeToMenu, 0, mainViewRelativeToMenu + shadowViewWidth, shadowViewHeight);

            if (changedView == mainContentView) {
                if (mainViewRelativeToMenu > 0) {

                    //向右滑动，打开左边菜单
                    float percent = mainViewRelativeToMenu / (float) range;
                    float f1 = 1 - percent * 0.3f;
                    mainContentView.setScaleX(f1);
                    mainContentView.setScaleY(f1);

                    rightMenuView.layout(UIUtils.getScreenWidth(), 0, UIUtils.getScreenWidth() + rightMenuView.getWidth(), rightMenuView.getHeight());
                    leftMenuView.layout(0, 0, leftMenuWidth, leftMenuHeight);
                    leftMenuView.setTranslationX(-leftMenuView.getWidth() / 2.3f + leftMenuView.getWidth() / 2.3f * percent);
                    leftMenuView.setScaleX(0.5f + 0.5f * percent);
                    leftMenuView.setScaleY(0.5f + 0.5f * percent);
                    leftMenuView.setAlpha(percent);

                    shadowView.setScaleX(f1 * 1.4f * (1 - percent * 0.12f));
                    shadowView.setScaleY(f1 * 1.85f * (1 - percent * 0.12f));
                    shadowView.setAlpha(percent);

                } else if (mainViewRelativeToMenu < 0) {

                    //向左滑动，打开右边菜单

                    
                    float percent = -mainViewRelativeToMenu / (float) range;
                    float f2 = 1 - percent * 0.3f;
                    
                    mainContentView.setScaleX(f2);
                    mainContentView.setScaleY(f2);
                    
                    leftMenuView.layout(-leftMenuWidth, 0, 0, leftMenuHeight);
                    rightMenuView.layout(0, 0, rightMenuView.getWidth(), rightMenuView.getHeight());

                    rightMenuView.setAlpha(percent);
                    rightMenuView.setScaleX(0.5f + 0.5f * percent);
                    rightMenuView.setScaleY(0.5f + 0.5f * percent);
                    rightMenuView.setPivotX(rightMenuView.getMeasuredWidth());
                    rightMenuView.setPivotX(rightMenuView.getMeasuredHeight() / 2);
                    int w = (int) (rightMenuView.getWidth() * 0.2);
                    rightMenuView.setTranslationX(w - w * percent);

                    shadowView.setScaleX(f2 * 1.8f * (1 - percent * 0.12f));
                    shadowView.setScaleY(f2 * 2.8f * (1 - percent * 0.12f));
                    shadowView.setAlpha(percent);
                }
            } else if (changedView == leftMenuView) {

                //手指在左菜单上滑动

                leftMenuView.layout(0, 0, leftMenuWidth, leftMenuHeight);
                mainContentView.layout(mainViewRelativeToMenu, 0, mainViewRelativeToMenu + leftMenuWidth, leftMenuHeight);

                float percent = mainViewRelativeToMenu / (float) range;
                float f1 = 1 - percent * 0.3f;
                mainContentView.setScaleX( f1);
                mainContentView.setScaleY(f1);
                leftMenuView.setTranslationX(-leftMenuView.getWidth() / 2.3f + leftMenuView.getWidth() / 2.3f * percent);
                leftMenuView.setScaleX(0.5f + 0.5f * percent);
                leftMenuView.setScaleY(0.5f + 0.5f * percent);
                leftMenuView.setAlpha(percent);

                shadowView.setScaleX(f1 * 1.4f * (1 - percent * 0.12f));
                shadowView.setScaleY(f1 * 1.85f * (1 - percent * 0.12f));
                shadowView.setAlpha(percent);


            } else if (changedView == rightMenuView) {

                //手指在右菜单上滑动

                rightMenuView.layout(0, 0, rightMenuWidth, rightMenuHeight);
                mainContentView.layout(mainViewRelativeToMenu, 0, mainViewRelativeToMenu + leftMenuWidth, leftMenuHeight);

                float percent = -mainViewRelativeToMenu / (float) range;
                float f2 = 1 - percent * 0.3f;
                mainContentView.setScaleX(f2);
                mainContentView.setScaleY(f2);
                rightMenuView.setAlpha(percent);
                rightMenuView.setScaleX(0.5f + 0.5f * percent);
                rightMenuView.setScaleY(0.5f + 0.5f * percent);
                rightMenuView.setPivotX(rightMenuView.getMeasuredWidth());
                rightMenuView.setPivotX(rightMenuView.getMeasuredHeight() / 2);
                int w = (int) (rightMenuView.getWidth() * 0.2);
                rightMenuView.setTranslationX(w - w * percent);

                shadowView.setScaleX(f2 * 1.7f * (1 - percent * 0.12f));
                shadowView.setScaleY(f2 * 2.8f * (1 - percent * 0.12f));
                shadowView.setAlpha(percent);
            }


        }
    };

    public LoopXDragMenuLayout(Context context) {
        this(context, null);
    }

    public LoopXDragMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopXDragMenuLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gestureDetector = new GestureDetectorCompat(context, new YScrollDetector());
        dragHelper = ViewDragHelper.create(this, dragHelperCallback);
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        leftMenuView    = findViewWithTag("leftMenu");
        mainContentView = findViewWithTag("mainView");
        rightMenuView   = findViewWithTag("rightMenu");
        shadowView      = findViewWithTag("bgShadowView");

        leftMenuView.setClickable(true);
        mainContentView.setClickable(true);
        rightMenuView.setClickable(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        leftMenuWidth = leftMenuView.getMeasuredWidth();
        leftMenuHeight = leftMenuView.getMeasuredHeight();

        rightMenuWidth = rightMenuView.getMeasuredWidth();
        rightMenuHeight = rightMenuView.getMeasuredHeight();

        shadowViewWidth  = shadowView.getMeasuredWidth();
        shadowViewHeight = shadowView.getMeasuredHeight();

        // 屏幕宽度的 60%
        range = (int) (leftMenuWidth * 0.6f);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev) && gestureDetector.onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        try {
            dragHelper.processTouchEvent(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void openRightMenuWithAnimation() {
        if (dragHelper.smoothSlideViewTo(mainContentView, -range, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void closeRightMenuWithAnimation() {
        if (dragHelper.smoothSlideViewTo(mainContentView, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void openLeftMenuWithAnimation() {
        if (dragHelper.smoothSlideViewTo(mainContentView, range, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void closeLeftMenuWithAnimation() {
        if (dragHelper.smoothSlideViewTo(mainContentView, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    class YScrollDetector extends SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
            return Math.abs(dy) <= Math.abs(dx);
        }
    }

}
