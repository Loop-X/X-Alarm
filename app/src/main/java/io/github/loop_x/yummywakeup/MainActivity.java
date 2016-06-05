package io.github.loop_x.yummywakeup;

import android.view.View;

import io.github.loop_x.yummywakeup.infrastructure.BaseActivity;
import io.github.loop_x.yummywakeup.view.LoopXDragMenuLayout;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "yummywakeup.MainActivity";
    private View openRightDrawerView;
    private View openLeftDrawerView;
    private LoopXDragMenuLayout loopXDragMenuLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onViewInitial() {

        loopXDragMenuLayout = (LoopXDragMenuLayout) findViewById(R.id.dragMenuLayout);
        openRightDrawerView = findViewById(R.id.openRightDrawer);
        openLeftDrawerView = findViewById(R.id.openLeftDrawer);

        openLeftDrawerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loopXDragMenuLayout.getMenuStatus() == LoopXDragMenuLayout.MenuStatus.Close){
                    loopXDragMenuLayout.openLeftMenuWithAnimation();
                }else {
                    loopXDragMenuLayout.closeLeftMenuWithAnimation();
                }
            }
        });

        openRightDrawerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (loopXDragMenuLayout.getMenuStatus() == LoopXDragMenuLayout.MenuStatus.Close){
                    loopXDragMenuLayout.openRightMenuWithAnimation();
                }else {
                    loopXDragMenuLayout.closeRightMenuWithAnimation();
                }
                
            }
        });

    }

    @Override
    public void onRefreshData() {

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_alarm_time:
            //    changeFragment(new SetAlarmFragment());
                break;
        }
    }


 /*   private void changeFragment(Fragment targetFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }*/


   
}
