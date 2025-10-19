package physics.gravity;

import physics.core.Particle;
import physics.core.Vector2D;
import java.util.ArrayList;

public class Gravity {
    private static final float G = 0.4f;

    public static void applyForces(ArrayList<Particle> particles) {
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size(); j++) {
                Particle p1 = particles.get(i);
                Particle p2 = particles.get(j);

                if (p1.mass == 0 || p2.mass == 0) {
                    continue;
                }

                Vector2D forceDirection = Vector2D.sub(p2.position, p1.position);
                float distance = forceDirection.mag();

                if (distance < 10) {
                    distance = 10;
                }

                forceDirection.normalize();

                float forceMagnitude = (G * p1.mass * p2.mass) / (distance * distance);
                forceDirection.mult(forceMagnitude);

                if (!p1.isStatic()) {
                    p1.applyForce(forceDirection);
                }
                if (!p2.isStatic()) {
                    p2.applyForce(Vector2D.sub(new Vector2D(0,0), forceDirection));
                }
            }
        }
    }
}
