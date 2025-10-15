package Physics.Lines.helpers;

import processing.core.PApplet;

import java.util.ArrayList;

public class Updater {
    PApplet sketcher;
    ArrayList<LineSegment> list;
    ArrayList<Dot> dotList;
    float increment;

    public Updater(PApplet sketcher, ArrayList<LineSegment> list, ArrayList<Dot> dotList, float increment) {
        this.sketcher = sketcher;
        this.list = list;
        this.dotList = dotList;
        this.increment = increment;
    }

    public void update() {
        for (LineSegment l : list) {
            updateLine(l);
        }

        if (dotList == null) {

        }
        else{
            for (Dot d : dotList) {
                updateDot(d);
            }
        }
    }

    public void updateDot(Dot d) {
        float a = d.angle;
        a += increment;
        d.angle = a; // new angle stored for further iterations
//        d.x1 = d.x1 + sketcher.cos(sketcher.radians(a));
//        d.x2 = d.x2 + sketcher.sin(sketcher.radians(a));
//        d.x1 = d.x1 + d.length * sketcher.cos(sketcher.radians(a));
//        d.x2 = d.x2 + d.length * sketcher.sin(sketcher.radians(a));
        d.x1 = d.centerX + d.length * sketcher.cos(sketcher.radians(a));
        d.x2 = d.centerY + d.length * sketcher.sin(sketcher.radians(a));
    }

    public void updateLine(LineSegment l) {
        float a = l.angle;
//        float a = sketcher.mouseX*sketcher.mouseY;
        a += increment;
//        a += sketcher.mouseX;
        l.angle = a;
        l.x2 = l.x1  + l.length * (sketcher.cos(sketcher.radians(a)));
        l.y2 = l.y1 + l.length * (sketcher.sin(sketcher.radians(a)));
    }
}
