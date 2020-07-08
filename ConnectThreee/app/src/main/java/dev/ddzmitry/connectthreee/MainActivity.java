package dev.ddzmitry.connectthreee;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    boolean turnRed = false;
    boolean gameEnded = false;
    int winningRed = 0;
    int winnigYellow = 0;
    // fill empty cells
    ArrayList<Integer> gameState = new ArrayList<>(Arrays.asList(2, 2, 2, 2, 2, 2, 2, 2, 2));
    // Winning Combinations
    int[][] winningPostitions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    public  void  ResetGame(){
        ImageView imageView1 =  findViewById(R.id.imageView1);
        ImageView imageView2 =  findViewById(R.id.imageView2);
        ImageView imageView3 =  findViewById(R.id.imageView3);
        ImageView imageView4 =  findViewById(R.id.imageView4);
        ImageView imageView5 =  findViewById(R.id.imageView5);
        ImageView imageView6 =  findViewById(R.id.imageView6);
        ImageView imageView7 =  findViewById(R.id.imageView7);
        ImageView imageView8 =  findViewById(R.id.imageView8);
        ImageView imageView9 =  findViewById(R.id.imageView9);

        ArrayList<ImageView> ImagesTokens = new ArrayList<>(Arrays.asList(imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9));
        int counter = 0;
        for (ImageView imagesToken : ImagesTokens) {
            Log.i("info", "Setting tag to " + counter);
            imagesToken.setImageResource(0);
            imagesToken.setTag(counter);
            counter++;
        }
        TextView  ScoreView = findViewById(R.id.ScoreView);
        ScoreView.setText(String.format("Red : %s  Yellow : %s",winningRed,winnigYellow));
        gameState = new ArrayList<>(Arrays.asList(2, 2, 2, 2, 2, 2, 2, 2, 2));
        gameEnded = false;

    }
    public void dropIn(View view) {

        ImageView counter = (ImageView) view;

        int tagCounter = Integer.parseInt(counter.getTag().toString());
        Log.i("tagCounter", counter.getTag().toString());
        Log.i("gameState", gameState.get(tagCounter).toString());
        // Get resource tag to be able to find it so user can't click same cell
        if (gameEnded) {
            Toast.makeText(this, "Game Ended", Toast.LENGTH_SHORT).show();
            ResetGame();

        } else {

            if (gameState.get(tagCounter) == 2) {

                gameState.set(tagCounter, turnRed ? 1 : 0);
                counter.setTranslationY(-1500);
                counter.setImageResource(turnRed ? R.drawable.red : R.drawable.yellow);
                counter.animate().translationYBy(1500).rotationY(3600).setDuration(1000);
                System.out.println(this.getTaskId());
                Log.i("Info", "Arr");


                for (int[] winningPostition : winningPostitions) {

                    int pos1 = winningPostition[0];
                    int pos2 = winningPostition[1];
                    int pos3 = winningPostition[2];
                    if (gameState.get(pos1) != 2 && gameState.get(pos1) == gameState.get(pos2) && gameState.get(pos1) == gameState.get(pos3)) {
                        Toast.makeText(this, String.format("WINNER is %s ", turnRed ? "RED" : "YELLOW"), Toast.LENGTH_SHORT).show();

                        if(turnRed){
                            winningRed ++;
                        } else {
                            winnigYellow ++;
                        }

                        gameEnded = true;
                        Log.i("WINNER", "WE GOT WINNER");

                    }

                }
                if ( !gameState.contains(2)){
                    Toast.makeText(this, String.format("Its a tie! "), Toast.LENGTH_SHORT).show();
                    ResetGame();
                }

                turnRed = !turnRed;

            } else {
                Toast.makeText(this, "Click Empty Cell", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
