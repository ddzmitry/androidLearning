package dev.ddzmitry.audioexample;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    public void PlaySound(View view){
        mediaPlayer.start();
    }

    public void StopSound(View view){

        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set Audiomanager

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // GET MAX VALUE SOUND OF DEVICE
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // Get Current Volume
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mediaPlayer = MediaPlayer.create(this, R.raw.marble);


        SeekBar volumeControl = findViewById(R.id.volumeSeekBar);
        // SET MAX VALUE FOR CONTROLLER
        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(currentVolume);

            volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                Log.i("Seekbar changed",Integer.toString(i));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // Scrub through Audio
        final SeekBar scrubSeekbar = findViewById(R.id.scrubSeekbar);
        // Set max value
        scrubSeekbar.setMax(mediaPlayer.getDuration());

        scrubSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                Log.i("scrubSeekbar changed",Integer.toString(i));

                // set value
                mediaPlayer.seekTo(i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();

            }
        });


        // Timer for sound
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scrubSeekbar.setProgress(mediaPlayer.getCurrentPosition());
            }
            // every 1/3 second starting now (0)
        },0,300);


    }

}
