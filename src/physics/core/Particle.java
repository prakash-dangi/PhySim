package physics.core;

import processing.core.PApplet;
import java.util.LinkedList;

public class Particle {
	public String name;
	private int color;
	private int colorIndex;

	public Vector2D position;
	public Vector2D velocity;
	public Vector2D acceleration;
    
    public float mass;
    public float charge;
    public float radius;
    private boolean isStatic = false;

    private static final int TRAIL_LENGTH = 500;
    private LinkedList<Vector2D> trail = new LinkedList<>();

    public Particle(float x, float y, float mass, float charge, float radius) {
        this.name = "Particle";
        this.colorIndex = (charge >= 0) ? 0 : 1;
        this.color = SimConstants.PRESET_COLORS[colorIndex];
        this.position = new Vector2D(x, y);
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
        this.mass = mass;
        this.charge = charge;
        this.radius = radius;
    }

    public Particle(String name, int color, float mass, float charge, float radius) {
        this(0, 0, mass, charge, radius);
        this.name = name;
        this.color = color;
        for (int i = 0; i < SimConstants.PRESET_COLORS.length; i++) {
            if (SimConstants.PRESET_COLORS[i] == color) {
                this.colorIndex = i;
                break;
            }
        }
    }

    public void update(float damping) {
        if (!isStatic) {
            this.velocity.add(this.acceleration); 
            this.velocity.mult(damping);
            this.position.add(this.velocity);

            trail.add(new Vector2D(this.position.x, this.position.y));
            if (trail.size() > TRAIL_LENGTH) {
                trail.removeFirst();
            }
        }
    }

    public void applyForce(Vector2D force) {
        if (this.mass > 0) {
            Vector2D f = Vector2D.div(force, this.mass);
            this.acceleration.add(f);
        }
    }

    public void resetAcceleration() {
        this.acceleration.mult(0);
    }

    public void display(PApplet p, boolean showTrails) {
        if (showTrails && trail.size() > 1) {
            p.noFill();
            p.stroke(this.color, 150);
            p.strokeWeight(2);
            p.beginShape();
            for (Vector2D v : trail) {
                p.vertex(v.x, v.y);
            }
            p.endShape();
        }

        p.noStroke();
        p.fill(this.color);
        p.ellipse(position.x, position.y, radius * 2, radius * 2);
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
        if (isStatic) {
            this.velocity = new Vector2D(0, 0);
            this.acceleration = new Vector2D(0, 0);
        }
    }

    public int getParticleColor() {
        return this.color;
    }

    public void cycleColor(int direction) {
        colorIndex += direction;
        if (colorIndex >= SimConstants.PRESET_COLORS.length) {
            colorIndex = 0;
        } else if (colorIndex < 0) {
            colorIndex = SimConstants.PRESET_COLORS.length - 1;
        }

        this.color = SimConstants.PRESET_COLORS[colorIndex];
    }
}
