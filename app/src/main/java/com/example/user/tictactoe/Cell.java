package com.example.user.tictactoe;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by user on 10/22/2016.
 */
public abstract class Cell extends Point {
    public Cell(int x, int y)
    {
        super(x,y);
    }
    abstract public void draw(Canvas g,Resources res, int x, int y, int w,
                              int h);

}
