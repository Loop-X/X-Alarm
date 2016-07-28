package io.github.loopX.XAlarm;

import android.support.v7.widget.Toolbar;

import io.github.loopX.XAlarm.infrastructure.BaseActivity;

public class About extends BaseActivity{

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void onViewInitial() {
        Toolbar bar = (Toolbar) findViewById(R.id.tb_about);
        setSupportActionBar(bar);
    }

    @Override
    public void onRefreshData() {

    }
}
