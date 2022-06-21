package com.aop.gameexample;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

public class GameObject {
    private final Drawable myDrawable;
    public Vector2 position;
    public Vector2 speed;
    public int sizeX, sizeY;
    public boolean isKinetic = false;
    public boolean destroyedOnCollision = false;
    public boolean followsTouch = false;
    public int pointValue = 0;

    public Rect screenRect;

    public GameObject(Drawable myDrawable, int sizeX, int sizeY) {
        this.myDrawable = myDrawable;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.position = Vector2.zero();
        this.speed = Vector2.zero();
        update();
    }

    public void moveTo(Vector2 newPosition) {
        this.position.x = newPosition.x;
        this.position.y = newPosition.y;
        update();
    }

    private void update() {
        if (screenRect == null) screenRect = new Rect();
        int halfX = sizeX / 2;
        int halfY = sizeY / 2;

        screenRect.bottom = (int) (position.y + halfY);
        screenRect.top = (int) (position.y - halfY);
        screenRect.left = (int) (position.x - halfX);
        screenRect.right = (int) (position.x + halfX);
    }

    public void draw(Canvas canvas) {
        myDrawable.setBounds(screenRect);
        myDrawable.draw(canvas);
    }



    private static Drawable GetDrawable(int resourceId)
    {
        return ResourcesCompat.getDrawable(
                MainActivity.INSTANCE.getResources(), resourceId, null);
    }

    public static GameObject CreateBall()
    {
        GameObject ball = new GameObject(GetDrawable(R.drawable.ball), 64, 64);
        ball.isKinetic = true;
        return ball;
    }

    public static GameObject CreateBrick(int width, int pointValue)
    {
        GameObject brick = new GameObject(GetDrawable(R.drawable.tile), width, 64);
        brick.destroyedOnCollision = true;
        brick.pointValue = pointValue;
        return brick;
    }

    public static GameObject CreatePaddle(int width)
    {
        GameObject paddle = new GameObject(GetDrawable(R.drawable.paddle), width, 64);
        paddle.followsTouch = true;
        return paddle;
    }
}
