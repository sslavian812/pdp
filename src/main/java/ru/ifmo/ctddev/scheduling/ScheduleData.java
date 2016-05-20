package ru.ifmo.ctddev.scheduling;

import javafx.util.Pair;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.List;

/**
 * Created by viacheslav on 20.05.2016.
 */
public interface ScheduleData extends Cloneable, Comparable<ScheduleData>, Comparator<int[]> {
    void clearRoute();

    void setRoute(int[] route);

    boolean checkConstraints(int[] route);

    boolean checkConstraints();

    double getCost();

    double getCost(int[] route);

    double getCost(List<Integer> route);

    int getOrdersNum();

    int[] getRoute();

    ScheduleData clone();

    @Override
    int compareTo(ScheduleData o);

    @Override
    int compare(int[] o1, int[] o2);

    int getFitFunctionCallsCount();

    void trimFitFunctionCalls();

    void setIds(List<Integer> ids);

    Point2D.Double[] getPoints();

    List<Point2D.Double> getSrcOrDstPoints(boolean getSrc);

    List<Pair<Point2D.Double, Point2D.Double>> getAllSrcDstPairs();

    default int getSize() {
        return getOrdersNum() * 2;
    }
}
