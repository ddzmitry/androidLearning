package dev.ddzmitry.gridexample;

import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;


    public void translateMe(View view){
        Button buttonClicked = (Button) view;
        buttonClicked.getId();
        // Can also use tags
//        buttonClicked.getTag()


        // will be used to pull resource
//        String id = view.getResources().getResourceName(view.getId()).split("/")[1].trim();
        String id = buttonClicked.getTag().toString();
        Log.i("Info","ID is " + id);
        System.out.println(id == "hello");
        System.out.println(String.format("id:%s",id));

        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        // dont need ending
        mediaPlayer = mediaPlayer.create(this, getResources().getIdentifier(id, "raw", getPackageName()));
        mediaPlayer.start();


//        if(id.contains("hello")){
//            mediaPlayer.stop();
//            Log.i("Info","ID is " + id);
//            mediaPlayer = MediaPlayer.create(this, R.raw.hello);
//            mediaPlayer.start();
//        } else if(id.contains("howareyou")){
//            mediaPlayer.stop();
//            Log.i("Info","ID is " + id);
//            mediaPlayer = MediaPlayer.create(this, R.raw.howareyou);
//            mediaPlayer.start();
//        }else if(id.contains("doyouspeakenglish")){
//            mediaPlayer.stop();
//            Log.i("Info","ID is " + id);
//            mediaPlayer = MediaPlayer.create(this, R.raw.doyouspeakenglish);
//            mediaPlayer.start();
//        }else if(id.contains("goodevening")){
//            mediaPlayer.stop();
//            Log.i("Info","ID is " + id);
//            mediaPlayer = MediaPlayer.create(this, R.raw.goodevening);
//            mediaPlayer.start();
//        }else if(id.contains("howareyou")){
//            mediaPlayer.stop();
//            Log.i("Info","ID is " + id);
//            mediaPlayer = MediaPlayer.create(this, R.raw.howareyou);
//            mediaPlayer.start();
//        }else if(id.contains("ilivein")){
//            mediaPlayer.stop();
//            Log.i("Info","ID is " + id);
//            mediaPlayer = MediaPlayer.create(this, R.raw.ilivein);
//            mediaPlayer.start();
//        }else if(id.contains("mynameis")){
//            mediaPlayer.stop();
//            Log.i("Info","ID is " + id);
//            mediaPlayer = MediaPlayer.create(this, R.raw.mynameis);
//            mediaPlayer.start();
//        }else if(id.contains("please")){
//            mediaPlayer.stop();
//            Log.i("Info","ID is " + id);
//            mediaPlayer = MediaPlayer.create(this, R.raw.please);
//            mediaPlayer.start();
//        }else if(id.contains("welcome")){
//            mediaPlayer.stop();
//            Log.i("Info","ID is " + id);
//            mediaPlayer = MediaPlayer.create(this, R.raw.welcome);
//            mediaPlayer.start();
//        } else {
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
//        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.hello);
        final SeekBar VolumeSeek = findViewById(R.id.VolumeSeek);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        VolumeSeek.setMax(maxVolume);
        // Get Current Volume
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        VolumeSeek.setProgress(currentVolume);

        VolumeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // set streamVolume
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
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
