package main.model;

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
    public int compareTo(Interval o) {
        if (this == o)
            return 0;

        int res1 = 1;
        int res0 = -1;

//        if (this.projection == Projection.LEFT){
//            res1 = -1;
//            res0 = 1;
//        }

        if (contains(o.getBegY()) == 0){
            double x = line.getIntersectedX(o.getBegY());
            double deviation = x - o.getLine().getTopx();
            if (deviation < -0.001)
                return res1;

            if (deviation > 0.001)
                return res0;
        }
        if (contains(o.getEndY()) == 0){
            double x = line.getIntersectedX(o.getEndY());
            double deviation = x - o.getLine().getBotx();
            if (deviation < -0.001)
                return res1;

            if (deviation > 0.001)
                return res0;
        }
        if (o.contains(getBegY()) == 0){
            double x = o.getLine().getIntersectedX(getBegY());
            double deviation = x - getLine().getTopx();
            if (deviation < -0.001)
                return res0;

            if (deviation > 0.001)
                return res1;
        }
        if (o.contains(getEndY()) == 0){
            double x = o.getLine().getIntersectedX(getEndY());
            double deviation = x - getLine().getBotx();
            if (deviation < -0.001)
                return res0;

            if (deviation > 0.001)
                return res1;
        }
        if (this.getEndY() - o.getEndY() > 0.01 )
            return res0;

        return 1;
    }
}
