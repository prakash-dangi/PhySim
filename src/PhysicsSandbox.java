import physics.core.Particle;
import physics.core.Vector2D;
import physics.electrostatics.Electrostatics;
import physics.gravity.Gravity;
import physics.magnetism.Magnetism;
import processing.core.PApplet;
import java.util.ArrayList;

public class PhysicsSandbox extends PApplet {
    ArrayList<Particle> particles = new ArrayList<>();

    boolean isElectrostaticsEnabled = true;
    boolean isGravityEnabled = false;
    boolean isMagnetismEnabled = false;

    float magneticFieldStrength = 0.01f;

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

        Particle centralPlanet = new Particle(width - 250, height / 2f, 20000, 0, 30);
        centralPlanet.setStatic(true);
        particles.add(centralPlanet);
    }

    public void draw() {
        background(20, 20, 30);

        if (isElectrostaticsEnabled) {
            Electrostatics.applyForces(particles);
        }

        if (isGravityEnabled) {
            Gravity.applyForces(particles);
        }

        if (isMagnetismEnabled) {
            Magnetism.applyForces(particles, magneticFieldStrength);
        }

        for (Particle p : particles) {
            p.update();
            p.display(this);
        }

        drawHUD();
    }

    public void drawHUD() {
        fill(220);
        textSize(16);
        textAlign(LEFT, TOP);
        text("Press keys to toggle forces:", 10, 10);

        fill(isElectrostaticsEnabled ? color(100, 255, 100) : color(220));
        text("[E]lectrostatics: " + (isElectrostaticsEnabled ? "ON" : "OFF"), 10, 35);

        fill(isGravityEnabled ? color(100, 255, 100) : color(220));
        text("[G]ravity: " + (isGravityEnabled ? "ON" : "OFF"), 10, 60);

        fill(isMagnetismEnabled ? color(100, 255, 100) : color(220));
        text("[M]agnetism: " + (isMagnetismEnabled ? "ON" : "OFF"), 10, 85);
    }

    public void keyPressed() {
        if (key == 'e' || key == 'E') {
            isElectrostaticsEnabled = !isElectrostaticsEnabled;
        }
        if (key == 'g' || key == 'G') {
            isGravityEnabled = !isGravityEnabled;
        }
        if (key == 'm' || key == 'M') {
            isMagnetismEnabled = !isMagnetismEnabled;
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
        } else if (mouseButton == CENTER) {
            Particle neutralParticle = new Particle(mouseX, mouseY, 50, 0, 10);
            neutralParticle.velocity = new Vector2D(1, 0);
            particles.add(neutralParticle);
        }
    }
}
