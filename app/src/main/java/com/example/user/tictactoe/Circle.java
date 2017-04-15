package com.example.user.tictactoe;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by user on 10/22/2016.
 */
public class Circle extends Cell {
    int image = R.drawable.circle;
    public Circle(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Canvas g, Resources res, int x, int y, int w, int h) {
        Bitmap im = BitmapFactory.decodeResource(res,
                image);

        g.drawBitmap(im, null, new Rect(x*w, y*h, (x*w)+w, (y*h)+h),
                new Paint());

    }

    public boolean equals(Object obj) {
        if (obj instanceof Circle) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public String toString() {
        return "O";
    }
}
