package com.example.memorygame;

import static androidx.core.os.HandlerCompat.postDelayed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameMechanics {

    //Create the first row
    public void startGame1(DatabaseHelperClass db) {
        db.createFirstRecord(new MemoryGameModelClass("0", "nothing"));
    }

    //Draw a random button
    public int startGame2(DatabaseHelperClass db) {

        int num = (int) (Math.random() * 9);
        //Add step to SqlDatabase
        String dbNum = String.valueOf(num);
        db.addNewRecord(new MemoryGameModelClass(null, dbNum));

        return num;

    }

    //Change the color of random button
    public void changeColor(Button button, int timeOne, int timeTwo, SoundPool SP, int choice) {

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {

                SP.play(choice, 1, 1, 0, 0, 1);
                button.setBackgroundResource(R.drawable.bg_pink);

            }
        }, timeOne);

        handler.postDelayed(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                button.setBackgroundResource(R.drawable.bg_button);

            }
        }, timeTwo);

    }

    //The correct answer -> change button color for short period of time
    public void userAnswer(Button button, int color) {

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                button.setBackgroundResource(color);
            }
        }, 100);

        handler.postDelayed(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                button.setBackgroundResource(R.drawable.bg_button);
            }
        }, 600);

    }

    //The working time of progressBar
    public void progressBar(ProgressBar pro, int timeOne, int timeTwo){

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {

                pro.setVisibility(View.VISIBLE);

            }
        }, timeOne);

        handler.postDelayed(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {

                pro.setVisibility(View.INVISIBLE);
                Boolean pause = true;

            }
        }, timeTwo);


    }
    //Get the amount of steps in game from sql -> set game lvl
    public void changeGameLvl (TextView textView, DatabaseHelperClass db) {
        ArrayList<Integer> list = db.getStepsColumn("work", "x");
        textView.setText(("Lvl: " + list.size()));
    }

    //Get the amount of steps in game from sql -> set game lvl in button
    public void changeButtonLvl (Button button, DatabaseHelperClass db) {
        ArrayList<Integer> list = db.getStepsColumn("work", "x");
        button.setText("Start lvl: "+  (list.size()+1));
    }

    //Get high score from sql -> set it in textView
    public void changeHighScore (TextView textView, DatabaseHelperClass db) {
        String high = db.showScore();
        textView.setText("Lvl: " + high);
    }

    //Toast with delay
    public void delayToast(String msg, Context context) {
        Toast mToast = Toast.makeText(
                context, msg, Toast.LENGTH_SHORT);
        mToast.show();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                mToast.cancel();
            }
        }, 800);

    }

}
