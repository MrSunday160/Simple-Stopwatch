package com.example.stopwatchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;


import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    private int seconds = 0;
    private boolean running;
    private boolean wasRunning;

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(savedInstanceState != null){ // check whether there is a saved instance

            // get previous state of the stopwatch even if the app has been destroyed
            // or recreated

            seconds = savedInstanceState.getInt(("milliseconds"));

            running = savedInstanceState.getBoolean("running");

            wasRunning = savedInstanceState.getBoolean("wasRunning");

        }

        runTimer();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){ // save the state of the stopwatch

        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("seconds", seconds);

        savedInstanceState.putBoolean("running", running);

        savedInstanceState.putBoolean("wasRunning", wasRunning);

    }

    // if activity is paused, stop the stopwatch
    @Override
    protected void onPause(){

        super.onPause();
        wasRunning = running;
        running = false;

    }

    // if activity is resumed, start the stopwatch again if it was running
    @Override
    protected void onResume(){

        super.onResume();
        if(wasRunning)
            running = true;

    }

    // start stopwatch when Start button is tapped
    public void onClickStart(View view){

        running = true;

        if(vibrator != null)
            vibrator.vibrate(100);

    }

    // set running = false when stop button is tapped
    public void onClickStop(View view){

        running = false;

        if(vibrator != null)
            vibrator.vibrate(100);

    }

    // reset the stopwatch when reset button is tapoped
    public void onClickReset(View view){

        running = false;
        seconds = 0;

        if(vibrator != null)
            vibrator.vibrate(100);

    }

    // sets the number of seconds on the stopwatch

    private void runTimer(){

        // get the text view
        final TextView timeView = (TextView) findViewById(R.id.time_view);

        // create new handler
        // handler is used to schedule code to run in the coming future
        // we use handler to schedule the stopwatch code to run every second
        final Handler handler = new Handler();

        // call post() method, passing in a new runnable
        // the method processes code without delay, so the code in the runnable will run almost immediately
        handler.post(new Runnable(){

            @Override
            public void run(){

                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                // format the seconds into hours, minutes, and seconds
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);

                // set the text view text
                timeView.setText(time);

                // if running is true, increment the seconds
                if(running)
                    seconds++;

                // post the code again with a delay of x ms
                handler.postDelayed(this, 1000);

            }

        });

    }

}