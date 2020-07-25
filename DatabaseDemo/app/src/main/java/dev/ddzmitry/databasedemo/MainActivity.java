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

            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS new_users (name VARCHAR, age INT(3), id INTEGER PRIMARY KEY)");
            myDatabase.execSQL("INSERT INTO new_users (name,age) values ('Dzmitry',28)");
            myDatabase.execSQL("INSERT INTO new_users (name,age) values ('Dzmitry',27)");
            myDatabase.execSQL("INSERT INTO new_users (name,age) values ('Dzmitry',26)");
            myDatabase.execSQL("INSERT INTO new_users (name,age) values ('Poofy',4)");

//            myDatabase.execSQL("DELETE FROM new_users WHERE name = 'Dzmitry'");
            Cursor c = myDatabase.rawQuery("SELECT * FROM new_users",null);
            //Cursor c = myDatabase.rawQuery("SELECT * FROM users where age > 18",null);
            //Cursor c = myDatabase.rawQuery("SELECT * FROM users where name = 'Dzmitry' ",null);
            //Cursor c = myDatabase.rawQuery("SELECT * FROM users where name = 'Dzmitry' and age=28 ",null);
            //Cursor c = myDatabase.rawQuery("SELECT * FROM users where name LIKE 'Dz%' and age=28 ",null);
            int nameIndex = c.getColumnIndex("name");
            int ageIndex = c.getColumnIndex("age");
            int idndex = c.getColumnIndex("id");
            // Move cursor to the starting position
            c.moveToFirst();

            // intill all records are out
            while (!c.isAfterLast()){
                Log.i("Name",c.getString(nameIndex));
                Log.i("Age",c.getString(ageIndex));
                Log.i("Id",c.getString(idndex));
                c.moveToNext();
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
