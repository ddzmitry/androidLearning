package dev.ddzmitry.eggtimer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    boolean isStopped = false;


    private CountDownTimer cdTimer;
    // Audio stuff
    MediaPlayer mediaPlayer;
    AudioManager audioManager;


    public void playHorn(){
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // GET MAX VALUE SOUND OF DEVICE
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // Get Current Volume
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer = MediaPlayer.create(this, R.raw.horn);
        mediaPlayer.start();
    }

    private void startCountDownTimer(long milliseconds) {

        final TextView timerView = findViewById(R.id.timerView);
        final SeekBar timerBar = findViewById(R.id.seekTime);
        final Button ButtonStartStop = findViewById(R.id.ButtonStartStop);

        cdTimer = new CountDownTimer(milliseconds, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                Integer secondsOnTimer = (int) (long) secondsLeft;
                timerView.setText(getTimer(secondsOnTimer));
                SeekBar timerBar = findViewById(R.id.seekTime);
                timerBar.setProgress(secondsOnTimer);
            }

            public void onFinish() {
                playHorn();
                timerBar.setEnabled(true);
                ButtonStartStop.setText("Start!");

            }
        }.start();
    }


    public void StartorStop(View view) {


        SeekBar timerBar = findViewById(R.id.seekTime);
        // get time here
        Integer currentValue = timerBar.getProgress();
        Button btn = (Button) view;
        if (isStopped) {
            timerBar.setEnabled(true);
            btn.setText("Start!");
            cdTimer.cancel();
        } else {
            timerBar.setEnabled(false);
            btn.setText("Stop!");
            startCountDownTimer(currentValue * 1000);
        }
        isStopped = !isStopped;

        Log.i("StartorStop", "Button Was Pressed");
    }

    public String getTimer(Integer value) {
        int sec = value % 60;
        int min = (value / 60) % 60;
        return String.format("%s:%s", min, sec);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int maxTimer = 600;
        int defaultTimer = 60;
        SeekBar timerBar = findViewById(R.id.seekTime);
        timerBar.setMax(maxTimer);
        timerBar.setProgress(defaultTimer);

        final TextView timerView = findViewById(R.id.timerView);
        timerView.setText(getTimer(defaultTimer));

        // timerBar

        timerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                timerView.setText(getTimer(i));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
