package Physics.Lines;

import Physics.Lines.helpers.LineSegment;
import Physics.Lines.helpers.Updater;
import processing.core.*;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Lines2 extends PApplet{
    int lineLength = 10;
    int columns = 800;
    int rows = 800;
    int[] angles = IntStream.rangeClosed(0, 360).toArray();
    int[] angles10 = IntStream.iterate(0, x -> x<=360, x -> x + 10).toArray();
    ArrayList<LineSegment> lines = new ArrayList<>();


    public static void main(String[] args){
        PApplet.main("Physics.Lines.Lines2");
    }

    public void settings(){
        size(800, 800);
        smooth(8);
    }

    public void setup(){
        background(0);
        stroke(255);
        strokeWeight(1);
        noFill();
        int index = 0;
        for (int j = 0; j<rows; j += lineLength*2){
            for (int i = 0; i<columns; i += lineLength*2){
                float a = angles[index];
                index++;

                if (index>360)
                    index = 0;
                float angle_x = lineLength*cos(radians(a));
                float angle_y = lineLength*sin(radians(a));
                LineSegment l = new LineSegment(this, i, j, i + angle_x, j + angle_y, lineLength);
                l.angle = a;
                l.drawLine();
                lines.add(l);
//                noLoop();
            }
        }
    }

    Updater u = new Updater(this, lines, null, 7);
    public void draw(){
        background(0);
        u.update();

        for (LineSegment l : lines){
            l.drawLine();
        }
    }
}
