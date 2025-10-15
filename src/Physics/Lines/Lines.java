package Physics.Lines;

import Physics.Lines.helpers.Dot;
import Physics.Lines.helpers.LineSegment;
import Physics.Lines.helpers.Updater;
import processing.core.*;

import java.util.ArrayList;

public class Lines extends PApplet{
    int lineLength = 15;
    int columns = 600;
    int rows = 600;
    ArrayList<LineSegment> lines = new ArrayList<>();
    ArrayList<Dot> dots = new ArrayList<>();

    public static void main(String[] args){
        PApplet.main("Physics.Lines.Lines");
    }

    public void settings(){
        size(800, 800);
        smooth(8);
    }

    public void setup(){
        background(0);
        stroke(255);
        for (int i = 200; i<columns; i += lineLength*2){
            for (int j = 200; j<rows; j += lineLength*2){
//                float a = random(0, 360);
                float a = 0;
                float angle_x = lineLength*cos(radians(a));
                float angle_y = lineLength*sin(radians(a));
                LineSegment l = new LineSegment(this, i, j, i + angle_x, j + angle_y, lineLength);
                l.angle = a;
                Dot d = new Dot(this, i, j,i + angle_x, j + angle_y, lineLength, a);
                l.drawLine();
                lines.add(l);
                d.adddot();
                dots.add(d);
            }
        }
//        noLoop();
    }

    Updater u = new Updater(this, lines, dots, 5);
    public void draw(){
        background(0);
        u.update();

        for(LineSegment l : lines){
            l.drawLine();
        }

        for (Dot d : dots){
            stroke(255, 0, 0);
            strokeWeight(4);
            d.adddot();
            strokeWeight(1);
//            stroke(255);
        }
    }
}



















//inside draw
//int a = (int)random(0, 360);
//float angle_x = lineLength*cos(radians(a));
//float angle_y = lineLength*sin(radians(a));
//line(i, j, i + angle_x, j + angle_y);
//i+=lineLength*2;
//        if (i>800){
//j+=lineLength;
//i = 0;
//        }
//
//        if (j>800){
//noLoop();