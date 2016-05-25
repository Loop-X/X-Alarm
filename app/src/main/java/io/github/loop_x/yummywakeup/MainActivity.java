package io.github.loop_x.yummywakeup;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

import io.github.loop_x.yummywakeup.infrastructure.BaseActivity;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    
    
  
    @Override
    public void onViewInitial() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(new DrawerListener());
    }

   

    @Override
    public void onRefreshData() {

    }

    private class DrawerListener implements DrawerLayout.DrawerListener {

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

            View mContent = mDrawerLayout.getChildAt(0);
            View mMenu = drawerView;

            float scale = 1 - slideOffset;
            float rightScale = 0.8f + scale * 0.2f;
            float leftScale = 1 - 0.3f * scale;

            ViewHelper.setScaleX(mMenu, leftScale);
            ViewHelper.setScaleY(mMenu, leftScale);

            if (mMenu.getTag() == "LEFT") {
                ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * slideOffset);
            } else {
                ViewHelper.setTranslationX(mContent, -mMenu.getMeasuredWidth() * slideOffset);
            }

            ViewHelper.setPivotX(mContent, 0);
            ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);

            mContent.invalidate();

            ViewHelper.setScaleX(mContent, rightScale);
            ViewHelper.setScaleY(mContent, rightScale);
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

}
