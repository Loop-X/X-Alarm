package io.github.loop_x.yummywakeup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import io.github.loop_x.yummywakeup.infrastructure.BaseActivity;
import io.github.loop_x.yummywakeup.module.mainpage.ResideMenu;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "yummywakeup.MainActivity";
    private ResideMenu resideMenu;
    private View openRightDrawerView;
    private View openLeftDrawerView;



    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onViewInitial() {
        resideMenu = new ResideMenu(this,R.layout.main_left_menu,R.layout.main_right_menu);
        resideMenu.setBackground(R.color.loopX_2);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        resideMenu.setScaleValue(0.5f);
        
        openRightDrawerView = findViewById(R.id.openRightDrawer);
        openLeftDrawerView = findViewById(R.id.openLeftDrawer);

        openLeftDrawerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });

        openRightDrawerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }

    @Override
    public void onRefreshData() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
    /*    if (v == itemModelNormal) {
            Toast.makeText(MainActivity.this, "Normal", Toast.LENGTH_SHORT).show();

        } else if (v == itemModeMath) {
            Toast.makeText(MainActivity.this, "Math", Toast.LENGTH_SHORT).show();

        } else if (v == itemModePaint) {
            Toast.makeText(MainActivity.this, "Paint", Toast.LENGTH_SHORT).show();

        } else if (v == itemModeShake) {
            Toast.makeText(MainActivity.this, "Shake", Toast.LENGTH_SHORT).show();
        }*/
    }


    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }


    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(MainActivity.this, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(MainActivity.this, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };
}
