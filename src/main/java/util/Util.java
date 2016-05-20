package util;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Created by viacheslav on 11.05.2016.
 */
public class Util {
    public static Double calcAverage(List<Double> list) {
        double acc = 0;
        for (Double x : list) {
            acc += x;
        }
        acc /= list.size();
        return acc;
    }

    public static double decartDist(Point2D.Double p1, Point2D.Double p2) {
        return Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX())
                + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
    }
}
