package physics.core;

import processing.core.PApplet;

public class Particle {
    public Vector2D position;
    public Vector2D velocity;
    public Vector2D acceleration;

    public float mass;
    public float charge;
    public float radius;
    private int particleColor;
    private boolean isStatic;

    public Particle(float x, float y, float mass, float charge, float radius) {
        this.position = new Vector2D(x, y);
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
        this.mass = mass;
        this.charge = charge;
        this.radius = radius;
        this.isStatic = false;

        if (charge > 0) {
            this.particleColor = 0xFFFF0000;
        } else if (charge < 0) {
            this.particleColor = 0xFF0000FF;
        } else {
            this.particleColor = 0xFF808080;
        }
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
        }
    }

    public void display(PApplet p) {
        p.noStroke();
        p.fill(this.particleColor);
        p.ellipse(this.position.x, this.position.y, this.radius * 2, this.radius * 2);
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public boolean isStatic() {
        return isStatic;
    }
}
