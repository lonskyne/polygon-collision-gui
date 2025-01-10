package main.model;

import main.SolutionDriver;
import main.avl.AVLTree;
import main.avl.Node;
import main.observer.MyObservable;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Polygon extends MyObservable {

    private final List<MyPoint2D> points = new ArrayList<>();
    private final List<Line> lines = new ArrayList<>();
    private boolean finished = false;
    AVLTree<Interval> tree = new AVLTree<>();

    private String pointString = "";

    public String getPointString() {
        return pointString;
    }

    // Drawing
    private MyPoint2D sweepLine = null;
    List<Interval> statusIntervals = new ArrayList<>();
    private final List<Interval> visible = new ArrayList<>();

    private MyPoint2D currentPointCheck = null;
    private MyPoint2D bSearchL = null, bSearchR = null;
    private Line currentDistance = null;
    private MyPoint2D minPoint = null;
    private double moveDistance = 0;

    public void addPoint(double x, double y) {
        if(!pointString.isEmpty())
            pointString += "\n";
        pointString += x + ", " + y;

        if(points.isEmpty()) {
            points.add(new MyPoint2D(x, y));
            return;
        }

        if(points.size() > 2 && points.getFirst().distancePow2(new MyPoint2D(x, y)) < 10) {
            finished = true;

            points.add(new MyPoint2D(points.getFirst().getX(), points.getFirst().getY()));

            lines.add(new Line(points.get(points.size() - 2), points.getLast()));

            points.getLast().setLine(lines.getLast());
            points.get(points.size() - 2).setLine(lines.getLast());
        }
        else {
            MyPoint2D end = new MyPoint2D(x, y);

            lines.add(new Line(points.getLast(), end));

            end.setLine(lines.getLast());
            points.getLast().setLine(lines.getLast());

            points.add(end);

            points.add(new MyPoint2D(x, y));
        }

        notify("addPoint called");
    }

    private Line2D getLineFromInterval(Interval interval) {
        double x1 = interval.getLine().getBegPoint().getX(), y1 = interval.getLine().getBegPoint().getY();
        double x2 = interval.getLine().getEndPoint().getX(), y2 = interval.getLine().getEndPoint().getY();
        double ya = interval.getBegY(), yb = interval.getEndY();
        double m = (y2 - y1) / (x2 - x1);

        double xa = (ya - y1) / m + x1;
        double xb = (yb - y1) / m + x1;

        return new Line2D.Double(xa, ya, xb, yb);
    }

    public void paint(Graphics2D g) {
        g.translate(moveDistance, 0);

        g.setStroke(new BasicStroke(3));
        g.setPaint(new GradientPaint(0, 0, Color.BLACK, 100, 0, Color.BLACK));

        for(Line line : lines)
            g.draw(new Line2D.Double(line.getBegPoint().getX(), line.getBegPoint().getY(), line.getEndPoint().getX(), line.getEndPoint().getY()));

        g.setPaint(new GradientPaint(0, 0, Color.RED, 100, 0, Color.RED));
        for(Interval interval : visible)
            g.draw(getLineFromInterval(interval));

        g.setPaint(new GradientPaint(0, 0, Color.BLUE, 100, 0, Color.BLUE));

        if(sweepLine != null) {
            g.draw(new Line2D.Double(0, sweepLine.getY(), 3000, sweepLine.getY()));
            g.fillRect((int) sweepLine.getX() - 5, (int) sweepLine.getY() - 5, 10, 10);
        }

        g.setPaint(new GradientPaint(0, 0, Color.GREEN, 100, 0, Color.GREEN));

        for(Interval interval : statusIntervals)
            g.draw(getLineFromInterval(interval));

        g.setPaint(new GradientPaint(0, 0, Color.CYAN, 100, 0, Color.CYAN));

        if(currentPointCheck != null)
            g.fillRect((int) currentPointCheck.getX() - 5, (int) currentPointCheck.getY() - 5, 10, 10);

        g.setPaint(new GradientPaint(0, 0, Color.ORANGE, 100, 0, Color.ORANGE));

        if(bSearchL != null && bSearchR != null) {
            g.fillRect((int) bSearchL.getX() - 5, (int) bSearchL.getY() - 5, 10, 10);
            g.fillRect((int) bSearchR.getX() - 5, (int) bSearchR.getY() - 5, 10, 10);
        }

        if(currentDistance != null)
            g.draw(new Line2D.Double(currentDistance.getBegPoint().getX(), currentDistance.getBegPoint().getY(), currentDistance.getEndPoint().getX(), currentDistance.getEndPoint().getY()));

        g.setPaint(new GradientPaint(0, 0, Color.MAGENTA, 100, 0, Color.MAGENTA));

        if(minPoint != null)
            g.fillRoundRect((int)minPoint.getX() - 5, (int)minPoint.getY() - 5, 10, 10, 5, 5);

        g.translate(-moveDistance, 0);
    }

    public List<Interval> getVisible() {
        visible.clear();
        Collections.sort(points);

        Double prevIntervalEnd = null;
        Line prevLine = null, minLine;

        for (MyPoint2D point : points) {
            sweepLine = point;
            Node<Interval> min = tree.getMin(tree.getRoot());

            minLine = null;

            if(min != null && min.getKey().getLine() != null)
                minLine = tree.getMin(tree.getRoot()).getKey().getLine();

            if(!visible.isEmpty())
                prevLine = visible.getLast().getLine();

            double EPSILON = 0.00001;

            if (minLine == prevLine && minLine != null)
                visible.getLast().setEndY(point.getY());
            else if (prevIntervalEnd != null && Math.abs(prevIntervalEnd-point.getY()) > EPSILON) {
                Interval interval = new Interval(prevIntervalEnd, point.getY(), tree.getMin(tree.getRoot()).getKey().getLine());
                visible.add(interval);
            }

            if (Math.abs(point.getY() - point.getLine().getLineInterval().getBegY()) < EPSILON) {
                statusIntervals.add(point.getLine().getLineInterval());
                tree.insert(point.getLine().getLineInterval());
            }
            else {
                statusIntervals.remove(point.getLine().getLineInterval());
                tree.remove(point.getLine().getLineInterval());
            }

            if(!visible.isEmpty())
                prevIntervalEnd = visible.getLast().getEndY();
            else
                prevIntervalEnd = point.getY();

            try {
                Thread.sleep(SolutionDriver.getSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            notify("Polygon change 1");
        }

        sweepLine = null;

        notify("Polygon change 2");

        return visible;
    }

    public void moveRight(double d) {
        while(moveDistance < d) {
            moveDistance += 0.00001;
            notify("Updated movement");
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public List<MyPoint2D> getPoints() {
        return points;
    }

    public void setCurrentPointCheck(MyPoint2D currentPointCheck) {
        this.currentPointCheck = currentPointCheck;
        notify("Changed point check.");
    }

    public void setbSearchL(MyPoint2D bSearchL) {
        this.bSearchL = bSearchL;
        notify("Changed bsearch l.");
    }

    public void setbSearchR(MyPoint2D bSearchR) {
        this.bSearchR = bSearchR;
        notify("Changed bsearch r.");
    }

    public void setCurrentDistance(Line currentDistance) {
        this.currentDistance = currentDistance;
        notify("Set current distance.");
    }

    public void setMinPoint(MyPoint2D minPoint) {
        this.minPoint = minPoint;
        notify("Set min line.");
    }

    public void setMoveDistance(int moveDistance) {
        this.moveDistance = moveDistance;
        notify("Set move distance.");
    }

    public MyPoint2D getMinPoint() {
        return minPoint;
    }
}
