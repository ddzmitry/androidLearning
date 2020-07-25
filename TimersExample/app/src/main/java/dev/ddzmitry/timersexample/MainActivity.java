package dev.ddzmitry.timersexample;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Use runnable
/*
        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
            Log.i("Hey it's us", " A second passed by");
            handler.postDelayed(this,1000);
            }
        };

       handler.post(run);
*/

        // Prefered Way
        new CountDownTimer(10000,1000) {
            // every second
            public  void onTick(long milisecondsUntillDone){
                Log.i("Seconds Left", String.valueOf(milisecondsUntillDone/1000));
            }

            // on finish
            public void  onFinish(){
                Log.i("We are done", "No more countdown");
            }

        }.start();


    }
}
