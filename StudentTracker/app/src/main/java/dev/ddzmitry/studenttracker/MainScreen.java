package dev.ddzmitry.studenttracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainScreen extends AppCompatActivity {

    @BindView(R.id.termsButton)
    ImageButton termsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);


        final ImageButton button = (ImageButton) findViewById(R.id.termsButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                Intent next= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(next);


            }



        });
        Bitmap myBitmap = QRCode.from("https://dzmitry.dev/").bitmap();
        final ImageView myImage = (ImageView) findViewById(R.id.imageView);
        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        System.out.println("BITMAP");
        System.out.println(myBitmap);
        iv.setImageBitmap(myBitmap);

    }


}
