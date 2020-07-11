package dev.ddzmitry.braintrainerapp;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Integer winningCount = 0;
    Integer loosingCount = 0;
    String questionString;
    Double answer;
    ArrayList<String> arrProblem = new ArrayList<>(Arrays.asList("product", "sum", "sub", "div"));
    ArrayList<Button> buttonsArray = new ArrayList<>();
    private CountDownTimer cdTimer;
    // audio
    // Audio stuff
    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    public void playHorn() {
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // GET MAX VALUE SOUND OF DEVICE
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // Get Current Volume
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer = MediaPlayer.create(this, R.raw.horn);
        mediaPlayer.start();
    }

    // Timer
    private void startCountDownTimer(long milliseconds) {
        final TextView timerView = findViewById(R.id.timerView);
        cdTimer = new CountDownTimer(milliseconds, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                Integer secondsOnTimer = (int) (long) secondsLeft;
                timerView.setText(String.valueOf(secondsOnTimer));

            }

            public void onFinish() {

                for (Button button : buttonsArray) {
                    button.setEnabled(false);


                }
                playHorn();
                Button startStop = findViewById(R.id.ButtonRestartStart);
                startStop.setText(String.valueOf("RESTART"));
                startStop.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    private static Double getRandomNumberInRange(Double min, Double max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        DecimalFormat df = new DecimalFormat("#.##");
        double random = new Random().nextDouble();
        return Double.valueOf(df.format(min + (random * (max - min))));
    }


    public void CheckAnswer(View view) {
        TextView CorrectWrongView = findViewById(R.id.CorrectWrongView);
        // Get button
        Button buttonClicked = (Button) view;
        // get Answer
        Double answerToCheck = Double.parseDouble(buttonClicked.getText().toString());
        DecimalFormat df = new DecimalFormat("#.##");


        if (Double.valueOf(df.format(answer)).equals(Double.valueOf(df.format(answerToCheck)))) {
            winningCount++;
        } else {
            loosingCount++;
        }
        String CorrectWrongViewText = String.format("%s/%s", winningCount, loosingCount);
        CorrectWrongView.setText(String.valueOf(CorrectWrongViewText));
        getAproblem();

    }

    public void getAproblem() {
        DecimalFormat df = new DecimalFormat("#.##");
        Random rand = new Random();
        String whatTodo = arrProblem.get(rand.nextInt(arrProblem.size()));
        Double first = Double.valueOf(df.format(getRandomNumberInRange(1d, 20d)));
        Double second = Double.valueOf(df.format(getRandomNumberInRange(1d, 30d)));

        //arrProblem
        if (whatTodo == "poduct") {
            answer = Double.valueOf(first) * Double.valueOf(second);
            questionString = first + " * " + second;
            //String.format("%s * %s"),first,second);
        } else if (whatTodo == "sum") {
            answer = Double.valueOf(first) + Double.valueOf(second);

            questionString = first + " + " + second;
        } else if (whatTodo == "sub") {

            answer = Double.valueOf(first) - Double.valueOf(second);
            questionString = first + " - " + second;
        } else {
            answer = Double.valueOf(first) / Double.valueOf(second);
            questionString = first + " / " + second;
        }
        TextView problemView = findViewById(R.id.problemView);
        //set Text for problem
        problemView.setText(String.valueOf(questionString));
        ArrayList<Double> possibleAnswers = new ArrayList<>();
        possibleAnswers.add(getRandomNumberInRange(answer, 601d));
        possibleAnswers.add(getRandomNumberInRange(1d, 700d));
        possibleAnswers.add(getRandomNumberInRange(26d, 321d));
        possibleAnswers.add(Double.valueOf(df.format(answer)));
        // Shuffled
        Collections.shuffle(possibleAnswers);

        for (Double possibleAnswer : possibleAnswers) {
            System.out.println(possibleAnswer);
            Integer index = possibleAnswers.indexOf(possibleAnswer);
            Button btnToSet = buttonsArray.get(index);
            // set text
            btnToSet.setText(String.valueOf(possibleAnswer));
            int color = Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            // set color
            btnToSet.setBackgroundColor(color);
            //possibleAnswer

        }
    }

    public void startRestartGame(View view) {
        Button startStop = (Button) view;
        startStop.setText(String.valueOf("RESTART"));
        startStop.setVisibility(View.INVISIBLE);
        TextView CorrectWrongView = findViewById(R.id.CorrectWrongView);
        CorrectWrongView.setText(String.valueOf("0/0"));
        winningCount = 0;
        loosingCount = 0;
        for (Button button : buttonsArray) {
            button.setEnabled(true);
        }
        getAproblem();

        startCountDownTimer(30000);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Answers

        Button answer1 = findViewById(R.id.answer1);
        Button answer2 = findViewById(R.id.answer2);
        Button answer3 = findViewById(R.id.answer3);
        Button answer4 = findViewById(R.id.answer4);
        buttonsArray.add(answer1);
        buttonsArray.add(answer2);
        buttonsArray.add(answer3);
        buttonsArray.add(answer4);

        for (Button button : buttonsArray) {
            button.setEnabled(false);
        }


        // Views
        TextView timerView = findViewById(R.id.timerView);
        TextView problemView = findViewById(R.id.problemView);

        // Button
        Button ButtonRestartStart = findViewById(R.id.ButtonRestartStart);

        ButtonRestartStart.setText(String.valueOf("Start"));


        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // GET MAX VALUE SOUND OF DEVICE
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // Get Current Volume
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


    }
}
