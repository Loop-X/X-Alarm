package io.github.loop_x.yummywakeup.module.UnlockTypeModule;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.infrastructure.BaseActivity;
import io.github.loop_x.yummywakeup.module.AlarmModule.AlarmAlertFullScreenToTest;

public class UnlockTypeActivity extends BaseActivity {

    private static final int PAGE_CONTENT = 4;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View saveLayout;
    
    public static final int MODE_NORMAL = 0;
    public static final int MODE_MATH = 1;
    public static final int MODE_PAINT = 2;
    public static final int MODE_SHAKE = 3;

    @Override
    public void onViewInitial() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        saveLayout = findViewById(R.id.saveLayout);
        saveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current =  viewPager.getCurrentItem();

                Intent intent  = new Intent(UnlockTypeActivity.this, UnlockTypeActivity.class);
                int returnValue = convertItemPositionToUnlockTypeId(current);
                intent.putExtra("unlockType", returnValue);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private int convertItemPositionToUnlockTypeId(int current) {
        int returnValue;
        switch (current){
            case MODE_NORMAL:
                returnValue = UnlockTypeEnum.Normal.getID();
                break;
            case MODE_MATH:
                returnValue = UnlockTypeEnum.Math.getID();
                break;
            case MODE_PAINT:
                returnValue = UnlockTypeEnum.Puzzle.getID();
                break;
            case MODE_SHAKE:
                returnValue = UnlockTypeEnum.Shake.getID();
                break;
            default:
                returnValue = UnlockTypeEnum.Normal.getID();
                break;
            
        }
        return returnValue;
    }
    
    private int convertUnlockTypeIdToItemPosition(int id){

        if (id == UnlockTypeEnum.Normal.getID()) {
            return MODE_NORMAL;
        } else if (id == UnlockTypeEnum.Math.getID()) {
            return MODE_MATH;
        } else if (id == UnlockTypeEnum.Puzzle.getID()) {
            return MODE_PAINT;
        } else if (id == UnlockTypeEnum.Shake.getID()) {
            return MODE_SHAKE;
        } else {
            return MODE_NORMAL;
        }

    }

    @Override
    public void onRefreshData() {
        viewPager.setAdapter(new UnlockModePageAdapter(this));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabSelectedListener(viewPager));
        
        int currentItem  = MODE_NORMAL;
        Intent intent = getIntent();
        
        if (intent != null){
            int unlockType = intent.getIntExtra("unlockType", UnlockTypeEnum.Normal.getID());
            currentItem = convertUnlockTypeIdToItemPosition(unlockType);
        }
        viewPager.setCurrentItem(currentItem, false);

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
                case MODE_NORMAL:
                    layoutResId = R.layout.unlock_mode_page_item_normal;
                    break;
                case MODE_MATH:
                    layoutResId = R.layout.unlock_mode_page_item_math;
                    break;
                case MODE_PAINT:
                    layoutResId = R.layout.unlock_mode_page_item_paint;
                    break;
                case MODE_SHAKE:
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
                case MODE_NORMAL:
                    resId = R.drawable.model_normal;
                    break;
                case MODE_MATH:
                    resId = R.drawable.model_math;
                    break;
                case MODE_PAINT:
                    resId = R.drawable.model_paint;
                    break;
                case MODE_SHAKE:
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
                case MODE_NORMAL:
                    resId = R.drawable.model_normal_grey;
                    break;
                case MODE_MATH:
                    resId = R.drawable.model_math_grey;
                    break;
                case MODE_PAINT:
                    resId = R.drawable.model_paint_grey;
                    break;
                case MODE_SHAKE:
                    resId = R.drawable.model_shake_grey;
                    break;
            }
            tab.setIcon(resId);
        }
        
    }

    public void runDemo(View view) {
        Intent intent = new Intent(this, AlarmAlertFullScreenToTest.class);
        intent.putExtra("UnlockType", convertItemPositionToUnlockTypeId(viewPager.getCurrentItem()));
        startActivity(intent);
    }
}
