package Physics.Electrostatics;
import Physics.Vectors.Vector;
import processing.core.*;

import java.util.ArrayList;

public class TwoCharges extends PApplet {
    double K = 9E9;

    public static void main(String[] args) {
        PApplet.main("Physics.Electrostatics.TwoCharges");
    }

    public void settings() {
        size(800, 800);
        smooth(8);
    }

    float[] eField(ArrayList<PointCharge> charges, int x, int y) {
        float[] efield = new float[]{0f, 0f, 0f}; // [magnitude, x-component, y-component]

        float ex = 0f;
        float ey = 0f;

        for (PointCharge q : charges) {
            float cx = q.chargeX - x;
            float cy = q.chargeY - y;
            float rSquared = cx * cx + cy * cy;
            float r = (float) Math.sqrt(rSquared);

            if (r == 0) {
                continue; // skip field contribution at the charge's location
            }

            float qField = (float) (K * q.charge / rSquared); // magnitude of field

            float dx = cx / r; // X direction unit vector of electric field by current charge
            float dy = cy / r; // Y direction unit vector of electric field by current charge

            ex += qField * dx;
            ey += qField * dy;

            efield[1] = dx + efield[1]; // resultant X direction unit vector of electric field by all charges
            efield[2] = dy + efield[2]; // resultant Y direction unit vector of electric field by all charges
        }

        float magnitude = (float) Math.sqrt(ex * ex + ey * ey);
        efield[0] = magnitude;

        return efield;
    }


    public void setup() {
        background(0);
        stroke(255, 255, 255);
        noFill();
        noLoop();
    }

    public void draw() {
        background(0);
        int length = 5;
        int columns = 800;
        int rows = 800;
        ArrayList<PointCharge> charges = new ArrayList<>();
        Vector[][] field = new Vector[rows][columns];

        PointCharge charge1 = new PointCharge(1000, 300, 300);
        PointCharge charge2 = new PointCharge(1000, 400, 400);
//        PointCharge charge3 = new PointCharge(1000, 300, 400);
//        PointCharge charge4 = new PointCharge(1000, 400, 300);
        charges.add(charge1);
        charges.add(charge2);
//        charges.add(charge3);
//        charges.add(charge4);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                field[i][j] = new Vector(this);
            }
        }

        float minE = Float.MAX_VALUE;
        float maxE = Float.MIN_VALUE;

        float density = map(length, 0, 1f, 1, length);
        for (int i = 0; i < rows; i += density * 2) {
            for (int j = 0; j < columns; j += density * 2) {
                float[] efield = eField(charges, i, j);
                if (efield[0] < minE) minE = efield[0];
                if (efield[0] > maxE) maxE = efield[0];
            }
        }

        for (int i = 0; i < rows; i += density * 2) {
            for (int j = 0; j < columns; j += density * 2) {
                float[] efield = eField(charges, j, i);
//                float scaledMag = map(magnitude[0], minE, maxE, 1, 10);
                float logMin = log(minE + 1);  // add 1 to avoid log(0)
                float logMax = log(maxE + 1);
                float logMag = log(efield[0] + 1);
                float scaledMag = map(logMag, logMin, logMax, 2, 15);

                field[j][i].magnitude = scaledMag;
//                field[j][i].magnitude = length; // magnitude[0];
                field[j][i].direction[0] = efield[1];
                field[j][i].direction[1] = efield[2];
                field[j][i].drawVector(j, i);
            }
        }
    }
}
