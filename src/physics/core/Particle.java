package physics.core;

import processing.core.PApplet;
import java.util.ArrayList;
import java.util.LinkedList;

public class Particle {
    public Vector2D position;
    public Vector2D velocity;
    public Vector2D acceleration;

    public float mass;
    public float charge;
    public float radius;
    private int particleColor;
    private boolean isStatic;

    private LinkedList<Vector2D> trail;
    private static final int TRAIL_LENGTH = 500;

    public Particle(float x, float y, float mass, float charge, float radius) {
        this.position = new Vector2D(x, y);
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
        this.mass = mass;
        this.charge = charge;
        this.radius = radius;
        this.isStatic = false;
        this.trail = new LinkedList<>();

        updateColor();
    }

    public void applyForce(Vector2D force) {
        if (this.mass > 0) {
            Vector2D f = force;
            f.div(this.mass);
            this.acceleration.add(f);
        }
    }

    public void update() {
        if (!isStatic) {
            this.velocity.add(this.acceleration);
            this.position.add(this.velocity);
            this.acceleration.mult(0);

            trail.add(new Vector2D(this.position.x, this.position.y));

            if (trail.size() > TRAIL_LENGTH) {
                trail.removeFirst();
            }
        }
    }

    public void display(PApplet p, boolean showTrails) {
        if (showTrails && trail.size() > 1) {
            p.noFill();
            p.strokeWeight(1);
            p.beginShape();
            for (int i = 0; i < trail.size(); i++) {
                Vector2D pos = trail.get(i);
                int alpha = (int) PApplet.map(i, 0, trail.size(), 0, 150);
                p.stroke(particleColor, alpha);
                p.vertex(pos.x, pos.y);
            }
            p.endShape();
        }

        p.noStroke();
        p.fill(this.particleColor);
        p.ellipse(this.position.x, this.position.y, this.radius*2, this.radius*2);
    }

    public void updateColor() {
        if (charge > 0) {
            this.particleColor = 0xFFFF8888;
        } else if (charge < 0) {
            this.particleColor = 0xFF8888FF;
        } else {
            this.particleColor = 0xFFCCCCCC;
        }
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public boolean isStatic() {
        return isStatic;
    }
}
