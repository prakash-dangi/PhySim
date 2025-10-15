import physics.core.Particle;
import physics.core.Vector2D;
import physics.electrostatics.Electrostatics;
import processing.core.PApplet;
import java.util.ArrayList;

public class PhysicsSandbox extends PApplet {
    ArrayList<Particle> particles = new ArrayList<>();

    public static void main(String[] args) {
        PApplet.main("PhysicsSandbox");
    }

    public void settings() {
        size(1200, 800);
        smooth(8);
    }

    public void setup() {
        Particle centralAttractor = new Particle(width / 2f, height / 2f, 1000, 200, 25);
        centralAttractor.setStatic(true);
        particles.add(centralAttractor);
    }

    public void draw() {
        background(20, 20, 30);

        Electrostatics.applyForces(particles);

        for (Particle p : particles) {
            p.update();
            p.display(this);
        }
    }

    public void mousePressed() {
        if (mouseButton == LEFT) {
            Particle newParticle = new Particle(mouseX, mouseY, 10, 20, 8);
            Vector2D velocity = new Vector2D(0, -2);
            newParticle.velocity = velocity;
            particles.add(newParticle);
        } else if (mouseButton == RIGHT) {
            Particle newParticle = new Particle(mouseX, mouseY, 10, -20, 8);
            Vector2D velocity = new Vector2D(0, 2);
            newParticle.velocity = velocity;
            particles.add(newParticle);
            }
    }
}
