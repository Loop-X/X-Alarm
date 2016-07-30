package io.github.loopX.XAlarm;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.loopX.XAlarm.infrastructure.BaseActivity;
import io.github.loopX.XAlarm.tools.LinksHelper;

public class AboutActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void onViewInitial() {

        // Enable links in text
        LinearLayout linksHolder = (LinearLayout) findViewById(R.id.ll_about);
        for( int i = 0; i < linksHolder.getChildCount(); i++ ) {
            TextView child = (TextView) linksHolder.getChildAt(i);
            LinksHelper.enableLinks(child, true);
        }

        TextView tvEmail = (TextView) findViewById(R.id.tv_email);
        LinksHelper.enableLinks(tvEmail, true);

    }

    @Override
    public void onRefreshData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_about_ok:
                finish();
                break;
        }
    }
}
