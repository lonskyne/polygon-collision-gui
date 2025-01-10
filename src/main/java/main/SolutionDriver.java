package main;

import main.model.Interval;
import main.model.Line;
import main.model.MyPoint2D;
import main.model.Polygon;
import main.view.CanvasView;

import java.util.List;

public class SolutionDriver {
    private static SolutionDriver instance;
    private static int speed;

    private Polygon lPoly, rPoly;
    private boolean calculatingLeft = true;

    Line minLine = null;

    public static SolutionDriver getInstance() {
        if(instance == null)
            instance = new SolutionDriver();
        return instance;
    }

    private SolutionDriver() {
    }

    public void solve() {
        lPoly = CanvasView.getInstance().getlPoly();
        rPoly = CanvasView.getInstance().getrPoly();

        List<Interval> lpVisible = lPoly.getVisible();
        calculatingLeft = false;
        List<Interval> rpVisible = rPoly.getVisible();

        Line lrMinLine = findClosestCollision(lPoly, rPoly, rpVisible);
        Line rlMinLine = findClosestCollision(rPoly, lPoly, lpVisible);

        if(lrMinLine != null) {
            minLine = lrMinLine;
            lPoly.setMinPoint(minLine.getBegPoint());
            rPoly.setMinPoint(minLine.getEndPoint());
        }

        if((lrMinLine == null && rlMinLine != null) || (rlMinLine != null && Math.abs(rlMinLine.getBegPoint().getX() - rlMinLine.getEndPoint().getX()) < Math.abs(lrMinLine.getBegPoint().getX() - lrMinLine.getEndPoint().getX()))) {
            minLine = rlMinLine;
            lPoly.setMinPoint(minLine.getEndPoint());
            rPoly.setMinPoint(minLine.getBegPoint());
        }
    }

    public void move() {
        lPoly.moveRight(Math.abs(minLine.getBegPoint().getX() - minLine.getEndPoint().getX()));
    }

    private Line findClosestCollision(Polygon poly, Polygon otherPoly, List<Interval> otherVisible) {
        double minDist = Double.MAX_VALUE;
        Line minLine = null;

        for(MyPoint2D p : poly.getPoints()) {
            poly.setCurrentPointCheck(p);

            int l = 0, r = otherVisible.size() - 1, mid = (l + r) / 2;

            while(l <= r && otherVisible.get(mid).contains(p.getY()) != 0) {
                otherPoly.setbSearchL(new MyPoint2D(otherVisible.get(l).getLine().getIntersectedX(otherVisible.get(l).getBegY()), otherVisible.get(l).getBegY()));
                otherPoly.setbSearchR(new MyPoint2D(otherVisible.get(r).getLine().getIntersectedX(otherVisible.get(r).getEndY()), otherVisible.get(r).getEndY()));

                if(otherVisible.get(mid).getEndY() < p.getY())
                    l = mid + 1;
                else
                    r = mid - 1;

                try {
                    Thread.sleep(speed);
                } catch(Exception e) {
                    e.printStackTrace();
                }

                mid = (l + r) / 2;
            }

            if(l <= r) {
                Line distLine = new Line(new MyPoint2D(p.getX(), p.getY()), new MyPoint2D(otherVisible.get(mid).getLine().getIntersectedX(p.getY()), p.getY()));
                otherPoly.setCurrentDistance(distLine);

                double dist = Math.abs(distLine.getBegPoint().getX() - distLine.getEndPoint().getX());

                if(dist < minDist) {
                    minDist = dist;
                    minLine = distLine;
                }
            }

            try {
                Thread.sleep(speed);
            } catch(Exception e) {
                e.printStackTrace();
            }

            otherPoly.setCurrentDistance(null);
        }

        poly.setCurrentPointCheck(null);
        otherPoly.setbSearchL(null);
        otherPoly.setbSearchR(null);

        return minLine;
    }

    public boolean isCalculatingLeft() {
        return calculatingLeft;
    }

    public static void setSpeed(int speed) {
        SolutionDriver.speed = speed;
    }

    public static int getSpeed() {
        return speed;
    }
}
