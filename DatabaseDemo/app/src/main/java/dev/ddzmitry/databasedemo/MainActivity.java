package dev.ddzmitry.databasedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // Create a new database
            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Users",MODE_PRIVATE,null);

            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR, age INT(3))");
            myDatabase.execSQL("INSERT INTO users (name,age) values ('Dzmitry',28)");
            myDatabase.execSQL("INSERT INTO users (name,age) values ('Poofy',4)");

            Cursor c = myDatabase.rawQuery("SELECT * FROM users",null);
            int nameIndex = c.getColumnIndex("name");
            int ageIndex = c.getColumnIndex("age");
            // Move cursor to the starting position
            c.moveToFirst();

            // intill all records are out
            while (!c.isAfterLast()){
                Log.i("Name",c.getString(nameIndex));
                Log.i("Age",c.getString(ageIndex));
                c.moveToNext();
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
