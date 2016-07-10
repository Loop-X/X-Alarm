package io.github.loopX.XAlarm.module.UnlockTypeModule;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import io.github.loopX.XAlarm.R;
import io.github.loopX.XAlarm.UIUtils;
import io.github.loopX.XAlarm.infrastructure.BaseActivity;
import io.github.loopX.XAlarm.module.AlarmModule.AlarmAlertFullScreenToTest;
import io.github.loopX.XAlarm.view.RippleBackgroundView;

public class UnlockTypeActivity extends BaseActivity {

    private static final int PAGE_CONTENT = 4;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View saveLayout;
    private TextView icSaveView;
    
    public static final int MODE_TYPE = 0;
    public static final int MODE_MATH = 1;
    public static final int MODE_PAINT = 2;
    public static final int MODE_SHAKE = 3;
    private Drawable saveDrawable;
    private  int currentUnlockType;
    private RippleBackgroundView rippleBackground;
    private boolean mFistInit;

    RippleBackgroundView.RippleBuilder mSelectedRippleBuild;
    RippleBackgroundView.RippleBuilder mUnSelectedRippleBuild;

    private Timer mTimer;

    @Override
    public void onViewInitial() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        icSaveView = (TextView) findViewById(R.id.icSave);
        saveLayout = findViewById(R.id.saveLayout);
        rippleBackground  = (RippleBackgroundView) findViewById(R.id.rippleBackground);

        mSelectedRippleBuild = new RippleBackgroundView.RippleBuilder(UnlockTypeActivity.this)
                .setFinishRippleRadius(UIUtils.getScreenWidth())
                .setStartRippleRadius(0)
                .setRippleColor(getResources().getColor(R.color.loopX_2));

        mUnSelectedRippleBuild = new RippleBackgroundView.RippleBuilder(UnlockTypeActivity.this)
                .setFinishRippleRadius(0)
                .setStartRippleRadius(UIUtils.getScreenWidth())
                .setRippleColor(getResources().getColor(R.color.loopX_2));

        saveDrawable = getResources().getDrawable(R.drawable.icon_set_choose_big);
        int w = UIUtils.dip2px(40);
        saveDrawable.setBounds(0,0,w,w);
        
        saveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rippleBackground.startRipple(mSelectedRippleBuild);

                if(mTimer == null) {
                    mTimer = new Timer(true);
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            int current =  viewPager.getCurrentItem();
                            Intent intent  = new Intent(UnlockTypeActivity.this, UnlockTypeActivity.class);
                            int returnValue = convertItemPositionToUnlockTypeId(current);
                            intent.putExtra("unlockType", returnValue);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }, 150);
                }

            }
        });
    }

    private int convertItemPositionToUnlockTypeId(int current) {
        int returnValue;
        switch (current){
            case MODE_TYPE:
                returnValue = UnlockTypeEnum.Type.getID();
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
                returnValue = UnlockTypeEnum.Type.getID();
                break;
            
        }
        return returnValue;
    }
    
    private int convertUnlockTypeIdToItemPosition(int id){

        if (id == UnlockTypeEnum.Type.getID()) {
            return MODE_TYPE;
        } else if (id == UnlockTypeEnum.Math.getID()) {
            return MODE_MATH;
        } else if (id == UnlockTypeEnum.Puzzle.getID()) {
            return MODE_PAINT;
        } else if (id == UnlockTypeEnum.Shake.getID()) {
            return MODE_SHAKE;
        } else {
            return MODE_TYPE;
        }

    }

    @Override
    public void onRefreshData() {
        viewPager.setAdapter(new UnlockModePageAdapter(this));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabSelectedListener(viewPager));

        int currentItem = MODE_TYPE;
        Intent intent = getIntent();
        
        if (intent != null){
            int unlockType = intent.getIntExtra("unlockType", UnlockTypeEnum.Type.getID());
            currentItem = convertUnlockTypeIdToItemPosition(unlockType);
            currentUnlockType = intent.getIntExtra("currentUnlockType",UnlockTypeEnum.Type.getID());
        }
        mFistInit = true;
        viewPager.setCurrentItem(currentItem, false);
        
        if (currentItem == MODE_TYPE){
            
            //fix bug: onPageSelected isn't triggered when calling setCurrentItem(0)
            //so we fist let the viewpage select another page but not the MODE_TYPE.
            viewPager.setCurrentItem(MODE_TYPE +1);
            mFistInit = true;
            viewPager.setCurrentItem(MODE_TYPE);
        }
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
                case MODE_TYPE:
                    layoutResId = R.layout.unlock_mode_page_item_type;
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

        AlphaAnimation alphaAnimation;
        
        public TabSelectedListener(ViewPager viewPager) {
            super(viewPager);
            alphaAnimation = new AlphaAnimation(0f,1f);
            alphaAnimation.setDuration(500);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            super.onTabSelected(tab);

            int position = tab.getPosition();
            int unlockType = convertItemPositionToUnlockTypeId(position);
            
            if (!mFistInit){
                rippleBackground.setBackgroundDrawable(null);
                if (!icSaveView.isSelected() && currentUnlockType == unlockType){
                    rippleBackground.startRipple(mSelectedRippleBuild);
                }else if (icSaveView.isSelected() && currentUnlockType != unlockType){
                    rippleBackground.startRipple(mUnSelectedRippleBuild);
                }
            }

            if(mFistInit){
                if (currentUnlockType != unlockType){
                    rippleBackground.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.loopX_3_10_alpha)));
                }else {
                    rippleBackground.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.loopX_2)));
                }
                mFistInit = false;
            }
            
            saveLayout.setSelected(currentUnlockType == unlockType);
            icSaveView.setText(currentUnlockType == unlockType ? "" : "OK");
            icSaveView.setCompoundDrawables(currentUnlockType == unlockType ? saveDrawable : null , null,null,null);
            
            
            int resId = 0;
            switch (position) {
                case MODE_TYPE:
                    resId = R.drawable.model_type;
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
                case MODE_TYPE:
                    resId = R.drawable.model_type_grey;
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = this.getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.loopX_1));
        }
    }
}
