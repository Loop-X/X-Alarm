package io.github.loop_x.yummywakeup;

import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

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
        mDrawerLayout.setScrimColor(Color.TRANSPARENT); // Disable dark fading in Navigation Drawer
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

            if (drawerView.getTag().equals("LEFT")) {
                float leftScale = 1 - 0.3f * scale;

                mMenu.setScaleX(leftScale);
                mMenu.setScaleY(leftScale);
                mContent.setTranslationX(mMenu.getMeasuredWidth() * (1 - scale));
                mContent.setPivotX(0);
                mContent.setPivotY(mContent.getMeasuredHeight() / 2);
                mContent.invalidate();
                mContent.setScaleX(rightScale);
                mContent.setScaleY(rightScale);

            } else {

                mContent.setTranslationX(-mMenu.getMeasuredWidth() * slideOffset);
                mContent.setPivotX(mContent.getMeasuredWidth());
                mContent.setPivotY(mContent.getMeasuredHeight() / 2);
                mContent.invalidate();
                mContent.setScaleX(rightScale);
                mContent.setScaleY(rightScale);

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

}
