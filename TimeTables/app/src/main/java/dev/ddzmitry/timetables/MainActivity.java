package dev.ddzmitry.timetables;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    public void SeekNumber(Integer value){

        ArrayList<Integer> answers = new ArrayList<>();

        for (int i = 1; i < 11; i++) {
            answers.add(value * i);
        }
        ListView lv = findViewById(R.id.AnswerView);
        ArrayAdapter<Integer> AnswerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,answers);
        lv.setAdapter(AnswerAdapter);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeekBar NumberPicker = findViewById(R.id.NumberPicker);
        NumberPicker.setMax(10);
        NumberPicker.setProgress(1);
        SeekNumber(1);
        NumberPicker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                SeekNumber(i);

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
