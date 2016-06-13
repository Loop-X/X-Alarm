package io.github.loop_x.yummywakeup;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import io.github.loop_x.yummywakeup.infrastructure.BaseActivity;
import io.github.loop_x.yummywakeup.module.SetAlarm.SetAlarmActivity;
import io.github.loop_x.yummywakeup.view.LoopXDragMenuLayout;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "yummywakeup.MainActivity";

    private static final int SET_ALARM_REQUEST_CODE = 1;

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
            // Go to Set Alarm Activity
            case R.id.im_set_alarm:
                Intent intent = new Intent(MainActivity.this, SetAlarmActivity.class);
                startActivityForResult(intent, SET_ALARM_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SET_ALARM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "SET ALARM", Toast.LENGTH_LONG).show();
                }
                break;
            default:
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
