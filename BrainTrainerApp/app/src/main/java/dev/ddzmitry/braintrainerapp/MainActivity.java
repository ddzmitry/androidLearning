package dev.ddzmitry.braintrainerapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Boolean isTimerUp;
    Boolean isGameStarted;
    Integer winningCount;
    Integer loosingCount;
    String questionString;
    Double answer;
    ArrayList<String> arrProblem = new ArrayList<>(Arrays.asList("product","sum","sub","div"));
    ArrayList<Button> buttonsArray = new ArrayList<>();


    private static Double getRandomNumberInRange(Double min, Double max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        DecimalFormat df = new DecimalFormat("#.##");
        double random = new Random().nextDouble();
        return Double.valueOf(df.format(min + (random * (max - min))));
    }


    public void CheckAnswer(View view){
        // Get button
        Button buttonClicked = (Button) view;
        // get Answer
        Double answerToCheck =  Double.parseDouble(buttonClicked.getText().toString());
        if(answer == answerToCheck){
            winningCount ++;
        } else {
            loosingCount ++;
        }
        getAproblem();

    }

    public void getAproblem(){
        DecimalFormat df = new DecimalFormat("#.##");
        Random rand = new Random();
        String whatTodo = arrProblem.get(rand.nextInt(arrProblem.size()));
        Double first = Double.valueOf(df.format(getRandomNumberInRange(1d,20d)));
        Double second = Double.valueOf(df.format(getRandomNumberInRange(1d,30d)));

        //arrProblem
        if(whatTodo == "poduct"){
            answer =  Double.valueOf(first) * Double.valueOf(second);
            questionString = first + " * " + second;
                    //String.format("%s * %s"),first,second);
        } else if(whatTodo == "sum"){
            answer =  Double.valueOf(first) + Double.valueOf(second);

            questionString = first + " + " + second;
        } else if(whatTodo == "sub"){

            answer =  Double.valueOf(first) - Double.valueOf(second);
            questionString = first + " - " + second;
        } else {
            answer =  Double.valueOf(first) / Double.valueOf(second);
            questionString = first + " / " + second;
        }
        TextView problemView = findViewById(R.id.problemView);
        //set Text for problem
        problemView.setText(String.valueOf(questionString));
        ArrayList<Double> possibleAnswers = new ArrayList<>();
        possibleAnswers.add(getRandomNumberInRange(answer,601d));
        possibleAnswers.add(getRandomNumberInRange(1d,700d));
        possibleAnswers.add(getRandomNumberInRange(26d,321d));
        possibleAnswers.add(Double.valueOf(df.format(answer)));
        // Shuffled
        Collections.shuffle(possibleAnswers);

        for (Double possibleAnswer : possibleAnswers) {
            System.out.println(possibleAnswer);
            Integer index =  possibleAnswers.indexOf(possibleAnswer);
            Button btnToSet =buttonsArray.get(index);
            // set text
            btnToSet.setText(String.valueOf(possibleAnswer));
            int color = Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            // set color
            btnToSet.setBackgroundColor(color);
            //possibleAnswer
            
        }
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


        // Views
        TextView timerView = findViewById(R.id.timerView);
        TextView problemView = findViewById(R.id.problemView);
        TextView CorrectWrongView = findViewById(R.id.CorrectWrongView);
        // Button
        Button ButtonRestartStart = findViewById(R.id.ButtonRestartStart);


    }
}
