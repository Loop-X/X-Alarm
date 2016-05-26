package io.github.loop_x.yummywakeup;

import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

import io.github.loop_x.yummywakeup.infrastructure.BaseActivity;

public class MainActivity extends BaseActivity {

    
    private static final String TAG = "yummywakeup.MainActivity";
    
    private DrawerLayout mDrawerLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void onViewInitial() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT); // Disable dark fading in Navigation Drawer
        mDrawerLayout.addDrawerListener(new DrawerListener());

        setDrawlayoutTouchArea("mLeftDragger", 3);
        setDrawlayoutTouchArea("mRightDragger", 3);
    }


    @Override
    public void onRefreshData() {

    }

    private class DrawerListener implements DrawerLayout.DrawerListener {


        /**
         * 
         * @param drawerView The child menu view that was moved
         * @param slideOffset center content view  to left|right menu : 0~1,
         */
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

            Log.e("TAG",String.format("slideOffset : %f",slideOffset));
            View centerView = mDrawerLayout.getChildAt(0);
            View mMenu = drawerView;
            float scale = 1 - slideOffset;
            float rightScale = 0.8f + scale * 0.2f;

            if (drawerView.getTag().equals("LEFT")) {
                float leftScale = 1 - 0.3f * scale;

                mMenu.setScaleX(leftScale);
                mMenu.setScaleY(leftScale);
                centerView.setTranslationX(mMenu.getMeasuredWidth() * (1 - scale));
                centerView.setPivotX(0);
                centerView.setPivotY(centerView.getMeasuredHeight() / 2);
                centerView.invalidate();
                centerView.setScaleX(rightScale);
                centerView.setScaleY(rightScale);

            } else {

                centerView.setTranslationX(-mMenu.getMeasuredWidth() * slideOffset);
                centerView.setPivotX(centerView.getMeasuredWidth());
                centerView.setPivotY(centerView.getMeasuredHeight() / 2);
                centerView.invalidate();
                centerView.setScaleX(rightScale);
                centerView.setScaleY(rightScale);

            }

        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }

    }

    /**
     * Set Drawlayout left/right dragger touch area
     *
     * @param dragger mLeftDragger | mRightDragger
     */
    private void setDrawlayoutTouchArea(String dragger, int area) {

        Field mDragger = null;//mRightDragger for right obviously
        try {
            mDragger = mDrawerLayout.getClass().getDeclaredField(
                    dragger);
            mDragger.setAccessible(true);
            ViewDragHelper draggerObj = (ViewDragHelper) mDragger.get(mDrawerLayout);

            Field mEdgeSize = draggerObj.getClass().getDeclaredField(
                    "mEdgeSize");
            mEdgeSize.setAccessible(true);
            int edge = mEdgeSize.getInt(draggerObj);

            mEdgeSize.setInt(draggerObj, edge * area);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
