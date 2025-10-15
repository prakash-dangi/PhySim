package Physics.Electrostatics;

import processing.core.PApplet;

public class DotField {
    float magnitude, x, y;
    PApplet sketch;

    public DotField(PApplet sketch, float efield, float x, float y) {
        this.sketch = sketch;
        this.magnitude = efield;
        this.x = x;
        this.y = y;
    }

    public DotField(PApplet sketch, float x, float y) {
        this.x = x;
        this.y = y;
        this.sketch = sketch;
    }

    public void drawDot() {
        sketch.point(x, y);
    }
}
