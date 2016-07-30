package io.github.loopX.XAlarm;

import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.loopX.XAlarm.infrastructure.BaseActivity;
import io.github.loopX.XAlarm.tools.LinksHelper;

public class AboutActivity extends BaseActivity{

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void onViewInitial() {

        // Set toolbar
        Toolbar bar = (Toolbar) findViewById(R.id.tb_about);
        setSupportActionBar(bar);

        // Enable links in text
        LinearLayout linksHolder = (LinearLayout) findViewById(R.id.ll_about);
        for( int i = 0; i < linksHolder.getChildCount(); i++ ) {
            TextView child = (TextView) linksHolder.getChildAt(i);
            LinksHelper.enableLinks(child);
        }

        TextView tvEmail = (TextView) findViewById(R.id.tv_email);
        LinksHelper.enableLinks(tvEmail);

    }

    @Override
    public void onRefreshData() {

    }
}
