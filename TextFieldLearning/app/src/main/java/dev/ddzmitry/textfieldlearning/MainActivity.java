package dev.ddzmitry.textfieldlearning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    public void clickFunction(View view){
        // Get element by id cast it as EditText
        EditText UserNameText = (EditText) findViewById(R.id.UserNameText);
        EditText PaswordNameText = (EditText) findViewById(R.id.PasswordNameText);
        System.out.println(view);
        Log.i("Info","clickFunciton was clicked");
        Log.i("username", UserNameText.getText().toString());
        Log.i("password", PaswordNameText.getText().toString());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
