package dev.ddzmitry.appnewsreader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> content = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    SQLiteDatabase articleDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articleDB = this.openOrCreateDatabase("Articles", MODE_PRIVATE,null);

        articleDB.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleID INTEGER, title VARCHAR, content VARCHAR)");



        DownloadTask task = new DownloadTask();
        try{
            task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
        }catch (Exception e){
            e.printStackTrace();
        }

        // Get list view
        ListView listView  = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,titles);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Move to class
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                // put HTML
                intent.putExtra("content",content.get(i));
                startActivity(intent);
            }
        });
        updateListView();



    }

    public void updateListView(){

        Cursor c = articleDB.rawQuery("SELECT * FROM articles",null);

        int contentIndex = c.getColumnIndex("content");
        int titleIndex = c.getColumnIndex("title");

        if(c.moveToFirst()){
            titles.clear();
            content.clear();

            do {
                // fill data
                titles.add(c.getString(titleIndex));
                content.add(c.getString(contentIndex));
            } while (c.moveToNext());

            arrayAdapter.notifyDataSetChanged();

        }

    }
    public  class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

             try {
                 url = new URL(urls[0]);

                 urlConnection = (HttpURLConnection) url.openConnection();
                 InputStream inputStream = urlConnection.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    // read
                 int data = inputStreamReader.read();
                 while (data != -1){
                     char current = (char) data;

                     result += current;
                     // update data
                     data = inputStreamReader.read();
                 }
                 // cast data as JSONARRAY
                 JSONArray jsonArray = new JSONArray(result);
                 int numberOfItems = 20;
                 if(jsonArray.length() < 20){

                     numberOfItems = jsonArray.length();
                 }

                 articleDB.execSQL("DELETE FROM articles");

                 for (int i =0; i < numberOfItems; i++){
                     //https://hacker-news.firebaseio.com/v0/item/8863.json?print=pretty
                     String articleId = jsonArray.getString(i);
                     url = new URL(String.format("https://hacker-news.firebaseio.com/v0/item/%s.json?print=pretty",articleId));
                     //System.out.println(String.format("https://hacker-news.firebaseio.com/v0/item/%s.json?print=pretty",articleId));
                     //https://hacker-news.firebaseio.com/v0/item/8863.json?print=pretty
                     urlConnection = (HttpURLConnection) url.openConnection();
                     inputStream = urlConnection.getInputStream();
                     inputStreamReader = new InputStreamReader(inputStream);
                     // read
                     String articleInfo = "";
                     data = inputStreamReader.read();
                     while (data != -1){
                         char current = (char) data;

                         articleInfo += current;
                         // update data
                         data = inputStreamReader.read();
                     }
//                     Log.i("ArticleInfo",articleInfo);

                     JSONObject jsonObject = new JSONObject(articleInfo);
//                     System.out.println(jsonObject.toString());
                     if(!jsonObject.isNull("title") && !jsonObject.isNull("url")){
                         String articleTitle = jsonObject.getString("title");
                         String articleURL = jsonObject.getString("url");
//                         Log.i("TITLE",articleTitle);
//                         Log.i("URL",articleURL);
                         // Article data
                         url = new URL(articleURL);
                         urlConnection = (HttpURLConnection) url.openConnection();
                         inputStream = urlConnection.getInputStream();
                         inputStreamReader = new InputStreamReader(inputStream);
                         data =inputStreamReader.read();
                         String articleContent = "";

                         while (data != -1){
                             char current = (char) data;
                             articleContent += current;
                             data = inputStream.read();
                         }

//                         Log.i("ArticleHTML" ,articleContent);
                        // to sql
                         String sql = "INSERT INTO articles (articleID, title, content) VALUES (?, ?, ?)";
                         SQLiteStatement statement = articleDB.compileStatement(sql);
                         statement.bindString(1,articleId);
                         statement.bindString(2,articleTitle);
                         statement.bindString(1,articleContent);
                         statement.execute();

                     }

                 }

                 // show result
                 Log.i("URL CONTENT", result);

                 return result;

             }catch (Exception e){
                 e.printStackTrace();
                 return null;
             }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateListView();
        }
    }
}
