package io.github.loopX.XAlarm.module.SetAlarmModule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;

import com.wx.wheelview.widget.WheelMask;

import io.github.loopX.XAlarm.R;
import io.github.loopX.XAlarm.UIUtils;

/**
 * Author SunMeng
 * Date : 2016 七月 02
 */
public class TimePickWheelMask extends WheelMask {


    private Paint paint;
    private Paint dividerPaint;
    private Paint topMaskPaint;
    private RectF topMaskRect;
    private Paint bottomMaskPaint;
    private RectF bottomMaskRect;
    private int bgColor;
  
    public TimePickWheelMask(Context context) {
        paint = new Paint();
        bgColor = ContextCompat.getColor(context, R.color.loopX_1);
        paint.setColor(bgColor);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(UIUtils.dip2px(1));
        dividerPaint.setColor(ContextCompat.getColor(context,R.color.loopX_2));
       
        topMaskPaint = new Paint();
        topMaskPaint.setStyle(Paint.Style.FILL);

        bottomMaskPaint = new Paint();
        bottomMaskPaint.setStyle(Paint.Style.FILL);
        
        bottomMaskPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    public void preDraw() {
        super.preDraw();
        int endColor = Color.parseColor("#05000000");
        topMaskRect = new RectF(0, 0, parentWidth, itemHeight);
        topMaskPaint.setShader(new LinearGradient(parentWidth/2, itemHeight / 5, parentWidth/2, itemHeight, bgColor,endColor, Shader.TileMode.CLAMP));
        
        bottomMaskRect = new RectF(0,itemHeight * (wheelItemSize - 1),parentWidth,parentHeight);
        bottomMaskPaint.setShader(new LinearGradient(parentWidth/2, itemHeight * ( wheelItemSize - 1) , parentWidth/2, parentHeight - itemHeight / 5,endColor ,bgColor, Shader.TileMode.CLAMP));

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(topMaskRect, topMaskPaint);
        canvas.drawRect(bottomMaskRect,bottomMaskPaint);
        
        // draw select border
        if (itemHeight != 0) {
            canvas.drawLine(0, itemHeight * (wheelItemSize / 2), parentWidth, itemHeight
                    * (wheelItemSize / 2), dividerPaint);
            canvas.drawLine(0, itemHeight * (wheelItemSize / 2 + 1), parentHeight, itemHeight
                    * (wheelItemSize / 2 + 1), dividerPaint);
        }
    }
}
