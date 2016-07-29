package io.github.loopX.XAlarm;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.loopX.XAlarm.infrastructure.BaseActivity;
import io.github.loopX.XAlarm.tools.LinksHelper;

public class About extends BaseActivity{

    private ImageView ivSendEmail;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void onViewInitial() {

        ivSendEmail = (ImageView) findViewById(R.id.iv_send_email);

        // Set toolbar
        Toolbar bar = (Toolbar) findViewById(R.id.tb_about);
        setSupportActionBar(bar);

        // Enable links in text
        LinearLayout linksHolder = (LinearLayout) findViewById(R.id.ll_about);
        for( int i = 0; i < linksHolder.getChildCount(); i++ ) {
            TextView child = (TextView) linksHolder.getChildAt(i);
            LinksHelper.enableLinks(child);
        }

        ivSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","email@email.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Loop-X");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
    }

    @Override
    public void onRefreshData() {

    }
}
