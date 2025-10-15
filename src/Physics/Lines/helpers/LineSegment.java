package Physics.Lines.helpers;

import processing.core.PApplet;

public class LineSegment {
    public float x1, y1, x2, y2;
    public float length;
    public float angle = 0f;
    PApplet sketch;

    public LineSegment(PApplet sketch, float x1, float y1, float x2, float y2, int length) {
        this.sketch = sketch;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.length = length;
    }

    public void drawLine() {
        sketch.line(x1, y1, x2, y2);
        length = (float)Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }
}
