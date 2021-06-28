package com.example.pong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class Game extends AppCompatActivity {
    private PongView pongView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pongView=new PongView(this);
        setContentView(R.layout.activity_game);
        pongView=findViewById(R.id.PongView);
        Intent intent=getIntent();
        pongView.Mode=intent.getStringExtra("MODE");
        pongView.setVisibility(View.VISIBLE);
    }
    public void onBackPressed(){
        if(pongView.isGameOver){
            pongView.release();
            int Score=pongView.UserScore;
            SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            int HighScore=(sharedPreferences.getInt("HIGHSCORE",0));
            if(HighScore<Score&&pongView.Mode.equals("HARD")){
                editor.putInt("HIGHSCORE",Score);
                editor.commit();
            }
            super.onBackPressed();
        }
    }
}