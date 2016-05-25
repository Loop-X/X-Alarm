package io.github.loop_x.yummywakeup.infrastructure.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements InitActivity {

    public BaseActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        initToolbar();
        initView();
        initData();
        initListener();

    }
    
}

