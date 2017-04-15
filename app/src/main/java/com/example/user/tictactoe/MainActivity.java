package com.example.user.tictactoe;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Game game1;
    private Button btn;
    private Button record;
    private Button Rule;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.button);
        record = (Button)findViewById(R.id.record);
        Rule = (Button)findViewById(R.id.rule);

        btn.setOnClickListener(this);
        record.setOnClickListener(this);
        Rule.setOnClickListener(this);


    }
    public void play()
    {
        Intent i=new Intent(MainActivity.this, Play.class);
        startActivity(i);
    }
    public void showRecord()
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        int win = mPrefs.getInt("WIN", 0);
        int lose = mPrefs.getInt("LOSE", 0);
        int tie = mPrefs.getInt("TIE",0);
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Record");
        alertDialog.setMessage("WIN: " + Integer.toString(win) + "\n" + "LOSE: " + Integer.toString(lose) + "\n" + "TIE: " + Integer.toString(tie));
        alertDialog.show();
    }
    public void showRule()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Rule");
        alertDialog.setMessage("Easy Mode: User is allowed to use 3-3, 3-4, 4-4, or 6 in row to make a victory.\n " +
                "Hard Mode: User is only allow 3-4 to make a victory.");
        alertDialog.show();

    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.button:
                play();
                break;
            case R.id.record:
                showRecord();
                break;
            case R.id.rule:
                showRule();
                break;
        }
    }
}
