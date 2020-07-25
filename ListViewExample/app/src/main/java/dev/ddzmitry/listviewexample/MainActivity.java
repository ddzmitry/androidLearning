package dev.ddzmitry.listviewexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoItems = new ArrayList<String>();

    public void addToDo(View view){
        EditText textTodo = findViewById(R.id.textTodo);
        String valueTodo = textTodo.getText().toString();
        textTodo.setText(null);
        todoItems.add(valueTodo);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = (ListView) findViewById(R.id.listView);
        ArrayList<String> family = new ArrayList<>(Arrays.asList("Ani","Dz","Poofy","Goober"));
        // Array adapter you pass this content and display as item list with data
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,todoItems);
        // Thats where magic happens

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getAdapter().getItem(i).toString();
                // remove data
                arrayAdapter.remove(arrayAdapter.getItem(i));
                // Notify list
                arrayAdapter.notifyDataSetChanged();
                Log.i("Index",Integer.toString(i));
                Log.i("Name is :",name);
            }
        });

    }
}
