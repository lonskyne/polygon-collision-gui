package main.model;

public class MyPoint2D implements Comparable<MyPoint2D> {
    private final double x, y;
    private Line line;

    public MyPoint2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public MyPoint2D(double x, double y, Line line) {
        this.x = x;
        this.y = y;
        this.line = line;
    }

    public double distancePow2(MyPoint2D otherPoint) {
        double deltaX = x - otherPoint.getX();
        double deltaY = y - otherPoint.getY();

        return deltaX * deltaX + deltaY * deltaY;
    }

    @Override
    public int compareTo(MyPoint2D mp) {
        return Double.compare(y, mp.getY());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }
}
