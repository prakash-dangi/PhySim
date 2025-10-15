package Physics.Lines.helpers;
import processing.core.*;

public class Arrow {
    float x1, y1, x2, y2;
    float length;
    PApplet sketch;

    public Arrow(PApplet sketch, float x1, float y1, float length) {
        this.sketch = sketch;
        this.x1 = x1;
        this.y1 = y1;
        this.length = length;
    }

    public void drawArrow() {

    }
}
