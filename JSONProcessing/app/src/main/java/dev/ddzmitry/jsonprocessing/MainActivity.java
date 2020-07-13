package dev.ddzmitry.jsonprocessing;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    // 933cce694442a49031b6635b21d87ac3
    // http://api.openweathermap.org/data/2.5/weather?q=charlotte&appid=933cce694442a49031b6635b21d87ac3

    public void getWeather(View view){

        EditText cityEntry = findViewById(R.id.cityEntry);
        String cityText = cityEntry.getText().toString();

        getWeather taskWeather = new getWeather();
        try {
            String cityWeather = taskWeather.execute(
                    String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=933cce694442a49031b6635b21d87ac3",cityText)
            ).get();
            Log.i("Weather", String.valueOf(cityWeather));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    class getWeather extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection connection = null;

            try{
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();

                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                while (data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return  result;

            }catch (Exception e ){

                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Could go touch someting in UI
//            Log.i("JSON",s);
            try {
                JSONObject object = new JSONObject(s);
                String weatherInfo  = object.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);

                for (int i =0 ; i < arr.length(); i++){

                       JSONObject jsonPart = arr.getJSONObject(i);
                    Log.i("main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));

                    TextView weatherView = findViewById(R.id.weatherView);
                    weatherView.setText("");
                    StringBuffer sb = new StringBuffer();
                    sb.append("Main: " + jsonPart.getString("main") + "\n");
                    sb.append("Description: " + jsonPart.getString("description") + "\n");
                    weatherView.setText(String.valueOf(sb.toString()));

                }


            } catch (Exception e){

                e.printStackTrace();
            }



        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
