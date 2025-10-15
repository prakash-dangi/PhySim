package physics.electrostatics;

import physics.core.Particle;
import physics.core.Vector2D;
import java.util.ArrayList;

public class Electrostatics {
    private static final float COULOMB_CONSTANT = 3f;
    public static void applyForces(ArrayList<Particle> particles) {
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size(); j++) {
                Particle p1 = particles.get(i);
                Particle p2 = particles.get(j);

                // Don't calculate forces if one of the particles has no charge.
                if (p1.charge == 0 || p2.charge == 0) {
                    continue;
                }

                Vector2D forceDirection = Vector2D.sub(p2.position, p1.position);
                float distance = forceDirection.mag();

                if (distance < 5) {
                    distance = 5;
                }

                forceDirection.normalize();

                float forceMagnitude = COULOMB_CONSTANT * (p1.charge * p2.charge) / (distance * distance);

                forceDirection.mult(forceMagnitude);

                if (!p1.isStatic()) {
                    p1.applyForce(Vector2D.sub(new Vector2D(0,0), forceDirection));
                }

                if (!p2.isStatic()) {
                    p2.applyForce(forceDirection);
                }
            }
        }
    }
}
