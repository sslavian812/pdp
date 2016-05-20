package ru.ifmo.ctddev.scheduling;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Created by viacheslav on 20.05.2016.
 */
public class FixedDistanceScheduleData extends DecartScheduleData implements ScheduleData {
    private double[][] distances;

    public FixedDistanceScheduleData(List<Point2D.Double> points, Point2D.Double depot) {
        super(points, depot);
    }

    public FixedDistanceScheduleData(List<Point2D.Double> points) {
        super(points);
    }

    public void setDistances(double[][] distances) {
        if (distances.length != ordersNum
                || distances[0].length != ordersNum)
            throw new RuntimeException("wrong size of distances");
    }

    public void calcDistances() {
        distances = new double[ordersNum][ordersNum];
        for (int i = 0; i < distances.length; ++i) {
            for (int j = 0; j < distances[0].length; ++j) {
                distances[i][j] = dist(points[i], points[j]);
            }
        }
    }

    @Override
    public double dist(int s1, int d1) {
        int s = s1 < 0 ? -s1 : s1;
        int d = d1 < 0 ? -d1 : d1;
        if (distances == null)
            return dist(points[s], points[d]);
        else
            return distances[s][d];
    }
}
