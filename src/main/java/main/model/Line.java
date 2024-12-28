package main.model;

public class Line {
    private final MyPoint2D begPoint, endPoint;
    private final Interval lineInterval;

    public Line(MyPoint2D begPoint, MyPoint2D endPoint) {
        this.begPoint = begPoint;
        this.endPoint = endPoint;

        lineInterval = new Interval(Math.min(begPoint.getY(), endPoint.getY()), Math.max(begPoint.getY(), endPoint.getY()), this);
    }

    public MyPoint2D getBegPoint() {
        return begPoint;
    }

    public MyPoint2D getEndPoint() {
        return endPoint;
    }

    public double getIntersectedX(double y) {
        double x1 = begPoint.getX(), y1 = begPoint.getY();
        double x2 = endPoint.getX(), y2 = endPoint.getY();
        double m = (y2 - y1) / (x2 - x1);

        return (y - y1) / m + x1;
    }

    public double getTopx() {
        if (endPoint.getY() < begPoint.getY())
            return endPoint.getX();
        else
            return begPoint.getX();
    }

    public double getBotx() {
        if (endPoint.getY() < begPoint.getY())
            return begPoint.getX();
        else
            return endPoint.getX();
    }

    public Interval getLineInterval() {
        return lineInterval;
    }
}
