package Physics.Lines.helpers;

import processing.core.PApplet;

public class Dot {
    float centerX, centerY, x1, x2, angle;
    PApplet sketch;
    int length;

    public Dot(PApplet sketch, float centerX, float centerY, float x1, float x2, int length, float angle) {
        this.sketch = sketch;
        this.centerX = centerX;
        this.centerY = centerY;
        this.x1 = x1;
        this.x2 = x2;
        this.length = length;
        this.angle = angle;
    }

    public void adddot() {
//        sketch.stroke(255, 0, 0);
//        sketch.stroke(x1, x2, 142);
//        sketch.stroke(sketch.mouseX, sketch.mouseY, sketch.mouseX);
        sketch.point(x1, x2);
        sketch.stroke(255);
    }
}
