package physics.magnetism;

import physics.core.Particle;
import physics.core.Vector2D;
import java.util.ArrayList;

public class Magnetism {
    public static void applyForces(ArrayList<Particle> particles, float magneticFieldZ) {
        if (magneticFieldZ == 0) {
            return;
        }

        for (Particle p : particles) {
            if (p.charge == 0 || p.isStatic()) {
                continue;
            }

            float forceX = p.charge * p.velocity.y * magneticFieldZ;
            float forceY = p.charge * -p.velocity.x * magneticFieldZ;

            p.applyForce(new Vector2D(forceX, forceY));
        }
    }
}
	
