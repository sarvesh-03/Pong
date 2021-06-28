package com.example.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
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
    private int Time=0;
    private MediaPlayer mediaPlayer1,mediaPlayer2;
    private float NextX =1;
    private int TimeAfterToLose;
    public int UserScore=0;
    private int ReduceSpeed;
    private Paint paintText1;
    private boolean MoveSlider=false;
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
        if(!isGameOver){
        Width=getWidth();
        Height=getHeight();
        canvas.drawColor(Color.parseColor("#000000"));
        if(ToStart){
            PanelCurrentRight=getWidth()/2+100;
            PanelCurrentLeft =getWidth()/2-100;
            SliderCurrentLeft=getWidth()/2-100;
            SliderCurrentRight=getWidth()/2+100;
        }
        if(DisplayPowerUpBox){
            canvas.drawRect(PowerUpBox,PaintRect);
        }
        canvas.drawText("SCORE : "+Score,Width/2,160,PaintText);
        canvas.drawText("SCORE : "+UserScore,Width/2,Height-110,PaintText);
        panel.left= PanelCurrentLeft;
        panel.top=getHeight()-240;
        panel.right=PanelCurrentRight;
        panel.bottom=getHeight()-220;
        ComputerSlider.left=SliderCurrentLeft;
        ComputerSlider.right=SliderCurrentRight;
        ComputerSlider.top=200;
        ComputerSlider.bottom=180;
        canvas.drawRect(panel,PaintRect);
        canvas.drawRect(ComputerSlider,PaintRect);
        canvas.drawRect(0,Height-220,10,200,PaintRect);
        canvas.drawRect(Width-10,Height-220,Width,200,PaintRect);
        if(CircleX-Radius>0&&CircleY+Radius<Height){
            canvas.drawCircle(CircleX,CircleY,Radius,PaintBall);
        }
        if(ToStart) StartGame();
        }
        else if(isGameOver){
            canvas.drawText("SCORE:"+UserScore,Width/2,Height/2-75,paintText1);
            canvas.drawText("Press Back Button",Width/2,Height-200,PaintText);
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
        if(motionEvent.getAction()==MotionEvent.ACTION_MOVE&&!isGameOver){
            CurrentX=motionEvent.getX();
            CurrentY=motionEvent.getY();
            if(CurrentY>Height-280&&CurrentY<Height-180)
                if(PanelCurrentRight+80>CurrentX&& PanelCurrentLeft -80<CurrentX) {
                    diff = (int) (CurrentX - initialX);
                    if (PanelCurrentLeft + diff > 10 && PanelCurrentRight + diff < Width-10) {
                        PanelCurrentRight = PanelCurrentRight + diff;
                        PanelCurrentLeft = PanelCurrentLeft + diff;
                        initialX = CurrentX;
                            invalidate();


                    }
                }
            return true;
        }
        if(motionEvent.getAction()==MotionEvent.ACTION_UP){
            diff=0;
            return false;
        }
        return value;
    }
    public void StartGame(){
        isGameOver=false;
        ReduceSpeed=0;
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
                }
            }
        },0,5);

    }
    public void SetDirection(){
        float RemoveDiff=0;
        if(CircleY+Radius<Height-240) {
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

                if(CircleX>=SliderCurrentLeft&&CircleX<=SliderCurrentRight) {
                   mediaPlayer1.seekTo(0);
                   mediaPlayer1.start();
                    Score=Score+2;
                    BallDirectionY=1;

                }
                else  {
                    isGameOver=true;
                    timer.cancel();
                    MoveSlider=false;
                    mediaPlayer2.start();
                    invalidate();
                    Time=0;
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


            }
        else if(((int)CircleX)>PanelCurrentLeft&&((int)CircleX)<PanelCurrentRight&&((int)(CircleY+Radius))<Height-230){
            if(CircleY+Radius>Height-240){
                CircleY-=CircleY+Radius-Height+240;
                CircleX-=-1*BallDirectionX*(CircleY+Radius-Height+240);
            }
            NextX = Predict((int) CircleX);
            if(Mode.equals("HARD")){
                MoveSlider=true;
            }
            else if(Mode.equals("EASY")&&Time/1000<TimeAfterToLose){
                MoveSlider=true;
            }
            else {
                NextX=(float)random.nextInt(Width-60)+20;
                MoveSlider=true;
            }
            BallDirectionY =-1;
            mediaPlayer1.seekTo(0);
            mediaPlayer1.start();
            UserScore+=2;
        }
        else if(CircleY+Radius>=Height) {
            isGameOver=true;
            timer.cancel();
            mediaPlayer2.start();
            invalidate();
            Time=0;
        }
    }


    public void GeneratePowerUp(){
        PowerUpBox.left=random.nextInt(Width-80)+20;
        PowerUpBox.right= PowerUpBox.left+30;
        PowerUpBox.bottom=200;
        PowerUpBox.top=240;
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
        if(PanelCurrentRight-PanelCurrentLeft<Width/3) {
            if (PanelCurrentLeft < 30) {
                PanelCurrentRight += 40;
            } else if (PanelCurrentRight > Width - 30) {
                PanelCurrentLeft -= 40;
            } else {
                PanelCurrentLeft -= 20;
                PanelCurrentRight += 20;
            }
            invalidate();
        }
        else DecreaseSpeed();
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
    public void DirectSlider(){
        if (NextX >= 110 && NextX <= Width - 110) {
            if (NextX < SliderCurrentLeft + 100 - Speed) {
                SliderCurrentLeft -= Speed;
                SliderCurrentRight -= Speed;
                invalidate();
            } else if (NextX > SliderCurrentLeft + 100 + Speed) {
                SliderCurrentLeft += Speed;
                SliderCurrentRight += Speed;
                invalidate();
            } else MoveSlider=false;
        } else if (NextX > Width - 110) {
            if (SliderCurrentRight + Speed < Width - 10) {
                SliderCurrentLeft += Speed;
                SliderCurrentRight += Speed;
                invalidate();
            } else MoveSlider=false;
        } else if (NextX < 110) {
            if (SliderCurrentLeft - Speed > 10) {
                SliderCurrentLeft -= Speed;
                SliderCurrentRight -= Speed;
                invalidate();
            } else MoveSlider=false;
        }
    }
    public int Predict(int x) {
        int EffectiveHeight = Height - 440 - (int) (2 * Radius);
        int EffectiveWidth = Width - 20 - (int) (2 * Radius);
        int q;
        if (BallDirectionX == 1) {
            if ((int) ((EffectiveHeight - EffectiveWidth + x - 10-Radius) / EffectiveWidth) % 2 == 1) {
              q=(int)((EffectiveHeight - EffectiveWidth + x - 10-Radius)% EffectiveWidth);
            }else q= EffectiveWidth-(int)((EffectiveHeight - EffectiveWidth + x - 10-Radius)% EffectiveWidth);
        }
        else{
            if((int)((EffectiveHeight-x+10+Radius)/EffectiveWidth)%2==0)
                q=(int)((EffectiveHeight-x+10+Radius)%EffectiveWidth);
            else q=EffectiveWidth-(int)((EffectiveHeight-x+10+Radius)%EffectiveWidth);
            }
        q+=Radius+10;
        return q;
        }
    }
