package dev.ddzmitry.fadingimages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public void bringbart(View view){
        Log.i("info","App is loaded");
        ImageView imageBart = findViewById(R.id.imageViewBart);
        imageBart.animate().translationX(1000).rotation(1800).scaleX(1f).scaleY(1f).setDuration(2000);

    }
    public void fadeImage(View view){

        ImageView imageBart = findViewById(R.id.imageViewBart);
        // Move down
//        imageBart.animate().translationYBy(1000).setDuration(2000);
        //move left negative , move right positive
        //imageBart.animate().translationX(-1200).setDuration(2000);
        // rotate and disapear
        // imageBart.animate().rotation(1800).alpha(0).setDuration(1000);

        imageBart.animate().scaleX(0.5f).scaleY(0.5f).rotation(1800).alpha(0).setDuration(1000);



//        ImageView imageHomer = findViewById(R.id.imageViewHomer);
//        if(imageBart.getAlpha() == 0 ){
//            imageHomer.animate().alpha(0).setDuration(2000);
//            imageBart.animate().alpha(1).setDuration(2000);
//        } else{
//            imageBart.animate().alpha(0).setDuration(2000);
//            imageHomer.animate().alpha(1).setDuration(2000);
//        }


        Log.i("Info","Imageview Was tapped");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageBart = findViewById(R.id.imageViewBart);
//        imageBart.animate().translationX(-1000);
        imageBart.setX(-1000);
        imageBart.setScaleX(0.1f);
        imageBart.setScaleY(0.1f);
        imageBart.animate().translationXBy(1000).rotation(3600).scaleX(1f).scaleY(1f).setDuration(2000);
    }
}
