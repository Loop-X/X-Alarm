package io.github.loopX.XAlarm.tools;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class ImageSplitUtil {

    public static List<ImagePiece> splitImage(Bitmap bitmap, int sizeOfRow) {

        List<ImagePiece> imagePieces = new ArrayList<>();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int pieceWidth = Math.min(width, height)/sizeOfRow;
        
        ImagePiece imagePiece;
        for (int i = 0; i < sizeOfRow; i++) {
            for (int j = 0; j < sizeOfRow; j++) {
                imagePiece = new ImagePiece();
                imagePiece.setIndex(j + i * sizeOfRow);

                int x = j * pieceWidth;
                int y = i * pieceWidth;
                // Returns an immutable bitmap from the specified subset of the source bitmap.
                imagePiece.setBitmap(Bitmap.createBitmap(bitmap, x, y, pieceWidth, pieceWidth));
                imagePieces.add(imagePiece);
            }
        }
        return imagePieces;
    }
}
