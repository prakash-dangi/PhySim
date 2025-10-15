package physics.core;

public class Vector2D {
    public float x, y;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this.x = 0;
        this.y = 0;
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

    public Vector2D normalized() {
        Vector2D v = new Vector2D(x, y);
        v.normalize();
        return v;
    }

    public void limit(float max) {
        if (mag() > max) {
            normalize();
            mult(max);
        }
    }

    public static Vector2D add(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vector2D sub(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }
}
