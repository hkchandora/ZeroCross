package com.himanshu.zerocross;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //0 = zero, 1 = crosss

    int activePlayer = 0;

    boolean gameIsActive = true;

    //2 means unplayed

    int[] gameState= {2,2,2,2,2,2,2,2,2};

    int[][] winningPositions = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Zero and Cross");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Download it :-\n\n" +
                        "https://play.google.com/store/apps/details?id=com.himanshu.zerocross";
                String shareSub = "Zero and cross Game";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                return true;
            case R.id.about:
                Intent i2 = new Intent(MainActivity.this,About.class);
                startActivity(i2);
                return true;
            case R.id.rate:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_example, menu);
        return true;
    }

    public void dropln(View view){
        ImageView counter = (ImageView) view;

        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if(gameState[tappedCounter] == 2 && gameIsActive) {

            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1000f);

            if (activePlayer == 0) {
                counter.setImageResource(R.drawable.zero);
                activePlayer = 1;
            } else {
                counter.setImageResource(R.drawable.cross);
                activePlayer = 0;
            }

            counter.animate().translationYBy(1000f).setDuration(300);

             for(int[] winningPositions : winningPositions) {
                 if(gameState[winningPositions[0]] == gameState[winningPositions[1]] &&
                         gameState[winningPositions[1]] == gameState[winningPositions[2]] &&
                         gameState[winningPositions[0]] != 2) {

                     //Someone has won!
                     gameIsActive = false;
                     String winner = "cross";

                     if(gameState[winningPositions[0]] == 0) {
                         winner = "zero";
                     }

                     TextView winnerMessage = (TextView) findViewById(R.id.textView);
                     winnerMessage.setText(winner +" has won!");

                     LinearLayout layout = (LinearLayout) findViewById(R.id.playAgainLayout);
                     layout.setVisibility(View.VISIBLE);
                 } else {
                     boolean gameIsOver = true;
                     for(int counterState : gameState) {
                         if(counterState ==2) gameIsOver = false;
                     }

                     if(gameIsOver) {
                         TextView winnerMessage = (TextView) findViewById(R.id.textView);
                         winnerMessage.setText("It's a draw!");

                         LinearLayout layout = (LinearLayout) findViewById(R.id.playAgainLayout);
                         layout.setVisibility(View.VISIBLE);
                     }
                 }
             }
         }
    }

    public void playAgain(View view) {

        gameIsActive = true;

        LinearLayout layout = (LinearLayout) findViewById(R.id.playAgainLayout);

        layout.setVisibility(View.INVISIBLE);

        activePlayer = 0;

        for(int i=0; i<gameState.length; i++) {
            gameState[i] = 2;
        }

        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        for(int i=0; i<gridLayout.getChildCount(); i++) {
            ((ImageView) gridLayout.getChildAt(i)).setImageResource(0);
        }
    }
}
