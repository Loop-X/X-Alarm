package io.github.loop_x.yummywakeup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import io.github.loop_x.yummywakeup.infrastructure.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "yummywakeup.MainActivity";
    private ResideMenu resideMenu;
    private ResideMenuItem itemModelNormal;
    private ResideMenuItem itemModeMath;
    private ResideMenuItem itemModePaint;
    private ResideMenuItem itemModeShake;
    private View openRightDrawerView;
    private View openLeftDrawerView;



    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onViewInitial() {
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.color.loopX_2);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);

        resideMenu.setScaleValue(0.7f);

        // create menu items;
        itemModelNormal = new ResideMenuItem(this, R.drawable.model_normal, "NORMAL");
        itemModeMath = new ResideMenuItem(this, R.drawable.model_math, "MATH");
        itemModePaint = new ResideMenuItem(this, R.drawable.model_paint, "PAINT");
        itemModeShake = new ResideMenuItem(this, R.drawable.model_shake, "SHAKE");

        itemModelNormal.setOnClickListener(this);
        itemModeMath.setOnClickListener(this);
        itemModePaint.setOnClickListener(this);
        itemModeShake.setOnClickListener(this);

        resideMenu.addMenuItem(itemModelNormal, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemModeMath, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemModePaint, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemModeShake, ResideMenu.DIRECTION_LEFT);

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
        if (v == itemModelNormal) {
            Toast.makeText(MainActivity.this, "Normal", Toast.LENGTH_SHORT).show();

        } else if (v == itemModeMath) {
            Toast.makeText(MainActivity.this, "Math", Toast.LENGTH_SHORT).show();

        } else if (v == itemModePaint) {
            Toast.makeText(MainActivity.this, "Paint", Toast.LENGTH_SHORT).show();

        } else if (v == itemModeShake) {
            Toast.makeText(MainActivity.this, "Shake", Toast.LENGTH_SHORT).show();
        }
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
