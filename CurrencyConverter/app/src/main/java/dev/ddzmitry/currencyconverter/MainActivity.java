package dev.ddzmitry.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {


    public  void  buttonConvertClick(View view){
        EditText currencyValue =  findViewById(R.id.currencyInput);
        if (currencyValue.getText().toString() == null) {
            Log.e("error","String is empty");
            Toast.makeText(this, "Field is empty", Toast.LENGTH_SHORT).show();
        }
        try {
            double currency = Double.parseDouble(currencyValue.getText().toString());
            double valueInDollars = currency * 1.3;
            NumberFormat formatter = new DecimalFormat("#0.00");
            // Conversion
            Toast.makeText(this, String.format("The amount in dollars is %s",formatter.format(valueInDollars)), Toast.LENGTH_SHORT).show();
            // Change image
            ImageView currencyImage = findViewById(R.id.imageCurrency);
            currencyImage.setImageResource(R.drawable.dollars);
            Log.i("conversion","Conversion is correct");
        } catch (NumberFormatException nfe) {
            Log.e("error",nfe.toString());
            Toast.makeText(this, "Must be Number", Toast.LENGTH_SHORT).show();
        }


//        try{
//            Double valueCurrency = Double.parseDouble(currencyValue.getText().toString());
//            Log.i("value_from_input", valueCurrency.toString());
//
//
//        }catch (Exception err){
//
//            Log.e("error",err.toString());
//        }




        //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
