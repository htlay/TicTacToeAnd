package com.example.user.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Play extends AppCompatActivity implements View.OnClickListener {
    private Game game1;
    Button put;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game1 = new Game(this);
        setContentView(R.layout.activity_play);

        put = (Button)findViewById(R.id.button2);
        put.setOnClickListener(this);
        RelativeLayout gmView = (RelativeLayout)findViewById(R.id.gameView);
        gmView.addView(game1);
    }
    public void finish(String status)
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        AlertDialog alertDialog = new AlertDialog.Builder(Play.this).create();
        alertDialog.setTitle("Rcord");
        if(status=="O") {
            alertDialog.setMessage("Player Won!");
            prefsEditor.putInt("WIN",mPrefs.getInt("WIN",0)+1);
        }
        else if(status=="X") {
            alertDialog.setMessage("Computer Won!");
            prefsEditor.putInt("LOSE", mPrefs.getInt("LOSE", 0) + 1);
        }
        else {
            alertDialog.setMessage("Board is Full. Tie!");
            prefsEditor.putInt("TIE", mPrefs.getInt("TIE", 0) + 1);

        }
        alertDialog.setButton("Replay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                startActivity(getIntent());
            }
        });
        alertDialog.show();

    }

    public void putPiece()
    {
            game1.drawimage(game1.x_r,game1.y_r);
    }
    @Override
    public void onClick(View v) {
        AlertDialog a = new AlertDialog.Builder(Play.this).create();
        switch(v.getId())
        {
            case R.id.button2:
                putPiece();
                put.setEnabled(false);
               if(game1.oWinS(game1.y_r,game1.x_r))
                    finish("O");
                else if(game1.isFull())
                    finish("draw");
                else
                {
                    game1.computerTurn();
                    put.setEnabled(true);
//                    a.setMessage(Integer.toString(game1.x_r2)+" "+Integer.toString(game1.y_r2));
//                    a.show();
                    if(game1.xWins(game1.x_r2,game1.y_r2))
                        finish("X");
                    else if(game1.isFull())
                        finish("Draw");
                }
                break;
        }

    }


}
