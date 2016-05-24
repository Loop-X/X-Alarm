package io.github.loop_x.yummywakeup.infrastructure.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public abstract class BaseActivity extends AppCompatActivity implements InitActivity {

    public BaseActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("activity", "onCreate>>" + ((Object) this).getClass().getName());
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        initToolbar();
        initView();
        initData();
        initListener();

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        Log.d("activity", "onAttachFragment>>" + ((Object) this).getClass().getName());
        super.onAttachFragment(fragment);
    }

    @Override
    protected void onStart() {
        Log.d("activity", "onStart>>" + ((Object) this).getClass().getName());
        super.onStart();
    }

    /**
     * BaseFragment
     * 当Activity可见时调用
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("activity", "onPause>>" + ((Object) this).getClass().getName());
        super.onPause();
        //此处不用调用当前Fragment的onPause方法，因为按照系统的调用顺序，先调用fragment的onpause，在调用activity的。
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("activity", "onSaveInstanceState>>" + ((Object) this).getClass().getName());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("activity", "onStop>>" + ((Object) this).getClass().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("activity", "onDestory>>" + ((Object) this).getClass().getName());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();

            //TODO 直接回到主Activity，但是需要在minifest中设置，谁是要返回的主Activity
            //TODO Did you forget to add the android.support.PARENT_ACTIVITY <meta-data>  element in your manifest?
            //NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}

