package com.aop.gameexample;

public class Vector2{
    public float x,y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // floating point zero evaluation
    public boolean isZero()
    {
        if(x > 0.01f || x < -0.01f) return false;
        if(y > 0.01f || y < -0.01f) return false;
        return true;
    }

    public double magnitude()
    {
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

    // arithmetics
    public static Vector2 add(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x+b.x, a.y+b.y);
    }
    public static Vector2 subtract(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x-b.x, a.y-b.y);
    }
    public static Vector2 multiply(Vector2 a, float b)
    {
        return new Vector2(a.x*b, a.y*b);
    }
    public static Vector2 multiply(Vector2 a, Vector2 b){return new Vector2(a.x*b.x, a.y*b.y);}
    public static Vector2 divide(Vector2 a, Vector2 b) { return new Vector2(a.x/b.x, a.y/b.y);}

    // directions
    public static Vector2 up() {return new Vector2(0,-1);}
    public static Vector2 down(){return new Vector2(0, +1);}
    public static Vector2 left(){return new Vector2(-1, 0);}
    public static Vector2 right(){return new Vector2(+1, 0);}
    public static Vector2 zero()
    {
        return new Vector2(0,0);
    }

    // inversions & reflections
    public static Vector2 inverse(Vector2 a)
    {
        return new Vector2(-a.x, -a.y);
    }
    public static Vector2 reflectY(Vector2 a)
    {
        return new Vector2(a.x, -a.y);
    }
    public static Vector2 reflectX(Vector2 a)
    {
        return new Vector2(-a.x, a.y);
    }

    @Override
    public String toString() {
        return "{" +
                 x +
                ", " + y +
                '}';
    }
}
