package Physics.Electrostatics;
import processing.core.*;

import java.util.ArrayList;

public class ChargesColor extends PApplet {
    double K = 9E0;
    public static void main(String[] args){
        PApplet.main("Physics.Electrostatics.ChargesColor");
    }

    public void settings(){
        size(800, 800);
        smooth(0);
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
                return efield; // skip field contribution at the charge's location
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
//        stroke(255, 255, 255);
        noFill();
//        noLoop();
    }

    public void draw() {
        background(0);
        int gap = 1;
        int columns = 600;
        int rows = 600;
        ArrayList<PointCharge> charges = new ArrayList<>();
        DotField[][] field = new DotField[rows][columns];

        PointCharge charge1 = new PointCharge(1000, mouseX, mouseY);
        PointCharge charge2 = new PointCharge(1000, 400, 300);
        PointCharge charge3 = new PointCharge(1000, 400, 500);
//        PointCharge charge4 = new PointCharge(-1000, 300, 400);
        charges.add(charge1);
        charges.add(charge2);
        charges.add(charge3);
//        charges.add(charge4);

//        for (int i = 200; i < rows; i += gap*80) {
//            for (int j = 200; j < columns; j += gap*80) {
//                PointCharge charge = new PointCharge(100, j, i);
//                charges.add(charge);
//            }
//        }

        for (int i = 200; i < rows; i++) {
            for (int j = 200; j < columns; j++) {
                field[i][j] = new DotField(this, i, j);
            }
        }

        float minE = Float.MAX_VALUE;
        float maxE = Float.MIN_VALUE;

// First pass to calculate min and max
        for (int i = 200; i < rows; i += gap) {
            for (int j = 200; j < columns; j += gap) {
                float[] efield = eField(charges, j, i);
                if (efield[0] < minE) minE = efield[0];
                if (efield[0] > maxE) maxE = efield[0];
            }
        }

        float base = 1.2f;
        for (int i = 200; i < rows; i += gap) {
            for (int j = 200; j < columns; j += gap) {
                float[] efield = eField(charges, j, i);
                float magnitude = efield[0];

                // Logarithmic scaling (for better visual contrast)
//                float logMin = log(minE + 1)/log(base);
//                float logMax = log(maxE + 1)/log(base);
//                float logMag = log(magnitude + 1)/log(base);

                float safeMinE = max(minE, 1e-6f);  // Avoid log(0)
                float safeMag = max(magnitude, 1e-6f);

// Double logarithmic compression
                float compressedMag = log(log(safeMag + 1) + 1);
                float compressedMin = log(log(safeMinE + 1) + 1);
                float compressedMax = log(log(maxE + 1) + 1);

                float brightness = map(compressedMag, compressedMin, compressedMax, 0, 255);

// Smooth rainbow-style gradient (blue → red)
                stroke(brightness, 255 - brightness, 255 - brightness * 0.5f);



//                int numBins = 15;
//                int binIndex = (int) map(magnitude, minE, maxE, 0, numBins - 1);
//                float brightness = map(binIndex, 0, numBins - 1, 0, 255);

                // Map to a brightness or color
//                float brightness = map(logMag, logMin, logMax, 0, 255);

//                float brightness = map(sqrt(magnitude), sqrt(minE), sqrt(maxE), 0, 255);

//                float gamma = 0.15f;
//                float brightness = map(pow(magnitude, gamma), pow(minE, gamma), pow(maxE, gamma), 0, 255);

//                float brightness = map(magnitude, minE, maxE, 0, 255);

//                stroke(brightness, 0, 255 - brightness);  // from purple-ish to pink
//
//                stroke(brightness, brightness, 0);  // from red to yellow
//                noStroke();

//                colorMode(HSB, 360, 100, 100);
//                float hue = map(binIndex, 0, numBins - 1, 0, 360);
//                stroke(hue, 100, 100);

                field[j][i].magnitude = magnitude;
//                field[j][i].magnitude = length; // magnitude[0];
                field[j][i].drawDot();
            }
        }
    }
}
