package Physics.Vectors;
import Physics.Lines.helpers.LineSegment;
import processing.core.*;

public class Vector {
    PApplet sketch;
    public float magnitude;
    public float[] direction;
    public float[] coordinates = new float[]{0, 0, 0, 0};

    public Vector(PApplet sketch) {
        this.sketch = sketch;
        this.magnitude = 1f;
        this.direction = new float[]{1f, -1f, 0f};
    }

    public Vector(PApplet sketch, float magnitude, float dx, float dy) {
        this.sketch = sketch;
        this.magnitude = magnitude;
        float unitCheck = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        if (unitCheck != 1) {
            dx = dx / unitCheck;
            dy = dy / unitCheck;
        }
        this.direction = new float[]{dx, -dy, 0f};
    }

    public void drawVector(float x, float y){
        float x1 = x + magnitude*direction[0];
        float y1 = y + magnitude*direction[1];
        this.coordinates[0] = x;
        this.coordinates[1] = y;
        this.coordinates[2] = x1;
        this.coordinates[3] = y1;
        sketch.line(x, y, x1, y1);
    }

    public void drawVector() {
        sketch.line(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
    }

    public Vector addVector(Vector ...v){
        float cx = coordinates[2] - coordinates[0]; // x component of vector
        float cy = coordinates[3] - coordinates[1]; // y component of vector
        Vector a = new Vector(sketch);

        for (Vector i : v){
            cx += i.coordinates[2] - i.coordinates[0];
            cy += i.coordinates[3] - i.coordinates[1];
        }

        float mag = (float)Math.sqrt(cx*cx + cy*cy);
        float[] dir = new float[]{cx/mag, cy/mag, 0f};

        float x0 = coordinates[0];
        float y0 = coordinates[1];
        float x1 = x0+cx;
        float y1 = y0+cy;


        a.coordinates = new float[]{x0, y0, x1, y1};
        a.magnitude = mag;
        a.direction = dir;

        return a;
    }

    public void align(LineSegment l){
        float length = l.length;
        l.x2 = l.x1 + length*direction[0];
        l.y2 = l.y1 + length*direction[1];
    }
}
