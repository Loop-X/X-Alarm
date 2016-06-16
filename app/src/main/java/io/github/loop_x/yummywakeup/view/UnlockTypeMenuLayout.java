package io.github.loop_x.yummywakeup.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.module.UnlockTypeModule.UnlockTypeEnum;

/**
 * Author UFreedom
 * Date : 2016 六月 10
 */
public class UnlockTypeMenuLayout extends LinearLayout implements View.OnClickListener {

    private View normalLayout;
    private View mathLayout;
    private View paintLayout;
    private View shakeLayout;

    public UnlockTypeMenuLayout(Context context) {
        this(context, null);
    }

    public UnlockTypeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UnlockTypeMenuLayout(Context context, AttributeSet attrs) {
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
         
        UnlockTypeEnum unlockType = UnlockTypeEnum.Normal;
        
        switch (v.getId()){
            case R.id.normalLayout:
                unlockType = UnlockTypeEnum.Normal;
                break;
            case R.id.mathLayout:
                unlockType = UnlockTypeEnum.Math;
                break;
            case R.id.paintLayout:
                unlockType = UnlockTypeEnum.Puzzle;
                break;
            case R.id.shakeLayout:
                unlockType = UnlockTypeEnum.Shake;
                break;
        }
        
        if (onUnlockTypeMenuClickListener != null){
            onUnlockTypeMenuClickListener.onClick(unlockType);
        }
        
    }

    public void setOnUnlockTypeMenuClickListener(OnUnlockTypeMenuClickListener onUnlockTypeMenuClickListener) {
        this.onUnlockTypeMenuClickListener = onUnlockTypeMenuClickListener;
    }

    private  OnUnlockTypeMenuClickListener onUnlockTypeMenuClickListener;
    
    public interface OnUnlockTypeMenuClickListener{
        public void  onClick(UnlockTypeEnum unlockTypeEnum);
    }
}
