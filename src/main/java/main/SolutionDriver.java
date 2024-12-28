package main;

import main.model.Interval;
import main.model.MyPoint2D;
import main.model.Polygon;
import main.view.CanvasView;

import java.util.ArrayList;
import java.util.List;

public class SolutionDriver {
    private static SolutionDriver instance;
    private static int speed;

    private Polygon lPoly, rPoly;

    public static SolutionDriver getInstance() {
        if(instance == null)
            instance = new SolutionDriver();
        return instance;
    }

    private SolutionDriver() {
    }

    public List<MyPoint2D> solve() {
        lPoly = CanvasView.getInstance().getlPoly();
        rPoly = CanvasView.getInstance().getrPoly();
        System.out.println("SOLVE");
        List<Interval> lpVisible = lPoly.getVisible(false);
        System.out.println("LEFT VISIBLE:");
        System.out.println(lpVisible.getFirst().getBegY() + " " + lpVisible.getFirst().getEndY());
        System.out.println();
        List<Interval> rpVisible = rPoly.getVisible(true);
        System.out.println("RIGHT VISIBLE:");
        System.out.println(rpVisible);
        System.out.println();

        return new ArrayList<MyPoint2D>();
    }

    public static void setSpeed(int speed) {
        SolutionDriver.speed = speed;
    }

    public static int getSpeed() {
        return speed;
    }
}
