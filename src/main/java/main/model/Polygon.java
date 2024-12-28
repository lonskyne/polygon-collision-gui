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

    private MyPoint2D sweepLine = null;
    List<Interval> statusIntervals = new ArrayList<>();
    AVLTree<Interval> tree = new AVLTree<>();

    private final List<Interval> visible = new ArrayList<>();

    public void addPoint(double x, double y) {
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

    public void paint(Graphics2D g) {
        g.setPaint(new GradientPaint(0, 0, Color.BLACK, 100, 0, Color.BLACK));

        for(Line line : lines)
            g.draw(new Line2D.Double(line.getBegPoint().getX(), line.getBegPoint().getY(), line.getEndPoint().getX(), line.getEndPoint().getY()));

        g.setPaint(new GradientPaint(0, 0, Color.RED, 100, 0, Color.RED));
        for(Interval interval : visible) {
            double x1 = interval.getLine().getBegPoint().getX(), y1 = interval.getLine().getBegPoint().getY();
            double x2 = interval.getLine().getEndPoint().getX(), y2 = interval.getLine().getEndPoint().getY();
            double ya = interval.getBegY(), yb = interval.getEndY();
            double m = (y2 - y1) / (x2 - x1);

            double xa = (ya - y1) / m + x1;
            double xb = (yb - y1) / m + x1;

            g.draw(new Line2D.Double(xa, ya, xb, yb));
        }

        g.setPaint(new GradientPaint(0, 0, Color.BLUE, 100, 0, Color.BLUE));

        if(sweepLine != null) {
            g.draw(new Line2D.Double(0, sweepLine.getY(), 3000, sweepLine.getY()));
            g.fillRect((int) sweepLine.getX() - 5, (int) sweepLine.getY() - 5, 10, 10);
        }

        g.setPaint(new GradientPaint(0, 0, Color.GREEN, 100, 0, Color.GREEN));

        for(Interval interval : statusIntervals) {
            double x1 = interval.getLine().getBegPoint().getX(), y1 = interval.getLine().getBegPoint().getY();
            double x2 = interval.getLine().getEndPoint().getX(), y2 = interval.getLine().getEndPoint().getY();
            double ya = interval.getBegY(), yb = interval.getEndY();
            double m = (y2 - y1) / (x2 - x1);

            double xa = (ya - y1) / m + x1;
            double xb = (yb - y1) / m + x1;

            g.draw(new Line2D.Double(xa, ya, xb, yb));
        }
    }

    public List<Interval> getVisible(boolean fromLeft) {
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

            double EPSILON = 0.000001;

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

        minLine = null;
        sweepLine = null;

        notify("Polygon change 2");

        return visible;
    }

    public boolean isFinished() {
        return finished;
    }
}
