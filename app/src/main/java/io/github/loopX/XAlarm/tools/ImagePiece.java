package io.github.loopX.XAlarm.tools;

import android.graphics.Bitmap;

public class ImagePiece {

    private int index;
    private Bitmap bitmap;

    public ImagePiece() {

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
