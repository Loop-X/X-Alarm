package io.github.loop_x.yummywakeup.module.UnlockTypeModule;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.infrastructure.BaseActivity;

/**
 * Author SunMeng
 * Date : 2016 六月 11
 */
public class UnlockTypeActivity extends BaseActivity {

    private static final int PAGE_CONTENT = 4;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onViewInitial() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    @Override
    public void onRefreshData() {
        viewPager.setAdapter(new UnlockModePageAdapter(this));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabSelectedListener(viewPager));
        viewPager.setCurrentItem(0, false);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_unlock_type;
    }

    
    public static class UnlockModePageAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public UnlockModePageAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return PAGE_CONTENT;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int layoutResId = 0;
            switch (position) {
                case 0:
                    layoutResId = R.layout.unlock_mode_page_item_normal;
                    break;
                case 1:
                    layoutResId = R.layout.unlock_mode_page_item_math;
                    break;
                case 2:
                    layoutResId = R.layout.unlock_mode_page_item_paint;
                    break;
                case 3:
                    layoutResId = R.layout.unlock_mode_page_item_shake;
                    break;
            }

            View view = layoutInflater.inflate(layoutResId, container, false);
            container.addView(view);

            return view;
        }

    }

    private class TabSelectedListener extends TabLayout.ViewPagerOnTabSelectedListener {

        public TabSelectedListener(ViewPager viewPager) {
            super(viewPager);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            super.onTabSelected(tab);
            int resId = 0;
            switch (tab.getPosition()) {
                case 0:
                    resId = R.drawable.model_normal;
                    break;
                case 1:
                    resId = R.drawable.model_math;
                    break;
                case 2:
                    resId = R.drawable.model_paint;
                    break;
                case 3:
                    resId = R.drawable.model_shake;
                    break;
            }
            tab.setIcon(resId);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            super.onTabUnselected(tab);
            int resId = 0;
            switch (tab.getPosition()) {
                case 0:
                    resId = R.drawable.model_normal_grey;
                    break;
                case 1:
                    resId = R.drawable.model_math_grey;
                    break;
                case 2:
                    resId = R.drawable.model_paint_grey;
                    break;
                case 3:
                    resId = R.drawable.model_shake_grey;
                    break;
            }
            tab.setIcon(resId);
        }
        
    }
}
