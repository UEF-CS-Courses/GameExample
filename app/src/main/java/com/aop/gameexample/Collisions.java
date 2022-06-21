package com.aop.gameexample;

import java.util.HashSet;
import java.util.Random;

public class Collisions
{
    public static void canvasBoundsCheck(HashSet<GameObject> gameObjects, GameCanvas canvas)
    {
        int maxY = canvas.getHeight();
        int maxX = canvas.getWidth();

        for(GameObject G : gameObjects) {
            if (G.isKinetic) {
                // ball collides with bottom edge
                if (G.screenRect.bottom > maxY) {
                    Game.INSTANCE.gameOver();
                    return;
                }
                // ball collides with top
                else if (G.screenRect.top < 0) {
                    G.position.y = G.sizeY/2f;
                    G.speed = Vector2.reflectY(G.speed);
                }
                // ball collides with left side
                if (G.screenRect.right > maxX) {
                    G.position.x = maxX - G.sizeX/2f;
                    G.speed = Vector2.reflectX(G.speed);
                }
                // ball collides with right side
                else if (G.screenRect.left < 0) {
                    G.position.x = G.sizeX/2f;
                    G.speed = Vector2.reflectX(G.speed);
                }
            }
        }
    }

    public static void collisionCheck(HashSet<GameObject> gameObjects)
    {
        GameObject toBeRemoved = null;
        for (GameObject me : gameObjects) {
            if(me.isKinetic)
            {
                boolean hitSomething = false;
                for(GameObject other : gameObjects)
                {
                    if(other.equals(me))continue;
                    if(other.followsTouch)continue; // paddle collisions handled separately
                    if(other.isKinetic) continue; // skip possible other balls
                    if(me.screenRect.intersect(other.screenRect))
                    {
                        hitSomething = true;
                        if(other.destroyedOnCollision){toBeRemoved = other;}
                        break;
                    }
                }
                if(hitSomething)
                {
                    me.speed = Vector2.reflectY(me.speed);
                    me.speed.x = 0;

                    // random float is between 0..1 -> scale to -1..+1
                    // use random to multiply left-directionVector to get
                    // random vector from 0,-1 (left)... 0, +1 (right)
                    Vector2 randomX = Vector2.multiply(Vector2.left(),
                            (new Random().nextFloat()*2-1));

                    // scale up
                    randomX = Vector2.multiply(randomX, 15);
                    me.speed = Vector2.add(me.speed, randomX);

                    // might be colliding with multiple bricks at the same time, warp a bit
                    // forwards to avoid double collisions
                    me.moveTo(Vector2.add(me.position, Vector2.multiply(me.speed, 0.1f)));
                }
            }
        }

        // finally, remove gameobject deleted by collision
        if(toBeRemoved != null){
            Game.INSTANCE.incrementScore(toBeRemoved);
            Game.INSTANCE.gameLoop.removeGameObject(toBeRemoved);
        }
    }

    public static void paddleCollisions(GameObject paddle, HashSet<GameObject> gameObjects)
    {
        for (GameObject G:gameObjects) {
            if(!G.isKinetic) continue;
            if(!paddle.screenRect.intersect(G.screenRect))continue;
            if(G.speed.y < 0) continue; // dont allow hits on the underside of paddle

            // aim the ball away from the center of paddle
            float deltaX = G.position.x - paddle.position.x;
            G.speed = Vector2.reflectY(G.speed);
            G.speed.x += deltaX*0.5f;
        }
    }
}
