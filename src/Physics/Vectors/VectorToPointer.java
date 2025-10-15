package Physics.Vectors;
import processing.core.PApplet;

public class VectorToPointer extends PApplet {
    public static void main(String[] args) {
        PApplet.main("Physics.Vectors.VectorToPointer");
    }

    public void settings() {
        size(800, 800);
    }

    public void setup() {
        background(255);
    }

    Vector v = new Vector(this);
    public void draw() {
        background(255);
        float x = mouseX - (float) width / 2;
        float y = mouseY - (float) height / 2;
        v.magnitude = sqrt(pow(x, 2) + pow(y, 2));
        v.direction[0] = x/v.magnitude;
        v.direction[1] = y/v.magnitude;
        v.direction[2] = 0f;
        v.drawVector((float) width /2, (float) height /2);
    }
}
