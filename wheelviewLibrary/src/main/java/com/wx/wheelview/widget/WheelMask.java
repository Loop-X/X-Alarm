package com.wx.wheelview.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

/**
 * Author UFreedom
 * Date : 2016 七月 02
 */
public  class WheelMask extends Drawable{
    
    protected int parentWidth;
    protected int parentHeight;
    protected int itemHeight;
    protected int wheelItemSize;

    public void setParentWidth(int parentWidth) {
        this.parentWidth = parentWidth;
    }

    public void setParentHeight(int parentHeight) {
        this.parentHeight = parentHeight;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public void setWheelItemSize(int wheelItemSize) {
        this.wheelItemSize = wheelItemSize;
    }

    public WheelMask() {
        
    }
    
    
    public void preDraw(){
        
    }

    @Override
    public void draw(Canvas canvas) {
    }
    
    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
