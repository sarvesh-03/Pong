package com.example.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PongView extends View {
    private Rect PowerUpBox;
    private boolean DisplayPowerUpBox=false;
    private Rect panel;
    private Rect ComputerSlider;
    private Paint PaintBall;
    private Paint PaintRect;
    private int Width;
    private int Height;
    private int SliderCurrentRight=-1,SliderCurrentLeft=-1;
    private int PanelCurrentRight=-1, PanelCurrentLeft =-1;
    public float CircleX,CircleY,Radius=0;
    public Timer timer;
    private float BallDirectionX =1, BallDirectionY =1;
    public boolean isGameOver=false;
    private Random random=new Random();
    public boolean ToStart=true;
    public String Mode="EASY";
    private Paint PaintText;
    private int Speed=2;
    private int Score=0;
    public int Time=0;
    private MediaPlayer mediaPlayer1,mediaPlayer2;
    private float NextX =1;
    private int TimeAfterToLose;
    public int UserScore=0;
    private int ReduceSpeed;
    private Paint paintText1;
    public String Orientation="Portrait";
    private boolean MoveSlider=false;
    public Bundle StoredData=new Bundle();
    public boolean ToRestore=false;
    private boolean ToFollow=false;
    public PongView(Context context) {
        super(context);
        init(null);
    }

    public PongView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PongView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PongView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    private void init(@Nullable AttributeSet attributeSet){
        PowerUpBox=new Rect();
        panel=new Rect();
        ComputerSlider=new Rect();
        PaintText=new Paint();
        PaintText.setColor(Color.parseColor("#8C8A8A"));
        PaintText.setTextAlign(Paint.Align.CENTER);
        paintText1=new Paint();
        paintText1.setColor(Color.parseColor("#FFFFFF"));
        paintText1.setTextAlign(Paint.Align.CENTER);
        paintText1.setTextSize(150);
        PaintText.setTextSize(100);
        PaintRect=new Paint(Paint.ANTI_ALIAS_FLAG);
        PaintRect.setColor(Color.parseColor("#FFFFFF"));
        PaintBall=new Paint(Paint.ANTI_ALIAS_FLAG);
        PaintBall.setColor(Color.parseColor("#FFFFFF"));
        Radius=15;
        mediaPlayer1=MediaPlayer.create(getContext(),R.raw.collision);
        mediaPlayer2=MediaPlayer.create(getContext(),R.raw.game_over);
    }
    @Override
    protected void onDraw(Canvas canvas){
        if(!isGameOver) {
            Width = getWidth();
            Height = getHeight();
            //Log.v("MainActivity", "OnDrawCalled");
            canvas.drawColor(Color.parseColor("#000000"));
            if(ToRestore)
                RestoreData();
            if (Orientation.equals("Portrait")) {
                if (ToStart) {
                    PanelCurrentRight = getWidth() / 2 + 100;
                    PanelCurrentLeft = getWidth() / 2 - 100;
                    SliderCurrentLeft = getWidth() / 2 - 100;
                    SliderCurrentRight = getWidth() / 2 + 100;
                }
                if (DisplayPowerUpBox) {
                    canvas.drawRect(PowerUpBox, PaintRect);
                }
                canvas.drawText("SCORE : " + Score, Width / 2, 160, PaintText);
                canvas.drawText("SCORE : " + UserScore, Width / 2, Height - 110, PaintText);
                panel.left = PanelCurrentLeft;
                panel.top = getHeight() - 240;
                panel.right = PanelCurrentRight;
                panel.bottom = getHeight() - 220;
                ComputerSlider.left = SliderCurrentLeft;
                ComputerSlider.right = SliderCurrentRight;
                ComputerSlider.top = 200;
                ComputerSlider.bottom = 180;
                canvas.drawRect(panel, PaintRect);
                canvas.drawRect(ComputerSlider, PaintRect);
                canvas.drawRect(0, Height - 220, 10, 200, PaintRect);
                canvas.drawRect(Width - 10, Height - 220, Width, 200, PaintRect);
                if (CircleX - Radius > 0 && CircleY + Radius < Height) {
                    canvas.drawCircle(CircleX, CircleY, Radius, PaintBall);
                }
                if (ToStart||ToRestore)
                    StartGame();
            }
            else if(Orientation.equals("LandScape")){
                if (ToStart) {
                    PanelCurrentRight = (Width)/2+100;
                    PanelCurrentLeft = (Width)/2-100;
                    SliderCurrentLeft = (Width)/2-100;
                    SliderCurrentRight = (Width)/2+100;
                }
                if (DisplayPowerUpBox) {
                    canvas.drawRect(PowerUpBox, PaintRect);
                }
                canvas.drawText("SCORE : " + Score,(float) (Width-Height)/4 ,(float) Height / 2,  PaintText);
                canvas.drawText("SCORE : " + UserScore, (float)(3*Width+Height)/4, (float) Height/2, PaintText);
                panel.left = PanelCurrentLeft;
                panel.top = getHeight() -30;
                panel.right = PanelCurrentRight;
                panel.bottom = getHeight() - 50;
                ComputerSlider.left = SliderCurrentLeft;
                ComputerSlider.right = SliderCurrentRight;
                ComputerSlider.top = 50;
                ComputerSlider.bottom = 30;
                canvas.drawRect(panel, PaintRect);
                canvas.drawRect(ComputerSlider, PaintRect);
                canvas.drawRect((float)(Width-Height)/2-10, Height-30 , (float)(Width-Height)/2, 30, PaintRect);
                canvas.drawRect((float)(Width+Height)/2, Height -30, (float)(Width+Height)/2+10, 30, PaintRect);
                if (CircleX - Radius > 0 && CircleY + Radius < Height) {
                    canvas.drawCircle(CircleX, CircleY, Radius, PaintBall);
                }
                if (ToStart||ToRestore)
                    StartGame();
            }
        }
        else if (isGameOver) {
            canvas.drawText("SCORE:" + UserScore, Width / 2, Height / 2 - 75, paintText1);
            canvas.drawText("Press Back Button", Width / 2, Height - 200, PaintText);
        }
    }
    private float initialX;
    private int diff=0;
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        boolean value=super.onTouchEvent(motionEvent);
        float CurrentX,CurrentY;
        if(getVisibility()!=VISIBLE)
            return false;
        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
            initialX =motionEvent.getX();
            return true;
        }
        if(Orientation.equals("Portrait")) {
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !isGameOver) {
                CurrentX = motionEvent.getX();
                CurrentY = motionEvent.getY();
                if (CurrentY > Height - 300 && CurrentY < Height)
                    if (PanelCurrentRight + 80 > CurrentX && PanelCurrentLeft - 80 < CurrentX) {
                        diff = (int) (CurrentX - initialX);
                        if (PanelCurrentLeft + diff > 10 && PanelCurrentRight + diff < Width - 10) {
                            PanelCurrentRight = PanelCurrentRight + diff;
                            PanelCurrentLeft = PanelCurrentLeft + diff;
                            initialX = CurrentX;
                            invalidate();


                        }
                    }
                return true;
            }
        }
        else {
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !isGameOver) {
                CurrentX = motionEvent.getX();
                CurrentY = motionEvent.getY();
                if (CurrentY > Height - 300 && CurrentY < Height)
                    if (PanelCurrentRight + 80 > CurrentX && PanelCurrentLeft - 80 < CurrentX) {
                        diff = (int) (CurrentX - initialX);
                        if (PanelCurrentLeft + diff > (Width-Height)/2 && PanelCurrentRight + diff < (Width + Height) / 2 ) {
                            PanelCurrentRight = PanelCurrentRight + diff;
                            PanelCurrentLeft = PanelCurrentLeft + diff;
                            initialX = CurrentX;
                            invalidate();


                        }
                    }
                return true;
            }
        }
        if(motionEvent.getAction()==MotionEvent.ACTION_UP){
            diff=0;
            return false;
        }
        return value;
    }
    public void StartGame(){
        Log.v("MainActivity","StartGameCalled");
        ToRestore=false;
        isGameOver=false;
        if(Mode.equals("EASY")){
            Speed=2;
            TimeAfterToLose=random.nextInt(80)+20;
        }
        if(ToStart) {
            CircleX = random.nextInt(Width / 2) +(int)(Width/4);
            CircleY = random.nextInt(Height/3)+(int)(Height/3) ;
            BallDirectionX=1;
            BallDirectionY=1;
            ToStart=false;
            UserScore=0;
            DisplayPowerUpBox=false;
            ReduceSpeed=0;
            Log.v("MainActivity",""+Width+"   "+Height);
        }
        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!isGameOver) {
                    CircleY += Speed * BallDirectionY;

                    CircleX += Speed * BallDirectionX;
                    if (Mode.equals("HARD")) {
                        Speed = (Time / 25000) + 2 - ReduceSpeed;
                    }
                    SetDirection();
                    invalidate();
                    Time += 5;
                    if (Time % 30000 == 0 && Mode.equals("HARD")) {
                        GeneratePowerUp();
                    }
                    if(DisplayPowerUpBox){
                        DirectPowerUpBox();
                    }
                    if(MoveSlider){
                        DirectSlider();
                    }
                    if(ToFollow)
                        FollowBall();
                }
            }
        },0,5);

    }
    public void SetDirection(){
        float RemoveDiff=0;
        if(Orientation.equals("Portrait")) {
            if (CircleY + Radius < Height - 240) {
                if (CircleX - Radius <= 10) {
                    if (CircleX - Radius < 10) {
                        RemoveDiff = 10 - CircleX + Radius;
                        CircleX += RemoveDiff;
                        CircleY += -1 * BallDirectionY * RemoveDiff;
                    }
                    BallDirectionX = 1;
                    mediaPlayer1.seekTo(0);
                    mediaPlayer1.start();
                }
                if (CircleY - Radius <= 200) {
                    if (CircleY - Radius < 200) {
                        RemoveDiff = 200 - CircleY + Radius;
                        CircleY += RemoveDiff;
                        CircleX += -1 * BallDirectionX * RemoveDiff;
                    }

                    if (CircleX >= SliderCurrentLeft && CircleX <= SliderCurrentRight) {
                        mediaPlayer1.seekTo(0);
                        mediaPlayer1.start();
                        ToFollow=false;
                        Score = Score + 2;
                        BallDirectionY = 1;

                    } else {
                        isGameOver = true;
                        timer.cancel();
                        Log.v("MainActivity","TopP");
                        MoveSlider = false;
                        mediaPlayer2.start();
                        invalidate();
                        Time = 0;
                    }


                }
                if (CircleX + Radius >= Width - 10) {
                    if (CircleX + Radius > Width - 10) {
                        RemoveDiff = CircleX + Radius - Width + 10;
                        CircleX -= RemoveDiff;
                        CircleY += -1 * BallDirectionY * RemoveDiff;
                    }
                    BallDirectionX = -1;
                    mediaPlayer1.seekTo(0);
                    mediaPlayer1.start();
                }


            } else if (((int) CircleX) > PanelCurrentLeft && ((int) CircleX) < PanelCurrentRight && ((int) (CircleY + Radius)) < Height - 230) {
                if (CircleY + Radius > Height - 240) {
                    CircleY -= CircleY + Radius - Height + 240;
                    CircleX -= -1 * BallDirectionX * (CircleY + Radius - Height + 240);
                }
                NextX = Predict((int) CircleX);
                if (Mode.equals("HARD")) {
                    MoveSlider = true;
                } else if (Mode.equals("EASY") && Time / 1000 < TimeAfterToLose) {
                    MoveSlider = true;
                } else {
                    NextX = (float) random.nextInt(Height-60) + (float) (Width-Height)/2;
                    MoveSlider = true;
                }
                BallDirectionY = -1;
                mediaPlayer1.seekTo(0);
                mediaPlayer1.start();
                UserScore += 2;
            } else if (CircleY + Radius >= Height) {
                isGameOver = true;
                Log.v("MainActivity","BottomP");
                timer.cancel();
                mediaPlayer2.start();
                invalidate();
                Time = 0;
            }
        }
        else if(Orientation.equals("LandScape")) {
            if (CircleY + Radius < Height - 50) {
                if (CircleX - Radius <= (float)(Width-Height)/2) {
                    if (CircleX - Radius < (float) (Width-Height)/2) {
                        RemoveDiff = (float)(Width-Height)/2 - CircleX + Radius;
                        CircleX += RemoveDiff;
                        CircleY += -1 * BallDirectionY * RemoveDiff;
                    }
                    BallDirectionX = 1;
                    mediaPlayer1.seekTo(0);
                    mediaPlayer1.start();
                }
                if (CircleY - Radius <= 50) {
                    if (CircleY - Radius < 50) {
                        RemoveDiff = 50 - CircleY + Radius;
                        CircleY += RemoveDiff;
                        CircleX += -1 * BallDirectionX * RemoveDiff;
                    }

                    if (CircleX >= SliderCurrentLeft && CircleX <= SliderCurrentRight) {
                        mediaPlayer1.seekTo(0);
                        mediaPlayer1.start();
                        ToFollow=false;
                        Score = Score + 2;
                        BallDirectionY = 1;

                    } else {
                        isGameOver = true;
                        timer.cancel();
                        Log.v("MainActivity","TopL"+CircleX+"   "+CircleY);
                        MoveSlider = false;
                        mediaPlayer2.start();
                        invalidate();
                        Time = 0;
                    }


                }
                if (CircleX + Radius >= (float) (Width+Height)/2) {
                    if (CircleX + Radius >(float) (Width+Height)/2) {
                        RemoveDiff = CircleX + Radius - (float) (Width+Height)/2;
                        CircleX -= RemoveDiff;
                        CircleY += -1 * BallDirectionY * RemoveDiff;
                    }
                    BallDirectionX = -1;
                    mediaPlayer1.seekTo(0);
                    mediaPlayer1.start();
                }


            } else if (((int) CircleX) > PanelCurrentLeft && ((int) CircleX) < PanelCurrentRight && ((int) (CircleY + Radius)) < Height -40) {
                if (CircleY + Radius > Height - 50) {
                    CircleY -= CircleY + Radius - Height + 50;
                    CircleX -= -1 * BallDirectionX * (CircleY + Radius - Height + 50);
                }
                NextX = Predict((int) CircleX);
                if (Mode.equals("HARD")) {
                    MoveSlider = true;
                } else if (Mode.equals("EASY") && Time / 1000 < TimeAfterToLose) {
                    MoveSlider = true;
                } else {
                    NextX = (float) random.nextInt(Height-60) + (float) (Width-Height)/2;
                    MoveSlider = true;
                }
                BallDirectionY = -1;
                mediaPlayer1.seekTo(0);
                mediaPlayer1.start();
                UserScore += 2;
            } else if (CircleY + Radius >= Height) {
                isGameOver = true;
                Log.v("MainActivity","BottomL");
                timer.cancel();
                mediaPlayer2.start();
                invalidate();
                Time = 0;
            }
        }
    }


    public void GeneratePowerUp(){
        if(Orientation.equals("Portrait")) {
            PowerUpBox.left = random.nextInt(Width - 80) + 20;
            PowerUpBox.right = PowerUpBox.left + 30;
            PowerUpBox.bottom = 200;
            PowerUpBox.top = 240;
        }
        else {
            PowerUpBox.left = random.nextInt(Height - 80) + (Width-Height)/2;
            PowerUpBox.right = PowerUpBox.left + 30;
            PowerUpBox.bottom = 200;
            PowerUpBox.top = 240;
        }
        DisplayPowerUpBox=true;
    }
    public void ApplyChanges(){
        int choosePower=random.nextInt(2)+1;
        if(choosePower==1)
        increasePanelSize();
        else
        DecreaseSpeed();
    }
    public void increasePanelSize(){
        if(Orientation.equals("Portrait")) {
            if (PanelCurrentRight - PanelCurrentLeft < Width / 3) {
                if (PanelCurrentLeft < 30) {
                    PanelCurrentRight += 40;
                } else if (PanelCurrentRight > Width - 30) {
                    PanelCurrentLeft -= 40;
                } else {
                    PanelCurrentLeft -= 20;
                    PanelCurrentRight += 20;
                }
                invalidate();
            } else DecreaseSpeed();
        }
        else {
            if (PanelCurrentRight - PanelCurrentLeft < Height / 3) {
                if (PanelCurrentLeft < 20+(Width-Height)/2) {
                    PanelCurrentRight += 40;
                } else if (PanelCurrentRight > (Height+Width)/2 - 20) {
                    PanelCurrentLeft -= 40;
                } else {
                    PanelCurrentLeft -= 20;
                    PanelCurrentRight += 20;
                }
                invalidate();
            } else DecreaseSpeed();

        }
    }
    public void DecreaseSpeed(){
        if(Speed>=3)
        ReduceSpeed++;
        else increasePanelSize();
    }
    public void release(){
       mediaPlayer1.release();
       mediaPlayer2.release();
    }
    public void DirectPowerUpBox(){
        if(Orientation.equals("Portrait")) {
            if (PowerUpBox.top < Height - 240) {
                PowerUpBox.top += 2;
                PowerUpBox.bottom += 2;
                invalidate();
            } else if (PowerUpBox.left > PanelCurrentLeft && PowerUpBox.right < PanelCurrentRight) {
                ApplyChanges();
                DisplayPowerUpBox = false;
            } else if (PowerUpBox.top < Height) {
                PowerUpBox.top += 2;
                PowerUpBox.bottom += 2;
                invalidate();
            } else {
                DisplayPowerUpBox = false;
            }
        }
        else{
            if (PowerUpBox.top < Height - 50) {
                PowerUpBox.top += 2;
                PowerUpBox.bottom += 2;
                invalidate();
            } else if (PowerUpBox.left > PanelCurrentLeft && PowerUpBox.right < PanelCurrentRight) {
                ApplyChanges();
                DisplayPowerUpBox = false;
            } else if (PowerUpBox.top < Height) {
                PowerUpBox.top += 2;
                PowerUpBox.bottom += 2;
                invalidate();
            } else {
                DisplayPowerUpBox = false;
            }
        }
    }
    public void DirectSlider(){
        if(Orientation.equals("Portrait")) {
            if (NextX >= 110 && NextX <= Width - 110) {
                if (NextX < SliderCurrentLeft + 100 - Speed) {
                    SliderCurrentLeft -= Speed;
                    SliderCurrentRight -= Speed;
                    invalidate();
                } else if (NextX > SliderCurrentLeft + 100 + Speed) {
                    SliderCurrentLeft += Speed;
                    SliderCurrentRight += Speed;
                    invalidate();
                } else MoveSlider = false;
            } else if (NextX > Width - 110) {
                if (SliderCurrentRight + Speed < Width - 10) {
                    SliderCurrentLeft += Speed;
                    SliderCurrentRight += Speed;
                    invalidate();
                } else MoveSlider = false;
            } else if (NextX < 110) {
                if (SliderCurrentLeft - Speed > 10) {
                    SliderCurrentLeft -= Speed;
                    SliderCurrentRight -= Speed;
                    invalidate();
                } else MoveSlider = false;
            }
        }
        else{
            if (NextX >= (Width-Height)/2+100 && NextX <= (Width+Height)/2 - 100) {
                if (NextX < SliderCurrentLeft + 100 - Speed) {
                    SliderCurrentLeft -= Speed;
                    SliderCurrentRight -= Speed;
                    invalidate();
                } else if (NextX > SliderCurrentLeft + 100 + Speed) {
                    SliderCurrentLeft += Speed;
                    SliderCurrentRight += Speed;
                    invalidate();
                } else MoveSlider = false;
            } else if (NextX >  (Width+Height)/2 - 100) {
                if (SliderCurrentRight + Speed < (Width+Height)/2  ) {
                    SliderCurrentLeft += Speed;
                    SliderCurrentRight += Speed;
                    invalidate();
                } else MoveSlider = false;
            } else if (NextX < (Width-Height)/2+100) {
                if (SliderCurrentLeft - Speed > (Width-Height)/2) {
                    SliderCurrentLeft -= Speed;
                    SliderCurrentRight -= Speed;
                    invalidate();
                } else MoveSlider = false;
            }
        }
    }
    public int Predict(int x) {
        if (Orientation.equals("Portrait")) {
            int EffectiveHeight;
            if(!ToRestore)
            EffectiveHeight = Height - 440 - (int) (2 * Radius);
            else {
                EffectiveHeight=(int)CircleY-200-(int)Radius;
            }
            int EffectiveWidth = Width - 20 - (int) (2 * Radius);
            int q;
            if (BallDirectionX == 1) {
                if ((int) ((EffectiveHeight - EffectiveWidth + x - 10 - Radius) / EffectiveWidth) % 2 == 1) {
                    q = (int) ((EffectiveHeight - EffectiveWidth + x - 10 - Radius) % EffectiveWidth);
                } else
                    q = EffectiveWidth - (int) ((EffectiveHeight - EffectiveWidth + x - 10 - Radius) % EffectiveWidth);
            } else {
                if ((int) ((EffectiveHeight - x + 10 + Radius) / EffectiveWidth) % 2 == 0)
                    q = (int) ((EffectiveHeight - x + 10 + Radius) % EffectiveWidth);
                else
                    q = EffectiveWidth - (int) ((EffectiveHeight - x + 10 + Radius) % EffectiveWidth);
            }
            q += Radius + 10;
            return q;
        }
        else {
            x=x-(Width-Height)/2-(int) Radius;
            int EffectiveHeight;
            if(!ToRestore) EffectiveHeight = Height - 100 - (int) (2 * Radius);
            else EffectiveHeight=(int)CircleY-50-(int)Radius;
            int EffectiveWidth = Height - (int) (2 * Radius);
            int q;
            if (BallDirectionX == 1) {
                if ((int) ((EffectiveHeight - EffectiveWidth + x ) / EffectiveWidth) % 2 == 1) {
                    q = (int) ((EffectiveHeight - EffectiveWidth + x ) % EffectiveWidth);
                } else
                    q = EffectiveWidth - (int) ((EffectiveHeight - EffectiveWidth + x ) % EffectiveWidth);
            } else {
                if ((int) ((EffectiveHeight - x ) / EffectiveWidth) % 2 == 0)
                    q = (int) ((EffectiveHeight - x ) % EffectiveWidth);
                else
                    q = EffectiveWidth - (int) ((EffectiveHeight - x ) % EffectiveWidth);
            }
            q += (Width-Height)/2+(int) Radius;
            return q;

        }
    }


    public void PauseGame(){
        if(timer!=null) {
            timer.cancel();
            timer=null;
        }
    }
    public Bundle getData(){
        Bundle bundle=new Bundle();
        bundle.putInt("Width",Width);
        bundle.putInt("Height",Height);
        bundle.putInt("SliderCurrentRight",SliderCurrentRight);
        bundle.putInt("SliderCurrentLeft",SliderCurrentLeft);
        bundle.putInt("PanelCurrentRight",PanelCurrentRight);
        bundle.putInt("PanelCurrentLeft",PanelCurrentLeft);
        bundle.putFloat("CircleX",CircleX);
        bundle.putFloat("CircleY",CircleY);
        bundle.putString("Mode",Mode);
        bundle.putInt("Time",Time);
        bundle.putInt("UserScore",UserScore);
        bundle.putInt("ComputerScore",Score);
        bundle.putInt("ReduceSpeed",ReduceSpeed);
        bundle.putFloat("BallDirectionX",BallDirectionX);
        bundle.putFloat("BallDirectionY",BallDirectionY);
        bundle.putBoolean("isGameOver",isGameOver);
        return bundle;
    }
    public void RestoreData(){
        Log.v("MainActivity","RestoreDataCalled");
        ToStart=false;
        float initialX=0;
        float PreviousWidth=StoredData.getInt("Width");
        float PreviousHeight=StoredData.getInt("Height");
        isGameOver=StoredData.getBoolean("isGameOver");
        UserScore=StoredData.getInt("UserScore");
        Score=StoredData.getInt("ComputerScore");
        Log.v("MainActivity",""+Score);
        ReduceSpeed=StoredData.getInt("ReduceSpeed");
        Mode=StoredData.getString("Mode");
        Time=StoredData.getInt("Time");
        BallDirectionX=StoredData.getFloat("BallDirectionX");
        BallDirectionY=StoredData.getFloat("BallDirectionY");
        int PanelSize=StoredData.getInt("PanelCurrentRight")-StoredData.getInt("PanelCurrentLeft");
        Log.v("PongView",""+Time);
        if(Orientation.equals("LandScape")){
            CircleX=(int)(Height*(StoredData.getFloat("CircleX")-10)/(PreviousWidth-20)+(Width-Height)/2);
            CircleY=(int)((Height-100)*(StoredData.getFloat("CircleY")-200)/(PreviousHeight-440)+50);
            PanelCurrentLeft=(int)(Height*(StoredData.getInt("PanelCurrentLeft")-10)/(PreviousWidth-20)+(Width-Height)/2);
            if(PanelCurrentLeft>(Width+Height)/2-PanelSize){
                PanelCurrentLeft=(Width+Height)/2-PanelSize;
                PanelCurrentRight=(Width+Height)/2;
            }
            else {
                PanelCurrentRight=PanelCurrentLeft+PanelSize;
            }
            SliderCurrentLeft=(int)(Height*(StoredData.getInt("SliderCurrentLeft")-10)/(PreviousWidth-20)+(Width-Height)/2);
            if(SliderCurrentLeft>(Width+Height)/2-200){
                SliderCurrentLeft=(Width+Height)/2-200;
                SliderCurrentRight=(Width+Height)/2;
            }
            else SliderCurrentRight=SliderCurrentLeft+200;
            if(BallDirectionY==-1){
                FollowBall();
                ToFollow=true;
            }
        }
        else {
            CircleX=(int)((Width-20)*(StoredData.getFloat("CircleX")-(PreviousWidth-PreviousHeight)/2)/(PreviousHeight)+10);
            CircleY=(int)((Height-440)*(StoredData.getFloat("CircleY")-50)/(PreviousHeight-100)+200);
            PanelCurrentLeft=(int)((Width-20)*(StoredData.getInt("PanelCurrentLeft")-(PreviousWidth-PreviousHeight)/2)/(PreviousHeight)+10);
            if(PanelCurrentLeft>Width-10-PanelSize){
                PanelCurrentLeft=Width-10-PanelSize;
                PanelCurrentRight=Width-10;
            }
            else {
                PanelCurrentRight=PanelCurrentLeft+PanelSize;
            }
            SliderCurrentLeft=(int)((Width-20)*(StoredData.getInt("SliderCurrentLeft")-(PreviousWidth-PreviousHeight)/2)/(PreviousHeight)+10);
            if(SliderCurrentLeft>Width-10-200){
                SliderCurrentLeft=Width-10-200;
                SliderCurrentRight=Width-10;
            }
            else SliderCurrentRight=SliderCurrentLeft+200;
            if(BallDirectionY==-1){
                FollowBall();
                ToFollow=true;
            }
        }
    }
    public void FollowBall(){
        if(Orientation.equals("Portrait")){
            if(CircleX>=110&&CircleX<=Width-110){
                SliderCurrentLeft=(int)CircleX-100;
                SliderCurrentRight=SliderCurrentLeft+200;
            }
            else if(CircleX>Width-110){
                SliderCurrentRight=Width-10;
                SliderCurrentLeft=Width-210;
            }
            else if(CircleX<110){
                SliderCurrentLeft=10;
                SliderCurrentRight=210;
            }
        }
        else {
            if(CircleX>=(Width-Height)/2+100&&CircleX<=(Width+Height)/2-100){
                SliderCurrentLeft=(int)CircleX-100;
                SliderCurrentRight=SliderCurrentLeft+200;
            }
            else if(CircleX>(Width+Height)/2-100){
                SliderCurrentRight=(Width+Height)/2;
                SliderCurrentLeft=(Width+Height)/2-200;
            }
            else if(CircleX<(Width-Height)/2+100){
                SliderCurrentLeft=(Width-Height)/2;
                SliderCurrentRight=SliderCurrentLeft+200;
            }
        }
    }
    public void ResumeGame(){
        if(timer==null&&!ToStart)
            StartGame();
    }
}
