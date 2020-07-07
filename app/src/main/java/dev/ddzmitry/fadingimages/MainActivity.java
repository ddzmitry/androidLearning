package dev.ddzmitry.fadingimages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public void fadeImage(View view){



        ImageView imageBart = findViewById(R.id.imageViewBart);
        ImageView imageHomer = findViewById(R.id.imageViewHomer);
        if(imageBart.getAlpha() == 0 ){
            imageHomer.animate().alpha(0).setDuration(2000);
            imageBart.animate().alpha(1).setDuration(2000);
        } else{
            imageBart.animate().alpha(0).setDuration(2000);
            imageHomer.animate().alpha(1).setDuration(2000);
        }




        Log.i("Info","Imageview Was tapped");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
