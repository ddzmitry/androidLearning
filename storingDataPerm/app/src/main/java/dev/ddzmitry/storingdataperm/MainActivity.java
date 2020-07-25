package dev.ddzmitry.storingdataperm;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("dev.ddzmitry.storingdataperm", Context.MODE_PRIVATE);
        ArrayList<String> friends = new ArrayList<>();
        friends.add("Goober");
        friends.add("POOFY");
        friends.add("STANLEY");
        try {
            //Add Array as string
            sharedPreferences.edit().putString("friends",ObjectSerializer.serialize(friends)).apply();
            Log.i("Friends",ObjectSerializer.serialize(friends));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> newfriends = new ArrayList<>();
        try {
            // deserialize data
            newfriends = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("friends",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < newfriends.size(); i++) {
            Log.i("Friend",newfriends.get(i));
        }


//        sharedPreferences.edit().putString("username","Dzmitry").apply();
//        String username = sharedPreferences.getString("username","");
//        Log.i("username",username);
    }
}
