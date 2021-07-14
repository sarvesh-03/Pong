package com.example.pong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

public class Game extends AppCompatActivity {
    private PongView pongView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pongView=new PongView(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        pongView=findViewById(R.id.PongView);
        Intent intent=getIntent();
        pongView.Mode=intent.getStringExtra("MODE");
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE)
            pongView.Orientation="LandScape";
        else pongView.Orientation="Portrait";
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
    @Override
    protected void onPause(){
        super.onPause();
        pongView.PauseGame();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("StoredData",pongView.getData());
        Log.v("SavedData",""+outState.getInt("ComputerScore")+outState.getInt("Hello"));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v("Onrestore",""+savedInstanceState.getBundle("StoredData").getInt("ComputerScore")+"  "+savedInstanceState.getInt("Hello"));
        pongView.StoredData=savedInstanceState.getBundle("StoredData");
        pongView.ToRestore=true;
    }
    @Override
    protected void onResume(){
        super.onResume();
        pongView.ResumeGame();
    }
}