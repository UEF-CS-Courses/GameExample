package com.aop.gameexample;

import android.os.Looper;

import java.util.HashSet;
import java.util.concurrent.Executors;

public class GameLoop
{
    public GameCanvas mainCanvas;
    public volatile boolean isRunning = false;
    GameThread gameThread;

    public GameLoop(GameCanvas gameCanvas)
    {
        this.mainCanvas = gameCanvas;
        initializeGameLoop();
    }

    public void refreshView()
    {
        if(!isRunning)return;
        // Game logic-thread has completed a loop, invalidate view on the UI-thread
        MainActivity.INSTANCE.runOnUiThread(() -> {
            mainCanvas.postInvalidate();
        });
    }

    private void initializeGameLoop()
    {
        gameThread = new GameThread(this);
    }

    public void start()
    {
        Executors.newSingleThreadExecutor().execute(gameThread);
        this.isRunning = true;
    }

    public void stop()
    {
        this.isRunning = false;
    }

    public void removeGameObject(GameObject G)
    {
        boolean runningOnMainThread =
                Thread.currentThread().equals(Looper.getMainLooper().getThread());

        if(isRunning && runningOnMainThread) return;
        gameThread.gameObjects.remove(G);

        MainActivity.INSTANCE.runOnUiThread(() -> mainCanvas.removeNextFrame.add(G));
    }

    public void addGameObject(GameObject G)
    {
        boolean runningOnMainThread =
                Thread.currentThread().equals(Looper.getMainLooper().getThread());

        // dont allow changes to gameobjects from mainthread while gameloop is running
        if(isRunning && runningOnMainThread) return;

        gameThread.gameObjects.add(G);
        mainCanvas.activeGameObjects.add(G);
    }

    private static class GameThread implements Runnable
    {
        GameLoop gameLoop;
        protected HashSet<GameObject> gameObjects;

        public GameThread(GameLoop loop) {
            this.gameLoop = loop;
            this.gameObjects = new HashSet<>();
        }

        @Override
        public void run() {
            gameLoop.refreshView();
            float deltaTime_ms = 0;
            long lastFrame_ms = 0;
            double lastScreenUpdate_ms = 0;
            while(true)
            {
                // timings
                if(lastFrame_ms == 0) lastFrame_ms = System.currentTimeMillis();
                // time it took to do one gameplay-loop
                deltaTime_ms = System.currentTimeMillis()- lastFrame_ms;
                float deltaTime_s = (float) (deltaTime_ms * 10e-3);
                lastFrame_ms = System.currentTimeMillis();

                if(!gameLoop.isRunning) break;
                Movement(deltaTime_s);
                if(!gameLoop.isRunning) break;
                Collisions();

                // trigger screen update every 16 milliseconds, for desired 60 fps
                if(System.currentTimeMillis() - lastScreenUpdate_ms > 0.016)
                {
                    gameLoop.refreshView();
                    lastScreenUpdate_ms = System.currentTimeMillis();
                }
            }
            if(!gameLoop.isRunning)
            {
                gameObjects.clear();
            }
        }

        private void Movement(float deltaTime)
        {
            // clamp vertical speed to speed (difficulty)
            Game.INSTANCE.ball.speed.y =
                    Game.INSTANCE.ball.speed.y < 0 ?
                            -Game.INSTANCE.speed : +Game.INSTANCE.speed;

            for(GameObject G : gameObjects)
            {
                if(G.isKinetic){
                    Vector2 movementThisFrame = Vector2.multiply(G.speed, deltaTime);
                    G.moveTo(Vector2.add(G.position, movementThisFrame));
                }
                if(G.followsTouch){
                    G.moveTo(new Vector2(Game.INSTANCE.paddleLocation, G.position.y));
                }
            }
        }



        private void Collisions()
        {
            if(gameLoop.isRunning)Collisions.canvasBoundsCheck(gameObjects, gameLoop.mainCanvas);
            if(gameLoop.isRunning)Collisions.collisionCheck(gameObjects);
            if(gameLoop.isRunning)Collisions.paddleCollisions(Game.INSTANCE.paddle, gameObjects);
        }
    }

}



