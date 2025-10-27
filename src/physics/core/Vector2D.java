package physics.core;
import processing.core.PApplet;

public class Vector2D {
    public float x;
    public float y;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
    }

    public void sub(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    public void mult(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    public void div(float scalar) {
        if (scalar != 0) {
            this.x /= scalar;
            this.y /= scalar;
        }
    }

    public float mag() {
        return (float) Math.sqrt(x*x + y*y);
    }

    public void normalize() {
        float m = mag();
        if (m != 0) {
            div(m);
        }
    }

    public static Vector2D sub(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }

    public static Vector2D div(Vector2D v, float scalar) {
        if (scalar != 0) {
            return new Vector2D(v.x / scalar, v.y / scalar);
        }
        return new Vector2D(v.x, v.y);
    }

    public String toString() {
        return "(x: " + PApplet.nf(this.x, 0, 2) + ", y: " + PApplet.nf(this.y, 0, 2) + ") ";
    }
}
