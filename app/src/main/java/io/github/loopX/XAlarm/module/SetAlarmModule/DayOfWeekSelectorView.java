package io.github.loopX.XAlarm.module.SetAlarmModule;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Calendar;

import io.github.loopX.XAlarm.R;
import io.github.loopX.XAlarm.UIUtils;
import io.github.loopX.XAlarm.view.RippleBackgroundView;

public class DayOfWeekSelectorView extends FrameLayout implements View.OnClickListener {

    public int daysOfWeek;
    private RippleBackgroundView rippleBackgroundView;
    private TextView dayOfWeekView;
    private int selectedRippleColor;
    private int unSelectedRippleColor;
    private float maxRippleRadius;

    public DayOfWeekSelectorView(Context context) {
        super(context);
    }

    public DayOfWeekSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DayOfWeekSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.day_of_week_selector, this, true);

        rippleBackgroundView = (RippleBackgroundView) findViewById(R.id.rippleBackground);
        dayOfWeekView = (TextView) findViewById(R.id.dayOfWeek);
        selectedRippleColor = UIUtils.getColor(R.color.loopX_2);
        unSelectedRippleColor = Color.parseColor("#FF1B1A30");
        maxRippleRadius = UIUtils.dip2px(40) / 2f;

        setOnClickListener(this);
        
    }

    public void setDay(int daysOfWeek, boolean selected) {
        this.daysOfWeek = daysOfWeek;

        dayOfWeekView.setText(getStringForDayOfWeek(daysOfWeek));
        dayOfWeekView.setSelected(selected);

        GradientDrawable gradientDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.btn_circular_normal);
        gradientDrawable.setColor(selected ? selectedRippleColor : unSelectedRippleColor);
        rippleBackgroundView.setBackgroundDrawable(gradientDrawable);
        
    }

    @Override
    public void onClick(View v) {
        if (dayOfWeekView.isSelected()) {
            //cancel select action
            dayOfWeekView.setSelected(false);

            GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.btn_circular_normal);
            drawable.setColor(selectedRippleColor);
            
            rippleBackgroundView.startRipple(new RippleBackgroundView.RippleBuilder(getContext())
                    .setRippleColor(unSelectedRippleColor)
                    .setBackgroundDrawable(drawable)
                    .setStartRippleRadius(0f)
                    .setFinishRippleRadius(maxRippleRadius)
                    .setRipplePivotX(maxRippleRadius)
                    .setRipplePivotY(maxRippleRadius)
            );

        } else {
            //select action
            dayOfWeekView.setSelected(true);

            GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.btn_circular_normal);
            drawable.setColor(unSelectedRippleColor);
            rippleBackgroundView.startRipple(new RippleBackgroundView.RippleBuilder(getContext())
                    .setRippleColor(selectedRippleColor)
                    .setBackgroundDrawable(drawable)
                    .setStartRippleRadius(0f)
                    .setFinishRippleRadius(maxRippleRadius)
                    .setRipplePivotX(maxRippleRadius)
                    .setRipplePivotY(maxRippleRadius)
            );
        }

        if (dayOfWeekSelectorListener != null) {
            dayOfWeekSelectorListener.onDayOfWeekSelector(daysOfWeek,dayOfWeekView.isSelected());
        }
    }

    private String getStringForDayOfWeek(int daysOfWeek) {
        switch (daysOfWeek) {
            case Calendar.MONDAY:
                return "MON";
            case Calendar.TUESDAY:
                return "TUE";
            case Calendar.WEDNESDAY:
                return "WED";
            case Calendar.THURSDAY:
                return "THU";
            case Calendar.FRIDAY:
                return "FRI";
            case Calendar.SATURDAY:
                return "SAT";
            case Calendar.SUNDAY:
                return "SUN";
            default:
                return "NONE";
        }
    }
    public DayOfWeekSelectorListener dayOfWeekSelectorListener;

    public void setDayOfWeekSelectorListener(DayOfWeekSelectorListener dayOfWeekSelectorListener) {
        this.dayOfWeekSelectorListener = dayOfWeekSelectorListener;
    }

    public interface DayOfWeekSelectorListener {
        void onDayOfWeekSelector(int dayOfWeek,boolean selected);
    }
}
