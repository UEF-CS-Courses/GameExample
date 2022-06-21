package com.aop.gameexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;

public class GameCanvas extends View {

    public HashSet<GameObject> activeGameObjects = new HashSet<>();
    public HashSet<GameObject> removeNextFrame = new HashSet<>();
    Paint debugTextPaint;


    public GameCanvas(Context context, AttributeSet attributes) {
        super(context, attributes);
        invalidate();

        debugTextPaint = new Paint();
        debugTextPaint.setColor(Color.BLACK);
        debugTextPaint.setTextSize(80);
    }

    public void ClearCanvas()
    {
        removeNextFrame.addAll(activeGameObjects);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // clear gameobjects deleted on gamelogic side before drawing
        if(removeNextFrame.size() > 0)
        {
            for (GameObject G : removeNextFrame) {
                activeGameObjects.remove(G);
            }
            removeNextFrame.clear();
        }

        if(Game.INSTANCE != null && Game.INSTANCE.isRunning)
        {
            MainActivity.INSTANCE.updateScore(Game.INSTANCE.score);

            for(GameObject G : activeGameObjects)
            {
                G.draw(canvas);
            }
        }
    }
}
