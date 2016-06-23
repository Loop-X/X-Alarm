package io.github.loop_x.yummywakeup.view;

import android.graphics.Canvas;

/**
 * The  step to  draw view:
 *
 * 1.onPreDraw() : 
 *  
 *  any numerical calculation should be there.If the clock view size was changed ,this method
 *  will be invoke in order to do numerical calculation .
 *                
 * 
 * 2.onDraw(Canvas canvas) : draw something that you want 
 * 
 */
public interface ViewPainter {

    public void onPreDraw();

    public void onDraw(Canvas canvas);

}
