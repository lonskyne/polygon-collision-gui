package main.model;

import main.SolutionDriver;

public class Interval implements Comparable<Interval> {
    private double begY, endY;
    private Line line;

    public Interval(double begY, double endY, Line line) {
        this.begY = begY;
        this.endY = endY;
        this.line = line;
    }

    public double getBegY() {
        return begY;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public Line getLine() {
        return line;
    }

    public int contains(double v){
        if(v > begY && v < endY)
            return 0;

        return -1;
    }


    @Override
    public int compareTo(Interval other) {
        if (this == other) return 0;

        final double EPSILON = 0.00001;
        int resultForSmaller = -1;
        int resultForLarger = 1;

        if (SolutionDriver.getInstance().isCalculatingLeft()) {
            resultForSmaller = 1;
            resultForLarger = -1;
        }

        if (contains(other.getBegY()) == 0) {
            double intersectX = line.getIntersectedX(other.getBegY());
            double deviation = intersectX - other.getLine().getTopx();
            if (deviation < -EPSILON) return resultForSmaller;
            if (deviation > EPSILON) return resultForLarger;
        }

        if (contains(other.getEndY()) == 0) {
            double intersectX = line.getIntersectedX(other.getEndY());
            double deviation = intersectX - other.getLine().getBotx();
            if (deviation < -EPSILON) return resultForSmaller;
            if (deviation > EPSILON) return resultForLarger;
        }

        if (other.contains(getBegY()) == 0) {
            double intersectX = other.getLine().getIntersectedX(getBegY());
            double deviation = intersectX - getLine().getTopx();
            if (deviation < -EPSILON) return resultForLarger;
            if (deviation > EPSILON) return resultForSmaller;
        }

        if (other.contains(getEndY()) == 0) {
            double intersectX = other.getLine().getIntersectedX(getEndY());
            double deviation = intersectX - getLine().getBotx();
            if (deviation < -EPSILON) return resultForLarger;
            if (deviation > EPSILON) return resultForSmaller;
        }

        if (this.getEndY() - other.getEndY() > EPSILON)
            return resultForLarger;

        return resultForSmaller;
    }

}
