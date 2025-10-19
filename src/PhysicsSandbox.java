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
    boolean isGravityEnabled = true;
    boolean isMagnetismEnabled = false;
    boolean showTrails = true;
    float magneticFieldStrength = 0.01f;

    float nextParticleCharge = 20.0f;
    float nextParticleMass = 10.0f;
    float nextParticleRadius = 8.0f;
    boolean nextParticleIsStatic = false;

    boolean isPlacingParticle = false;
    Vector2D placementStartPosition;

    public static void main(String[] args) {
        PApplet.main("PhysicsSandbox");
    }

    public void settings() {
        size(1600, 900);
        smooth(8);
    }

    public void setup() {
        placementStartPosition = new Vector2D(0, 0);
    }

    public void draw() {
        background(20, 20, 30);

        if (isElectrostaticsEnabled) Electrostatics.applyForces(particles);
        if (isGravityEnabled) Gravity.applyForces(particles);
        if (isMagnetismEnabled) Magnetism.applyForces(particles, magneticFieldStrength);

        for (Particle p : particles) {
            p.update();
            p.display(this, showTrails);
        }

        drawHUD();
        if (isPlacingParticle) {
            stroke(255, 100);
            strokeWeight(2);
            line(placementStartPosition.x, placementStartPosition.y, mouseX, mouseY);
        }
    }

    public void drawHUD() {
        fill(220);
        textSize(16);
        textAlign(LEFT, TOP);
        int y = 10;
        int y_inc = 25;

        text("--- FORCES (TOGGLE) ---", 10, y); y += y_inc;
        fill(isElectrostaticsEnabled ? color(100, 255, 100) : color(220));
        text("[E]lectrostatics: " + (isElectrostaticsEnabled ? "ON" : "OFF"), 10, y); y += y_inc;
        fill(isGravityEnabled ? color(100, 255, 100) : color(220));
        text("[G]ravity: " + (isGravityEnabled ? "ON" : "OFF"), 10, y); y += y_inc;
        fill(isMagnetismEnabled ? color(100, 255, 100) : color(220));
        text("[M]agnetism: " + (isMagnetismEnabled ? "ON" : "OFF"), 10, y); y += y_inc;
        y += 10;

        fill(220);
        text("--- GLOBAL ---", 10, y); y += y_inc;
        fill(showTrails ? color(100, 255, 100) : color(220));
        text("[T]rails: " + (showTrails ? "ON" : "OFF"), 10, y); y += y_inc;
        fill(220);
        text("[C] Clear All Particles", 10, y); y+=y_inc;
        y += 10;

        fill(220);
        text("--- NEXT PARTICLE (ADJUST) ---", 10, y); y += y_inc;
        text("[Q/A] Charge: " + nf(nextParticleCharge, 0, 1), 10, y); y += y_inc;
        text("[W/S] Mass: " + nf(nextParticleMass, 0, 1), 10, y); y += y_inc;
        text("[R/F] Radius: " + nf(nextParticleRadius, 0, 1), 10, y); y += y_inc;
        fill(nextParticleIsStatic ? color(255, 255, 100) : color(220));
        text("[X] Static: " + nextParticleIsStatic, 10, y);
    }

    public void keyPressed() {
        if (key == 'e' || key == 'E') isElectrostaticsEnabled = !isElectrostaticsEnabled;
        if (key == 'g' || key == 'G') isGravityEnabled = !isGravityEnabled;
        if (key == 'm' || key == 'M') isMagnetismEnabled = !isMagnetismEnabled;
        if (key == 't' || key == 'T') showTrails = !showTrails;
        if (key == 'c' || key == 'C') particles.clear();

        if (key == 'q' || key == 'Q') nextParticleCharge += 5;
        if (key == 'a' || key == 'A') nextParticleCharge -= 5;
        if (key == 'w' || key == 'W') nextParticleMass += 10;
        if (key == 's' || key == 'S') nextParticleMass = max(0, nextParticleMass - 10);
        if (key == 'r' || key == 'R') nextParticleRadius += 1;
        if (key == 'f' || key == 'F') nextParticleRadius = max(1, nextParticleRadius - 1);
        if (key == 'x' || key == 'X') nextParticleIsStatic = !nextParticleIsStatic;
    }

    public void mousePressed() {
        if (mouseButton == LEFT) {
            isPlacingParticle = true;
            placementStartPosition = new Vector2D(mouseX, mouseY);
        }
    }

    public void mouseReleased() {
        if (mouseButton == LEFT && isPlacingParticle) {
            isPlacingParticle = false;

            Particle p = new Particle(
                    placementStartPosition.x,
                    placementStartPosition.y,
                    nextParticleMass,
                    nextParticleCharge,
                    nextParticleRadius
            );

            float velocityScale = 0.1f;
            Vector2D velocity = Vector2D.sub(new Vector2D(mouseX, mouseY), placementStartPosition);
            velocity.mult(velocityScale);
            p.velocity = velocity;

            p.setStatic(nextParticleIsStatic);

            particles.add(p);
        }
    }
}

