package io.github.loop_x.yummywakeup.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.UIUtils;
import io.github.loop_x.yummywakeup.module.AlarmModule.model.DaysOfWeek;

/**
 * Author UFreedom
 * Date : 2016 六月 10
 */
public class DayOfWeekSelectorView extends FrameLayout implements View.OnClickListener{

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
                    .setRippleDirection(RippleBackgroundView.RippleDirection.EXPAND)
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
                    .setRippleDirection(RippleBackgroundView.RippleDirection.EXPAND)
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
            case DaysOfWeek.MONDAY:
                return "MON";
            case DaysOfWeek.TUESDAY:
                    return "TUE";
            case DaysOfWeek.WEDNESDAY:
                return "WED";
            case DaysOfWeek.THURSDAY:
                return "THU";
            case DaysOfWeek.FRIDAY:
                return "FRI";
            case DaysOfWeek.SATURDAY:
                return "SAT";
            case DaysOfWeek.SUNDAY:
                return "SUN";
            default:
                return "NONE";
        }
    }
    public  DayOfWeekSelectorListener dayOfWeekSelectorListener;

    public void setDayOfWeekSelectorListener(DayOfWeekSelectorListener dayOfWeekSelectorListener) {
        this.dayOfWeekSelectorListener = dayOfWeekSelectorListener;
    }


    public interface DayOfWeekSelectorListener {
        public void onDayOfWeekSelector(int dayOfWeek,boolean selected);
    }
}
