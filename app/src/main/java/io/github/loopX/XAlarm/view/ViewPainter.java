package io.github.loopX.XAlarm.view;

import android.graphics.Canvas;

/**
 * The  step to  draw view:
 * 
 * 
 * 
 *
 * 1.onPrepareDraw() : 
 *  
 *  any numerical calculation should be there.If the clock view size was changed ,this method
 *  will be invoke in order to do numerical calculation .
 *                
 * 
 * 3.draw(Canvas canvas) : draw something that you want 
 * 
 */
public interface ViewPainter {

    
    public void onPrepareDraw();

    public void draw(Canvas canvas);

}

