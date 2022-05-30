package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private DatabaseHelperClass db =
            new DatabaseHelperClass(MainActivity.this);

    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    private Button[] btnList;

    private TextView lvl;
    private TextView highScore;

    ImageView refresh;

    private GameMechanics GM = new GameMechanics();

    //number of random button
    private int num;

    //two variables to make gameLoop
    private int x = 0;
    int variable = 1;

    private SoundPool soundPool;
    private int badChoice, goodChoice;

    ProgressBar progressBar;

    private Boolean pause = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variables
        start = findViewById(R.id.start);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);

        lvl = findViewById(R.id.lvl);
        highScore = findViewById(R.id.highScore);

        progressBar = findViewById(R.id.progressBar);

        refresh = findViewById(R.id.refresh);

        //Create first row in sqlDatabase (only one time)
        ArrayList<Integer> flagLsit
                = db.getStepsColumn("work", "-");
        if (flagLsit.size() == 0){
            GM.startGame1(db);
        }

        //First game high score has to be 0
        if (Integer.valueOf(db.showScore()) < 1) {
            highScore.setText("Lvl: 0");
        } else {
            //Get hight score form SqlDatabase
            GM.changeHighScore(highScore, db);
        }

        progressBar.setVisibility(View.INVISIBLE);

        //List of game's buttons
        btnList = new Button[]{btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9};

        //Create sound in the game
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(3)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }

        //initialize sounds types
        badChoice = soundPool.load(this, R.raw.sound_bad_choice, 1);
        goodChoice = soundPool.load(this, R.raw.sound_positive_choice, 1);

        start.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                //Draw buttton and its id from SqlDatabse
                num = GM.startGame2(db);
                //Change Color button to show clickthrough
                show(1000,1500);

                ArrayList<Integer> list = db.getStepsColumn("work", "x");
                if (list.size() != 1) {
                    variable = 0;
                }

                //Change game lvl
                GM.changeGameLvl(lvl, db);

                start.setText("Start Game");

                start.setEnabled(false);

                //GameLoop time
                GM.progressBar(progressBar,1000,timer());
                //Time when user cannot play = GameLoop time
                pauseFun();
            }
        });

        //Restart high score -> 0 in view and sqlDatabase
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.updateScore(new MemoryGameModelClass("0", "nothing"));
                highScore.setText("Lvl: 0");
            }
        });


    }


    public void onClickBtn(View v) {
        ArrayList<Integer> list = db.getStepsColumn("work", "x");
        switch (v.getId()) {
            case R.id.btn1:

                if (list.size() > 0) {
                    fillButton(0, btn1);
                }
                break;

            case R.id.btn2:

                if (list.size() > 0) {
                    fillButton(1, btn2);
                }
                break;

            case R.id.btn3:

                if (list.size() > 0) {
                    fillButton(2, btn3);
                }
                break;

            case R.id.btn4:

                if (list.size() > 0) {
                    fillButton(3, btn4);
                }
                break;

            case R.id.btn5:

                if (list.size() > 0) {
                    fillButton(4, btn5);
                }
                break;

            case R.id.btn6:

                if (list.size() > 0) {
                    fillButton(5, btn6);
                }
                break;

            case R.id.btn7:

                if (list.size() > 0) {
                    fillButton(6, btn7);
                }
                break;

            case R.id.btn8:

                if (list.size() > 0) {
                    fillButton(7, btn8);
                }
                break;

            case R.id.btn9:

                if (list.size() > 0) {
                    fillButton(8, btn9);
                }
                break;


        }
    }

    //Change color every button in the game loop
    private void show (int timeOne, int timeTwo) {
        ArrayList<Integer> list = db.getStepsColumn("work", "x");
        //For every step
        for (int  i : list) {
            GM.changeColor(btnList[i], timeOne, timeTwo, soundPool, goodChoice);
            //Color change delay
            timeOne += 1300;
            timeTwo += 1300;
        }

    }

    //Time of processing the progressBar animation
    private int timer () {
        //Get number steps to repeat
        ArrayList<Integer> list = db.getStepsColumn("work", "x");
        //Time = end progessBar animation
        int time = list.size() * 1300;
        return time;

    }

    //Pause time, when user cannot play
    private void pauseFun () {
        //Get number steps to repeat
        ArrayList<Integer> list = db.getStepsColumn("work", "x");
        //Time = game pause
        int time = list.size() * 1300;

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                pause = true;
            }
        }, time);

    }

    //Game code
    private void fillButton (int btnNum, Button btn) {
        //Get list of steps from sqlDatabse
        ArrayList<Integer> list = db.getStepsColumn("work", "x");

        if (pause == true) {

            //Correct answer and next lvl:
            if (btnNum == (list.get(list.size() - 1)) &&
                    variable == ((list.size()))) {
                //Play music
                soundPool.play(goodChoice, 1, 1, 0, 0, 1);
                //Change button color
                GM.userAnswer(btn, R.drawable.bg_green);
                //If user breaks the high score, set new high score
                if (Integer.valueOf(db.showScore()) < list.size()) {
                    db.updateScore(new MemoryGameModelClass(String.valueOf(list.size()), "nothing"));
                    GM.changeHighScore(highScore, db);
                }
                //Two variables
                x = 0;
                variable = -1;
                //Change lvl
                GM.changeButtonLvl(start, db);
                start.setEnabled(true);
                pause = false;

                //Correct answer:
            } else if (btnNum == Integer.valueOf(list.get(x)) && variable == x) {
                //Play music
                soundPool.play(goodChoice, 1, 1, 0, 0, 1);
                //Change button color
                GM.userAnswer(btn, R.drawable.bg_green);

                x++;
                if (x == list.size() - 1) {
                    variable = list.size();
                } else {
                    variable++;
                }

                //Incorrect answer
            } else {
                //Play Music
                soundPool.play(badChoice, 1, 1, 0, 0, 1);
                //Change button color
                GM.userAnswer(btn, R.drawable.bg_red);
                //Clean sqlDatabase from steps
                db.deleteSteps();
                lvl.setText("Lvl: 0");
                start.setEnabled(true);
                x=0;
                variable = 1;
                //Toast with delay
                GM.delayToast("You Lose", MainActivity.this);

            }

        }
    }
    

    @Override
    protected void onStop() {
        super.onStop();
        //Clean sqlDatabase from steps
        db.deleteSteps();
    }
}