package com.example.user.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class Game extends View {
    AlphaBeta alpha = new AlphaBeta(0);
    private Cell[][] singlesquare = null;
    int x = 15;
    int y = 15;
    private int l;
    private int a;
    private boolean whatdrawn = false;
    private int playerwin = 5;
    private Paint caneta;
    public int x_r= -1;
    public int y_r = -1;
    public int x_r2 =-1;
    public int y_r2 = -1;
    private boolean done = false;
    public boolean hardmode = true;

    Handler handler = new Handler() {
        // @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    invalidate();
                    break;
                case 1:
                    Toast.makeText(getContext(), "You Win!",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getContext(), "Computer Win!",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getContext(), "Loose!",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    public int getGameSize() {
        return x;
    }

    public Game(Context context) {
        super(context);

        caneta = new Paint();
        this.caneta.setARGB(255, 0, 0, 0);
        this.caneta.setAntiAlias(true);
        this.caneta.setStyle(Style.STROKE);
        this.caneta.setStrokeWidth(5);
        this.setBackgroundColor(0x663300);

        l = this.getWidth();
        a = this.getHeight();

        singlesquare = new Cell[x][y];

        int xss = l / x;
        int yss = a / y;

        for (int z = 0; z < y; z++) {
            for (int i = 0; i < x; i++) {
                singlesquare[z][i] = new Empty(xss * i, z * yss);
            }
        }
    }

    public Cell[][] getBoard()
    {
        return singlesquare;
    }
    public void putX(int i,int j)
    {
        singlesquare[i][j] = new Cross(j,i);
    }
    public void putO(int i,int j)
    {
        singlesquare[i][j] = new Circle(j,i);
    }
    public void remove(int i,int j)
    {
        singlesquare[i][j] = new Empty(j,i);
    }
    public boolean validPosition(int i, int j)
    {
        if(i>=0&&i<x&&j>=0&&j<y)
            return true;
        return false;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < singlesquare.length; i++) {
            for (int j = 0; j < singlesquare[0].length; j++) {
                singlesquare[i][j].draw(canvas, getResources(), j, i, (this
                        .getWidth() + 3)
                        / singlesquare.length, this.getHeight()
                        / singlesquare[0].length);
            }
        }

        int xs = this.getWidth() / x;
        int ys = this.getHeight() / y;
        for (int i = 0; i <= x; i++) {
            canvas.drawLine(xs * i, 0, xs * i, this.getHeight(), caneta);
        }
        for (int i = 0; i <= y; i++) {
            canvas.drawLine(0, ys * i, this.getWidth(), ys * i, caneta);
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x_aux = (int) (event.getX() / (this.getWidth() / x));
        int y_aux = (int) (event.getY() / (this.getHeight() / y));
        drawReady(x_aux, y_aux);
        return super.onTouchEvent(event);
    }

    public void drawReady(int x_aux, int y_aux)
    {
        for(int i=0;i<x;i++)
        {
            for(int j=0;j<y;j++)
            {
                if(singlesquare[i][j].toString()=="R")
                {
                    singlesquare[i][j] = new Empty(y_aux,x_aux);
                }
            }
        }
        if(singlesquare[y_aux][x_aux].toString()==" ") {
            Cell cel = new Ready(singlesquare[x_aux][y_aux].x, singlesquare[x_aux][y_aux].y);
            singlesquare[y_aux][x_aux] = cel;
            handler.sendMessage(Message.obtain(handler, 0));

            x_r = x_aux;
            y_r = y_aux;

            done = true;
        }
    }
    public void computerTurn()
    {
        Cross c = alpha.alphabeta(this);

        x_r2 = c.x;
        y_r2 = c.y;


        drawimage(c.y,c.x);
    }

    public void drawimage(int x_aux, int y_aux) {
        Cell cel = null;
        x_r= x_aux;
        y_r = y_aux;
        if (whatdrawn)
        {
            cel = new Cross(singlesquare[x_aux][y_aux].x,singlesquare[x_aux][y_aux].y);
            whatdrawn = false;
        }
        else
        {
            cel = new Circle(singlesquare[x_aux][y_aux].x,
                    singlesquare[x_aux][y_aux].y);
            whatdrawn = true;
        }

        singlesquare[y_aux][x_aux] = cel;
        if(singlesquare[y_aux][x_aux].toString().equals("X"))

        handler.sendMessage(Message.obtain(handler, 0));
        done = false;
    }

    public boolean isO(int x, int y)
    {
        if(singlesquare[x][y].toString().equals("O"))
            return true;
        return false;
    }
    public boolean isX(int x, int y)
    {
        if(singlesquare[x][y].toString().equals("X"))
            return true;
        return false;
    }
    public boolean isEmpty(int x, int y)
    {
        if(singlesquare[x][y].toString().equals(" "))
            return true;
        return false;
    }
    public boolean xWins(int x, int y)
    {
        if(!singlesquare[x][y].toString().equals("X"))
            return false;
        int count =0;
        if(x>=4){
            for(int i=1; i<5;i++)
            {
                if(!isX(x - i, y))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==4) return true;
        }
        if(x<11){
            for(int i=1; i<5;i++)
            {
                if(!isX(x + i, y))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==4) return true;
        }
        if(y>=4)
        {
            for(int i=1; i<5;i++)
            {
                if(!isX(x, y - i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==4) return true;
        }
        if(y<11)
        {
            for(int i=1; i<5;i++)
            {
                if(!isX(x, y + i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==4) return true;
        }
        if(x>=4&&y>=4)
        {
            for(int i=1; i<5;i++)
            {
                if(!isX(x - i, y - i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==4) return true;
        }
        if(x<11&&y<11)
        {
            for(int i=1; i<5;i++)
            {
                if(!isX(x + i, y + i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==4) return true;
        }
        if(x>=4&&y<11)
        {
            for(int i=1; i<5;i++)
            {
                if(!isX(x - i, y + i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==4) return true;
        }
        if(x<11&&y>=4)
        {
            for(int i=1; i<5;i++)
            {
                if(!isX(x + i, y - i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==4) return true;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////
        if(x>=3&&x<14)
        {
            for(int i=-1; i<4;i++)
            {
                if(!isX(x - i, y))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;
        }
        if(x<12&&x>0)
        {
            for(int i=-1; i<4;i++)
            {
                if(!isX(x + i, y))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;
        }
        if(y>=3&&y<14)
        {
            for(int i=-1; i<4;i++)
            {
                if(!isX(x, y - i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;
        }
        if(y<12&&y>0)
        {
            for(int i=-1; i<4;i++)
            {
                if(!isX(x, y + i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;
        }
        if(x>=3&&y>=3&&x<14&&y<14)
        {
            for(int i=-1; i<4;i++)
            {
                if(!isX(x - i, y - i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;
        }
        if(x<12&&y<12&&x>0&&y>0)
        {
            for(int i=-1; i<4;i++)
            {
                if(!isX(x + i, y + i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;
        }
        if(x>=3&&y<12&&x<14&&y>0)
        {
            for(int i=-1; i<4;i++)
            {
                if(!isX(x - i, y + i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;
        }
        if(y>=3&&x<12&&y<14&&x>0)
        {
            for(int i=-1; i<4;i++)
            {
                if(!isX(x + i, y - i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;
        }
        /////////////////////////////////////////////////////////////////////////////////////////////
        if(x>=2&&x<13)
        {
            for(int i=-2; i<3;i++)
            {
                if(!isX(x - i, y))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;

            for(int i=-2; i<3;i++)
            {
                if(!isX(x + i, y))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;
        }
        if(y>=2&&y<13)
        {
            for(int i=-2; i<3;i++)
            {
                if(!isX(x, y - i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;


            for(int i=-2; i<3;i++)
            {
                if(!isX(x, y + i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;
        }
        if(x>=2&&y>=2&&x<13&&y<13)
        {
            for(int i=-2; i<3;i++)
            {
                if(!isX(x - i, y - i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;


            for(int i=-2; i<3;i++)
            {
                if(!isX(x + i, y + i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;


            for(int i=-2; i<3;i++)
            {
                if(!isX(x - i, y + i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;


            for(int i=-2; i<3;i++)
            {
                if(!isX(x + i, y - i))
                {
                    count =0;
                    break;
                }
                else count++;
            }
            if(count==5) return true;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        return false;
    }
    public boolean oWinS(int x,int y)
    {
        if (isO(x, y)) {
            if (validPosition(x + 1, y) && validPosition(x + 2, y) && validPosition(x + 3, y) && validPosition(x + 4, y))
                if (isO(x + 1, y) && isO(x + 2, y) && isO(x + 3, y) && isO(x + 4, y))
                    return true;
            if (validPosition(x - 1, y) && validPosition(x + 2, y) && validPosition(x + 3, y) && validPosition(x + 1, y))
                if (isO(x + 1, y) && isO(x + 2, y) && isO(x + 3, y) &&isO(x -1 , y))
                    return true;
            if (validPosition(x - 2, y) && validPosition(x - 1, y) && validPosition(x + 1, y) && validPosition(x + 2, y))
                if (isO(x + 1, y) && isO(x + 2, y) && isO(x -2, y) && isO(x -1, y))
                    return true;
            if (validPosition(x - 2, y) && validPosition(x - 1, y) && validPosition(x - 3, y) && validPosition(x - 4, y))
                if (isO(x - 4, y) && isO(x - 3, y) && isO(x -2, y) && isO(x -1, y))
                    return true;
            if (validPosition(x - 2, y) && validPosition(x - 1, y) && validPosition(x - 3, y) && validPosition(x +1, y))
                if (isO(x +1, y) && isO(x - 3, y) && isO(x -2, y) && isO(x -1, y))
                    return true;

            if (validPosition(x, y - 1) && validPosition(x, y - 2) && validPosition(x, y - 3) && validPosition(x, y - 4))
                if (isO(x, y - 1) && isO(x, y - 2) && isO(x, y - 3) && isO(x, y - 4))
                    return true;
            if (validPosition(x, y - 1) && validPosition(x, y - 2) && validPosition(x, y - 3) && validPosition(x, y +1))
                if (isO(x, y - 1) && isO(x, y - 2) && isO(x, y - 3) && isO(x, y +1))
                    return true;
            if (validPosition(x, y - 1) && validPosition(x, y - 2) && validPosition(x, y +2) && validPosition(x, y +1))
                if (isO(x, y - 1) && isO(x, y - 2) && isO(x, y +2) && isO(x, y +1))
                    return true;
            if (validPosition(x, y + 1) && validPosition(x, y + 2) && validPosition(x, y + 3) && validPosition(x, y +4))
                if (isO(x, y + 1) && isO(x, y + 2) && isO(x, y + 3) && isO(x, y +4))
                    return true;
            if (validPosition(x, y - 1) && validPosition(x, y + 1) && validPosition(x, y + 2) && validPosition(x, y +3))
                if (isO(x, y - 1) && isO(x, y + 1) && isO(x, y + 2) && isO(x, y +3))
                    return true;

            if (validPosition(x - 1, y - 1) && validPosition(x - 2, y - 2) && validPosition(x - 3, y - 3) && validPosition(x - 4, y - 4))
                if (isO(x - 1, y - 1) && isO(x - 2, y - 2) && isO(x - 3, y - 3) && isO(x - 4, y - 4))
                    return true;
            if (validPosition(x - 1, y - 1) && validPosition(x - 2, y - 2) && validPosition(x - 3, y - 3) && validPosition(x +1, y +1))
                if (isO(x - 1, y - 1) && isO(x - 2, y - 2) && isO(x - 3, y - 3) && isO(x +1, y +1))
                    return true;
            if (validPosition(x - 1, y - 1) && validPosition(x - 2, y - 2) && validPosition(x +1, y +1) && validPosition(x +2, y +2))
                if (isO(x - 1, y - 1) && isO(x - 2, y - 2) && isO(x +1, y+1) && isO(x +2, y +2))
                    return true;
            if (validPosition(x - 1, y - 1) && validPosition(x +3, y +3) && validPosition(x +1, y +1) && validPosition(x +2, y +2))
                if (isO(x - 1, y - 1) && isO(x +3, y +3) && isO(x +1, y+1) && isO(x +2, y +2))
                    return true;
            if (validPosition(x + 4, y + 4) && validPosition(x +3, y +3) && validPosition(x +1, y +1) && validPosition(x +2, y +2))
                if (isO(x +4, y +4) && isO(x +3, y +3) && isO(x +1, y+1) && isO(x +2, y +2))
                    return true;



            if (validPosition(x - 1, y + 1) && validPosition(x - 2, y + 2) && validPosition(x - 3, y + 3) && validPosition(x - 4, y + 4))
                if (isO(x - 1, y + 1) && isO(x - 2, y + 2) && isO(x - 3, y + 3) && isO(x - 4, y + 4))
                    return true;
            if (validPosition(x - 1, y + 1) && validPosition(x - 2, y + 2) && validPosition(x - 3, y + 3) && validPosition(x + 1, y - 1))
                if (isO(x - 1, y + 1) && isO(x - 2, y + 2) && isO(x - 3, y + 3) && isO(x + 1, y - 1))
                    return true;
            if (validPosition(x - 1, y + 1) && validPosition(x - 2, y + 2) && validPosition(x + 1, y -1) && validPosition(x +2, y -2))
                if (isO(x - 1, y + 1) && isO(x - 2, y + 2) && isO(x + 1, y - 1) && isO(x + 2, y - 2))
                    return true;
            if (validPosition(x - 1, y + 1) && validPosition(x + 1, y - 1) && validPosition(x + 2, y - 2) && validPosition(x + 3, y - 3))
                if (isO(x - 1, y + 1) && isO(x + 1, y - 1) && isO(x + 3, y - 3) && isO(x + 2, y - 2))
                    return true;
            if (validPosition(x + 1, y - 1) && validPosition(x + 2, y - 2) && validPosition(x + 3, y - 3) && validPosition(x + 4, y - 4))
                if (isO(x + 1, y - 1) && isO(x + 2, y - 2) && isO(x + 3, y - 3) && isO(x + 4, y - 4))
                    return true;


        }
        return false;
    }


    public boolean isIllegal(int x, int y)
    {
        if (validPosition(x + 1, y) && validPosition(x + 2, y) && validPosition(x + 3, y) && validPosition(x + 4, y)&& validPosition(x + 5, y))
            if (isO(x + 1, y) && isO(x + 2, y) && isO(x + 3, y) && isO(x + 4, y)&& isO(x + 5, y))
                return true;
        if (validPosition(x - 1, y) && validPosition(x + 2, y) && validPosition(x + 3, y) && validPosition(x + 1, y)&& validPosition(x + 4, y))
            if (isO(x + 1, y) && isO(x + 2, y) && isO(x + 3, y) &&isO(x -1 , y)&& isO(x + 4, y))
                return true;
        if (validPosition(x - 2, y) && validPosition(x - 1, y) && validPosition(x + 1, y) && validPosition(x + 2, y)&& validPosition(x + 3, y))
            if (isO(x + 1, y) && isO(x + 2, y) && isO(x -2, y) && isO(x -1, y)&& isO(x + 3, y))
                return true;
        if (validPosition(x - 2, y) && validPosition(x - 1, y) && validPosition(x - 3, y) && validPosition(x - 4, y)&& validPosition(x -5, y))
            if (isO(x - 4, y) && isO(x - 3, y) && isO(x -2, y) && isO(x -1, y)&& isO(x - 5, y))
                return true;
        if (validPosition(x - 2, y) && validPosition(x - 1, y) && validPosition(x - 3, y) && validPosition(x +1, y)&& validPosition(x - 4, y))
            if (isO(x +1, y) && isO(x - 3, y) && isO(x -2, y) && isO(x -1, y)&& isO(x - 4, y))
                return true;

        if (validPosition(x, y - 1) && validPosition(x, y - 2) && validPosition(x, y - 3) && validPosition(x, y - 4)&& validPosition(x, y - 5))
            if (isO(x, y - 1) && isO(x, y - 2) && isO(x, y - 3) && isO(x, y - 4)&& isO(x, y - 5))
                return true;
        if (validPosition(x, y - 1) && validPosition(x, y - 2) && validPosition(x, y - 3) && validPosition(x, y +1)&& validPosition(x, y - 4))
            if (isO(x, y - 1) && isO(x, y - 2) && isO(x, y - 3) && isO(x, y +1)&& isO(x, y - 4))
                return true;
        if (validPosition(x, y - 1) && validPosition(x, y - 2) && validPosition(x, y +2) && validPosition(x, y +1)&& validPosition(x, y - 3))
            if (isO(x, y - 1) && isO(x, y - 2) && isO(x, y +2) && isO(x, y +1)&& isO(x, y - 3))
                return true;
        if (validPosition(x, y + 1) && validPosition(x, y + 2) && validPosition(x, y + 3) && validPosition(x, y +4)&& validPosition(x, y + 5))
            if (isO(x, y + 1) && isO(x, y + 2) && isO(x, y + 3) && isO(x, y +4)&& isO(x, y + 5))
                return true;
        if (validPosition(x, y - 1) && validPosition(x, y + 1) && validPosition(x, y + 2) && validPosition(x, y +3)&& validPosition(x, y + 4))
            if (isO(x, y - 1) && isO(x, y + 1) && isO(x, y + 2) && isO(x, y +3)&& isO(x, y + 4))
                return true;

        if (validPosition(x - 1, y - 1) && validPosition(x - 2, y - 2) && validPosition(x - 3, y - 3) && validPosition(x - 4, y - 4)&& validPosition(x - 5, y - 5))
            if (isO(x - 1, y - 1) && isO(x - 2, y - 2) && isO(x - 3, y - 3) && isO(x - 4, y - 4)&& isO(x - 5, y - 5))
                return true;
        if (validPosition(x - 1, y - 1) && validPosition(x - 2, y - 2) && validPosition(x - 3, y - 3) && validPosition(x +1, y +1)&& validPosition(x - 4, y - 4))
            if (isO(x - 1, y - 1) && isO(x - 2, y - 2) && isO(x - 3, y - 3) && isO(x +1, y +1)&& isO(x - 4, y - 4))
                return true;
        if (validPosition(x - 1, y - 1) && validPosition(x - 2, y - 2) && validPosition(x +1, y +1) && validPosition(x +2, y +2)&& validPosition(x - 3, y - 3))
            if (isO(x - 1, y - 1) && isO(x - 2, y - 2) && isO(x +1, y+1) && isO(x +2, y +2)&& isO(x - 3, y - 3))
                return true;
        if (validPosition(x - 1, y - 1) && validPosition(x +3, y +3) && validPosition(x +1, y +1) && validPosition(x +2, y +2)&& validPosition(x - 2, y - 2))
            if (isO(x - 1, y - 1) && isO(x +3, y +3) && isO(x +1, y+1) && isO(x +2, y +2)&& isO(x - 2, y - 2))
                return true;
        if (validPosition(x + 4, y + 4) && validPosition(x +3, y +3) && validPosition(x +1, y +1) && validPosition(x +2, y +2)&& validPosition(x + 5, y + 5))
            if (isO(x +4, y +4) && isO(x +3, y +3) && isO(x +1, y+1) && isO(x +2, y +2)&& isO(x +5, y +5))
                return true;



        if (validPosition(x - 1, y + 1) && validPosition(x - 2, y + 2) && validPosition(x - 3, y + 3) && validPosition(x - 4, y + 4)&& validPosition(x - 5, y + 5))
            if (isO(x - 1, y + 1) && isO(x - 2, y + 2) && isO(x - 3, y + 3) && isO(x - 4, y + 4)&& isO(x - 5, y + 5))
                return true;
        if (validPosition(x - 1, y + 1) && validPosition(x - 2, y + 2) && validPosition(x - 3, y + 3) && validPosition(x + 1, y - 1)&& validPosition(x - 4, y + 4))
            if (isO(x - 1, y + 1) && isO(x - 2, y + 2) && isO(x - 3, y + 3) && isO(x + 1, y - 1)&& isO(x - 4, y + 4))
                return true;
        if (validPosition(x - 1, y + 1) && validPosition(x - 2, y + 2) && validPosition(x + 1, y -1) && validPosition(x +2, y -2)&& validPosition(x - 3, y + 3))
            if (isO(x - 1, y + 1) && isO(x - 2, y + 2) && isO(x + 1, y - 1) && isO(x + 2, y - 2)&& isO(x - 3, y + 3))
                return true;
        if (validPosition(x - 1, y + 1) && validPosition(x + 1, y - 1) && validPosition(x + 2, y - 2) && validPosition(x + 3, y - 3)&& validPosition(x - 2, y + 2))
            if (isO(x - 1, y + 1) && isO(x + 1, y - 1) && isO(x + 3, y - 3) && isO(x + 2, y - 2)&& isO(x - 2, y + 2))
                return true;
        if (validPosition(x + 1, y - 1) && validPosition(x + 2, y - 2) && validPosition(x + 3, y - 3) && validPosition(x + 4, y - 4)&& validPosition(x + 5, y - 5))
            if (isO(x + 1, y - 1) && isO(x + 2, y - 2) && isO(x + 3, y - 3) && isO(x + 4, y - 4)&& isO(x +5, y -5))
                return true;
        //////////////////////////////////////////////////////////Above is checking 6 in row


        /////X+,X- 3-3
        if((validPosition(x+1,y)&&validPosition(x+2,y)&&validPosition(x-1,y)&&validPosition(x+3,y)&&isEmpty(x-1,y)&&isEmpty(x+3,y)&&isO(x+1,y)&&isO(x+2,y))
                ||(validPosition(x+2,y)&&validPosition(x+3,y)&&isO(x+2,y)&&isO(x+3,y))
                ||(validPosition(x-1,y)&&validPosition(x-2,y)&&isO(x-1,y)&&isO(x-2,y))
                ||(validPosition(x-2,y)&&validPosition(x-3,y)&&isO(x-2,y)&&isO(x-3,y)))
        {
            if(validPosition(x,y+1)&&validPosition(x,y+2)&&isO(x, y + 1)&&isO(x,y+2))
                return true;
            if(validPosition(x,y+2)&&validPosition(x,y+3)&&isO(x,y+2)&&isO(x,y+3))
                return true;

            if(validPosition(x,y-1)&&validPosition(x,y-2)&&isO(x,y-1)&&isO(x,y-2))
                return true;
            if(validPosition(x,y-2)&&validPosition(x,y-3)&&isO(x,y-2)&&isO(x,y-3))
                return true;

            if(validPosition(x+1,y+1)&&validPosition(x+2,y+2)&&isO(x+1,y+1)&&isO(x+2,y+2))
                return true;
            if(validPosition(x+2,y+2)&&validPosition(x+3,y+3)&&isO(x+2,y+2)&&isO(x+3,y+3))
                return true;

            if(validPosition(x-1,y-1)&&validPosition(x-2,y-2)&&isO(x-1,y-1)&&isO(x-2,y-2))
                return true;
            if(validPosition(x-2,y-2)&&validPosition(x-3,y-3)&&isO(x-2,y-2)&&isO(x-3,y-3))
                return true;

            if(validPosition(x+1,y-1)&&validPosition(x+2,y-2)&&isO(x+1,y-1)&&isO(x+2,y-2))
                return true;
            if(validPosition(x+2,y-2)&&validPosition(x+3,y-3)&&isO(x+2,y-2)&&isO(x+3,y-3))
                return true;

            if(validPosition(x-1,y+1)&&validPosition(x-2,y+2)&&isO(x-1,y+1)&&isO(x-2,y+2))
                return true;
            if(validPosition(x-2,y+2)&&validPosition(x-3,y+3)&&isO(x-2,y+2)&&isO(x-3,y+3))
                return true;
        }
        ////////Y+,Y- 3-3
        if((validPosition(x,y+1)&&validPosition(x,y+2)&&isO(x,y+1)&&isO(x,y+2))||(validPosition(x,y+2)&&validPosition(x,y+3)&&isO(x,y+2)&&isO(x,y+3))
                ||(validPosition(x,y-1)&&validPosition(x,y-2)&&isO(x,y-1)&&isO(x,y-2))||(validPosition(x,y-2)&&validPosition(x,y-3)&&isO(x,y-2)&&isO(x,y-3)))
        {
            if(validPosition(x+1,y)&&validPosition(x+2,y)&&isO(x+1, y)&&isO(x+2,y))
                return true;
            if(validPosition(x+2,y)&&validPosition(x+3,y)&&isO(x+2,y)&&isO(x+3,y))
                return true;

            if(validPosition(x-1,y)&&validPosition(x-2,y)&&isO(x-1,y)&&isO(x-2,y))
                return true;
            if(validPosition(x-2,y)&&validPosition(x-3,y)&&isO(x-2,y)&&isO(x-3,y))
                return true;

            if(validPosition(x+1,y+1)&&validPosition(x+2,y+2)&&isO(x+1,y+1)&&isO(x+2,y+2))
                return true;
            if(validPosition(x+2,y+2)&&validPosition(x+3,y+3)&&isO(x+2,y+2)&&isO(x+3,y+3))
                return true;

            if(validPosition(x-1,y-1)&&validPosition(x-2,y-2)&&isO(x-1,y-1)&&isO(x-2,y-2))
                return true;
            if(validPosition(x-2,y-2)&&validPosition(x-3,y-3)&&isO(x-2,y-2)&&isO(x-3,y-3))
                return true;

            if(validPosition(x+1,y-1)&&validPosition(x+2,y-2)&&isO(x+1,y-1)&&isO(x+2,y-2))
                return true;
            if(validPosition(x+2,y-2)&&validPosition(x+3,y-3)&&isO(x+2,y-2)&&isO(x+3,y-3))
                return true;

            if(validPosition(x-1,y+1)&&validPosition(x-2,y+2)&&isO(x-1,y+1)&&isO(x-2,y+2))
                return true;
                return true;
        }
        ////////////////////////////////X+,Y+, X-, Y- 3-3
        if((validPosition(x+1,y+1)&&validPosition(x+2,y+2)&&isO(x+1,y+1)&&isO(x+2,y+2))||(validPosition(x+2,y+2)&&validPosition(x+3,y+3)&&isO(x+2,y+2)&&isO(x+3,y+3))
                ||(validPosition(x-1,y-1)&&validPosition(x-2,y-2)&&isO(x-1,y-1)&&isO(x-2,y-2))||(validPosition(x-2,y-2)&&validPosition(x-3,y-3)&&isO(x-2,y-2)&&isO(x-3,y-3)))
        {
            if(validPosition(x+1,y)&&validPosition(x+2,y)&&isO(x+1, y)&&isO(x+2,y))
                return true;
            if(validPosition(x+2,y)&&validPosition(x+3,y)&&isO(x+2,y)&&isO(x+3,y))
                return true;

            if(validPosition(x-1,y)&&validPosition(x-2,y)&&isO(x-1,y)&&isO(x-2,y))
                return true;
            if(validPosition(x-2,y)&&validPosition(x-3,y)&&isO(x-2,y)&&isO(x-3,y))
                return true;

            if(validPosition(x,y+1)&&validPosition(x,y+2)&&isO(x,y+1)&&isO(x,y+2))
                return true;
            if(validPosition(x,y+2)&&validPosition(x,y+3)&&isO(x,y+2)&&isO(x,y+3))
                return true;

            if(validPosition(x,y-1)&&validPosition(x,y-2)&&isO(x,y-1)&&isO(x,y-2))
                return true;
            if(validPosition(x,y-2)&&validPosition(x,y-3)&&isO(x,y-2)&&isO(x,y-3))
                return true;

            if(validPosition(x+1,y-1)&&validPosition(x+2,y-2)&&isO(x+1,y-1)&&isO(x+2,y-2))
                return true;
            if(validPosition(x+2,y-2)&&validPosition(x+3,y-3)&&isO(x+2,y-2)&&isO(x+3,y-3))
                return true;

            if(validPosition(x-1,y+1)&&validPosition(x-2,y+2)&&isO(x-1,y+1)&&isO(x-2,y+2))
                return true;
            if(validPosition(x-2,y+2)&&validPosition(x-3,y+3)&&isO(x-2,y+2)&&isO(x-3,y+3))
                return true;
        }
        //////////////////  X+,Y-, X-,Y+ 3-3
        if((validPosition(x-1,y+1)&&validPosition(x-2,y+2)&&isO(x-1,y+1)&&isO(x-2,y+2))||(validPosition(x-2,y+2)&&validPosition(x-3,y+3)&&isO(x-2,y+2)&&isO(x-3,y+3))
                ||(validPosition(x+1,y-1)&&validPosition(x+2,y-2)&&isO(x+1,y-1)&&isO(x+2,y-2))||(validPosition(x+2,y-2)&&validPosition(x+3,y-3)&&isO(x+2,y-2)&&isO(x+3,y-3)))
        {
            if(validPosition(x+1,y)&&validPosition(x+2,y)&&isO(x+1, y)&&isO(x+2,y))
                return true;
            if(validPosition(x+2,y)&&validPosition(x+3,y)&&isO(x+2,y)&&isO(x+3,y))
                return true;

            if(validPosition(x-1,y)&&validPosition(x-2,y)&&isO(x-1,y)&&isO(x-2,y))
                return true;
            if(validPosition(x-2,y)&&validPosition(x-3,y)&&isO(x-2,y)&&isO(x-3,y))
                return true;

            if(validPosition(x+1,y+1)&&validPosition(x+2,y+2)&&isO(x+1,y+1)&&isO(x+2,y+2))
                return true;
            if(validPosition(x+2,y+2)&&validPosition(x+3,y+3)&&isO(x+2,y+2)&&isO(x+3,y+3))
                return true;

            if(validPosition(x-1,y-1)&&validPosition(x-2,y-2)&&isO(x-1,y-1)&&isO(x-2,y-2))
                return true;
            if(validPosition(x-2,y-2)&&validPosition(x-3,y-3)&&isO(x-2,y-2)&&isO(x-3,y-3))
                return true;

            if(validPosition(x,y-1)&&validPosition(x,y-2)&&isO(x,y-1)&&isO(x,y-2))
                return true;
            if(validPosition(x,y-2)&&validPosition(x,y-3)&&isO(x,y-2)&&isO(x,y-3))
                return true;

            if(validPosition(x,y+1)&&validPosition(x,y+2)&&isO(x,y+1)&&isO(x,y+2))
                return true;
            if(validPosition(x,y+2)&&validPosition(x,y+3)&&isO(x,y+2)&&isO(x,y+3))
                return true;
        }
        return false;
    }
    public boolean isFull() {
        for (int i = 0; i < singlesquare.length; i++) {
            for (int j = 0; j < singlesquare[0].length; j++) {
                if (singlesquare[i][j] instanceof Empty) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean renjuRule()
    {
        return false;
    }

    public int getPlayerwin() {
        return playerwin;
    }
}
