package com.aop.gameexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Game game;
    public static MainActivity INSTANCE;

    private TextView scoreDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.INSTANCE = this;
        setContentView(R.layout.activity_main);
        GameCanvas gameCanvas = findViewById(R.id.gamecanvas);
        scoreDisplay = findViewById(R.id.tvScore);

        game = new Game(gameCanvas);

        // hide bottom navigation bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

    }

    public void onBtnStartClicked(View v)
    {
        game.startGame();

        Button btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setVisibility(View.INVISIBLE);

        Button btnQuit = (Button) findViewById(R.id.btnQuit);
        btnQuit.setVisibility(View.INVISIBLE);

    }

    public void onBtnQuitClicked(View v)
    {
        finish();
    }

    public void updateScore(int points)
    {
        scoreDisplay.setText(String.valueOf(points));
    }

    public void gameOver(int points)
    {
        String text = "Game Over!\nScore:"+String.valueOf(points);

        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnQuit = (Button) findViewById(R.id.btnQuit);
        scoreDisplay.setText(text);

        // turn the buttons visible after a 2s delay
        Handler handler = new Handler();
        handler.postDelayed((Runnable) () -> {
            btnStart.setVisibility(View.VISIBLE);
            btnQuit.setVisibility(View.VISIBLE);
        },2000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        game.paddleLocation = event.getX();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // clean up thread when closing app
        game.stopGame();
    }
}