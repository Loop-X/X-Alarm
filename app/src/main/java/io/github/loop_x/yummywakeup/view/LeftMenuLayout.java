package io.github.loop_x.yummywakeup.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.module.UnlockTypeModule.UnlockTypeActivity;

/**
 * Author UFreedom
 * Date : 2016 六月 10
 */
public class LeftMenuLayout extends LinearLayout implements View.OnClickListener {
    private View normalLayout;
    private View mathLayout;
    private View paintLayout;
    private View shakeLayout;

    public LeftMenuLayout(Context context) {
        this(context, null);
    }

    public LeftMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LeftMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.menu_left, this, true);

        normalLayout = findViewById(R.id.normalLayout);
        mathLayout = findViewById(R.id.mathLayout);
        paintLayout = findViewById(R.id.paintLayout);
        shakeLayout = findViewById(R.id.shakeLayout);

        normalLayout.setOnClickListener(this);
        mathLayout.setOnClickListener(this);
        paintLayout.setOnClickListener(this);
        shakeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), UnlockTypeActivity.class);
        getContext().startActivity(intent);
    }
}
