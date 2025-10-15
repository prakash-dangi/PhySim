package Physics.Electrostatics;
import Physics.Vectors.Vector;
import processing.core.*;

public class OneCharge extends PApplet{
    double K = 9E0;
    public static void main(String[] args){
        PApplet.main("Physics.Electrostatics.OneCharge");
    }

    public void settings(){
        size(800, 800);
        smooth(8);
    }

    float[] eField(PointCharge q, int chargeX, int chargeY, int x, int y){
        float cx = q.chargeX - x;
        float cy = q.chargeY - y;
        float r = (float)Math.sqrt(cx*cx + cy*cy);
        float[] efield = new float[3];
        efield[0] = (float)K*q.charge/(r);
        // direction:
        float dx = cx/r;
        float dy = cy/r;
        efield[1] = dx;
        efield[2] = dy;
        return efield;
    }

    public void setup(){
        background(255);
        stroke(255, 0, 0);
        noFill();
    }

    public void draw() {
        background(255);
        int length = 10;
        int columns = 800;
        int rows = 800;
        Vector[][] field = new Vector[rows][columns];

//        float charge = 1000;
        int chargeX = mouseX;
        int chargeY = mouseY;
        ellipse(mouseX, mouseY, 7, 7);
        PointCharge charge = new PointCharge(1000, mouseX, mouseY);

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                field[i][j] = new Vector(this);
            }
        }

        float minE = Float.MAX_VALUE;
        float maxE = Float.MIN_VALUE;

        for(int i = 0; i < rows; i += length * 2){
            for (int j = 0; j < columns; j += length * 2){
                if (i == chargeY && j == chargeX) continue;
                float[] efield = eField(charge, chargeX, chargeY, i, j);
                if (efield[0] < minE) minE = efield[0];
                if (efield[0] > maxE) maxE = efield[0];
            }
        }

        for(int i = 0; i<rows; i += length*2){
            for (int j = 0; j<columns; j += length*2){
                if (i == chargeY && j == chargeX){
                    continue;
                }

                float[] efield = eField(charge, chargeX, chargeY, j, i);
//                float scaledMag = map(magnitude[0], minE, maxE, 1, 10);
                float logMin = log(minE + 1);  // add 1 to avoid log(0)
                float logMax = log(maxE + 1);
                float logMag = log(efield[0] + 1);
                float scaledMag = map(logMag, logMin, logMax, 2, 10);

                field[j][i].magnitude = scaledMag;
//                field[j][i].magnitude = length; // magnitude[0];
                field[j][i].direction[0] = efield[1];
                field[j][i].direction[1] = efield[2];
                field[j][i].drawVector(j, i);
            }
        }
    }
}
