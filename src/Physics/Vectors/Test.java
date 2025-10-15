package Physics.Vectors;
import Physics.Lines.helpers.LineSegment;
import processing.core.*;

public class Test extends PApplet {
    public static void main(String[] args) {
        PApplet.main("Physics.Vectors.Test");
    }

    public void settings(){
        size(800, 800);
        smooth(8);
    }

    Vector v;
    public void setup() {
        background(0);
        stroke(255);
        v = new Vector(this);
        v.magnitude = 50f;
        v.drawVector(400, 400);

        Vector a = new Vector(this, 50f, 50f, 4f);
        a.drawVector(v.coordinates[2], v.coordinates[3]);

        Vector c = v.addVector(a);
        c.drawVector();

        LineSegment l = new LineSegment(this, 600, 600, 700, 500, 10);
        l.drawLine();

        c.align(l);
        l.drawLine();
    }
}
