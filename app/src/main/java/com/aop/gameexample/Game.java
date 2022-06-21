package com.aop.gameexample;

import android.os.Handler;

public class Game {
    // singleton
    public static Game INSTANCE;

    public GameCanvas canvas;
    public GameLoop gameLoop;

    public boolean isRunning = true;

    public GameObject ball, paddle;
    public float paddleLocation = 0;

    public int score = 0;
    private final float initialSpeed = 80f;
    public float speed = initialSpeed;

    public Game(GameCanvas canvas) {
        this.canvas = canvas;
        gameLoop = new GameLoop(canvas);
        Game.INSTANCE = this;
    }

    public void startGame()
    {
        initializeGameField();
        this.isRunning = true;
        Handler handler = new Handler();
        handler.postDelayed(() -> gameLoop.start(), 500);
    }

    public void stopGame()
    {
        this.isRunning = false;
        gameLoop.stop();
        canvas.ClearCanvas();
    }

    private void resetGame()
    {
        score = 0;
        speed = initialSpeed;
        ball = null;
        paddle = null;
    }

    // called from gameLoop (not main thread)
    public void gameOver()
    {
        int finalScore = score;
        MainActivity.INSTANCE.runOnUiThread((Runnable) () ->
                        MainActivity.INSTANCE.gameOver(finalScore));
        stopGame();
        resetGame();
    }

    // called from gameLoop (not main thread)
    public void incrementScore(GameObject G)
    {
        if(G.pointValue == 0) return;
        score += G.pointValue;
        float speedIncrement = 25f;
        speed = (float) (initialSpeed + ((int)Math.floor(score/5d))* speedIncrement);
    }


    //initializations:
    public void initializeGameField()
    {
        ball = initializeBall();
        initializeBrickField();
        paddle = initializePaddle();
        paddleLocation = canvas.getWidth()/2f;
    }

    private GameObject initializeBall()
    {
        GameObject b = GameObject.CreateBall();
        b.moveTo(new Vector2(400, 400));
        b.speed = new Vector2(60, initialSpeed);
        gameLoop.addGameObject(b);
        return b;
    }

    public GameObject initializePaddle()
    {
        int canvasHeight = canvas.getHeight();
        int canvasWidth = canvas.getWidth();
        GameObject paddle = GameObject.CreatePaddle(canvasWidth/6);
        paddle.moveTo(new Vector2(canvasWidth/2f, canvasHeight-64));
        gameLoop.addGameObject(paddle);
        return paddle;
    }

    private void initializeBrickField()
    {
        // top row
        for(int i = 0; i < 5; i++)
        {
            GameObject brick = GameObject.CreateBrick(canvas.getWidth()/5, 5);
            brick.moveTo(new Vector2(
                    brick.sizeX/2f + i*brick.sizeX,
                    brick.sizeY-(brick.sizeY/2f)));
            gameLoop.addGameObject(brick);
        }
        // middle row
        for(int i = 0; i < 10; i++)
        {
            GameObject brick = GameObject.CreateBrick(canvas.getWidth()/10, 2);
            brick.moveTo(new Vector2(
                    brick.sizeX/2f + i*brick.sizeX,
                    2*brick.sizeY-(brick.sizeY/2f)));
            gameLoop.addGameObject(brick);
        }
        // bottom row
        for(int i = 0; i < 15; i++)
        {
            GameObject brick = GameObject.CreateBrick(canvas.getWidth()/15, 1);
            brick.moveTo(new Vector2(
                    brick.sizeX/2f + i*brick.sizeX,
                    3*brick.sizeY-(brick.sizeY/2f)));
            gameLoop.addGameObject(brick);
        }

    }


}
