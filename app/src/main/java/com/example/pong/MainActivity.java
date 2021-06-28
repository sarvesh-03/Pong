package com.example.pong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {
    private int HighScore=0;
    private TextView HighScoreText;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button Start;
        Button Hard;
        TextView ScoreText;
        setContentView(R.layout.activity_main);
        ScoreText=findViewById(R.id.Score);
        HighScoreText=findViewById(R.id.HighScore);
        Hard = findViewById(R.id.Hard);
        Start=findViewById(R.id.Start);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        HighScore=sharedPreferences.getInt("HIGHSCORE",0);
        HighScoreText.setText("HIGHEST SCORE: "+HighScore);
        ScoreText.setVisibility(View.VISIBLE);
        ScoreText.setText("Let's Relax");
        Start.setVisibility(View.VISIBLE);
        Hard.setVisibility(View.VISIBLE);

        Start.setOnClickListener(v -> {

            Intent intent=new Intent(MainActivity.this,Game.class);
            intent.putExtra("MODE","EASY");
            startActivity(intent);
        });
        Hard.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this,Game.class);
            intent.putExtra("MODE","HARD");
            startActivity(intent);
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        HighScore=sharedPreferences.getInt("HIGHSCORE",0);
        HighScoreText.setText("HIGHEST SCORE: "+HighScore);
    }
}