package dev.ddzmitry.languagepreferences;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public void set_language(String language){

        final SharedPreferences sharedPreferences = this.getSharedPreferences("dev.ddzmitry.languagepreferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("language_default",language).apply();
        Toast.makeText(this, "Your Language is " + sharedPreferences.getString("language_default",null), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // get menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_language,menu);
        return super.onCreateOptionsMenu(menu);
    }
    // Set On click events


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.english:
                set_language("English");
                return true;
            case R.id.spanish:
                set_language("Spanish");
                return true;
            default:
                return false;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // create shared properties
        SharedPreferences sharedPreferences = this.getSharedPreferences("dev.ddzmitry.languagepreferences", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("language_default",null) == null){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_btn_speak_now)
                    .setTitle("Pick your language!?")
                    .setMessage("What would be your language?")
                    .setPositiveButton("English", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            set_language("English");
                        }
                    })
                    .setNegativeButton("Spanish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            set_language("Spanish");

                        }
                    })
                    .show();
        } else{

            Toast.makeText(this, "Your Language is " + sharedPreferences.getString("language_default",null), Toast.LENGTH_SHORT).show();
        }

    }
}
