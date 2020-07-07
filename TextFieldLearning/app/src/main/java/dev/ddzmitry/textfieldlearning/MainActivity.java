package dev.ddzmitry.textfieldlearning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void clickFunction(View view){
        // Get element by id cast it as EditText
        EditText UserNameText =  findViewById(R.id.UserNameText);
        EditText PaswordNameText =  findViewById(R.id.PasswordNameText);
        System.out.println(view);
        Log.i("Info","clickFunciton was clicked");
        Log.i("username", UserNameText.getText().toString());
        Log.i("password", PaswordNameText.getText().toString());
        Toast.makeText(this, String.format("Hello \n %s",UserNameText.getText().toString()), Toast.LENGTH_SHORT).show();
        ImageView catView = findViewById(R.id.imageCat);
        catView.setImageResource(R.drawable.cat3);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
