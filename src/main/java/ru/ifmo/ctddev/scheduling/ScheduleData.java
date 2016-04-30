package ru.ifmo.ctddev.scheduling;

import ru.ifmo.ctddev.Config;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * Created by viacheslav on 01.12.2015.
 * <p>
 * This class holds all data, needed for scheduling.
 */
public class ScheduleData implements Cloneable, Comparable<ScheduleData>, Comparator<int[]> {

    // there are N pairs of points. N "orders". N pickups and N drops.

    /**
     * coordinates of each point. Size 2*N
     */
    private Point2D.Double[] points;

    /**
     * depot-point. here starts and ends each route.
     * It's usually the mass-center af all points.
     */
    private Point2D.Double depot;

    /**
     * The whole path through src and dst points.
     * Each value is an index in {@code ScheduleData.points} array, with a sign:
     * if positive or 0 - pick the object up. if negative - drop the object.
     * 0,..(N-1),(-N),..(-2N+1).
     */
    private int[] route;

    /**
     * total cost of current route.
     * If -1, then it's not calculated yet.
     */
    private double cost;

    /**
     * Total count of orders (total count of pairs {src, dst} )
     * it's N, actually.
     */
    private int ordersNum;

    /**
     * Id's from original file. For future feature extraction.
     * Each integer here is an identifier of concrete src-dst pair.
     */
    private List<Integer> ids;

    /**
     * This counter shows, how much times, the fit-function was couted.
     * The fit-function is the length of the path.
     */
    private int fitFunctionCallsCount = 0;

    /**
     * This publi constructor specifies coordinates of considered points and the depot-point.
     * @param points
     * @param depot
     */
    public ScheduleData(List<Point2D.Double> points, Point2D.Double depot) {
        this.ordersNum = points.size() / 2;
        this.points = points.toArray(new Point2D.Double[1]);
        this.depot = depot;
        this.route = new int[points.size()];
//        int[] intArray = ArrayUtils.toPrimitive(routeList.toArray(new Integer[routeList.size()]));
        clearRoute();
    }

    /**
     * When depot is not specified, mean of points is used.
     *
     * @param points
     */
    public ScheduleData(List<Point2D.Double> points) {
        this(points, calcCenter(points));
    }

    /**
     * Calculates the mass center of points.
     * @param points
     * @return
     */
    private static Point2D.Double calcCenter(List<Point2D.Double> points) {
        double x = 0;
        double y = 0;

        for (Point2D point : points) {
            x += point.getX();
            y += point.getY();
        }
        x /= points.size();
        y /= points.size();
        return new Point2D.Double(x, y);
    }


    /**
     * This function
     */
    public void clearRoute() {
        for (int i = 0; i < this.points.length; ++i) {
            if (i < ordersNum)
                route[i] = i;
            else
                route[i] = -i;
        }
        this.cost = -1;
        this.fitFunctionCallsCount = 0;
    }


    public void setRoute(int[] route) {
        this.route = route;
        this.cost = -1;
    }

    /**
     * checks construction constraints. (pairing, capacity, tw if present)
     *
     * @return true, if constraint is satisfied.
     */
    public boolean checkConstraints(int[] route) {
        Set<Integer> picked = new HashSet<Integer>();
        for (int p : route) {
            if (p >= 0) {
                picked.add(p);
                if (Config.enableConstraintCapacity && picked.size() > Config.maxCapacity)
                    return false;
            } else {
                if (Config.enableConstraintPairing && !picked.contains((-p) - ordersNum)) {
                    return false;
                }
                picked.remove((-p) - ordersNum);
            }
        }
        return true;
    }

    /**
     * checks construction constraints in internal data. (pairing, capacity, tw if present)
     *
     * @return
     */
    public boolean checkConstraints() {
        return checkConstraints(route);
    }


    /**
     * Provides the number of points, which is equal 2*N
     * @return
     */
    public int getSize() {
        return points.length;
//        return getOrdersNum();
    }

    /**
     * Provides Cost of internal data.
     * @return
     */
    public double getCost() {
        if (cost < 0) {
            this.cost = getCost(route);
        }
        return this.cost;
    }

    /**
     * Calculates cost of given route, according to distance-function implemented in this ScheduleData.
     * Increments fitFunctionCallCount.
     * @param route
     * @return
     */
    public double getCost(int[] route) {
        double acc = 0.0;
        for (int i = 1; i < route.length; ++i) {
            acc += dist(route[i - 1], route[i]);
        }
        ++fitFunctionCallsCount;
        return acc;
    }

    /**
     * Calculates cost of given route as a List, according to distance-function implemented in this ScheduleData.
     * @param route
     * @return
     */
    public double getCost(List<Integer> route) {
        double acc = 0.0;
        for (int i = 1; i < route.size(); ++i) {
            acc += dist(route.get(i - 1), route.get(i));
        }
        ++fitFunctionCallsCount;
        return acc;
    }

    /**
     * Calculated distance between two points according to the Euclidean metric.
     * @param s1
     * @param d1
     * @return
     */
    private double dist(int s1, int d1) {
        int s = s1 < 0 ? -s1 : s1;
        int d = d1 < 0 ? -d1 : d1;
        return Math.sqrt((points[s].getX() - points[d].getX()) * (points[s].getX() - points[d].getX())
                + (points[s].getY() - points[d].getY()) * (points[s].getY() - points[d].getY()));
    }

    /**
     * Provdes the number of pairs, N.
     * @return
     */
    public int getOrdersNum() {
        return ordersNum;
    }

    public Point2D.Double[] getPoints() {
        return points;
    }


    /**
     * Provides current route as list of points.
     * For future use by visualization.
     * @return
     */
    public List<Point2D.Double> getRouteAsPoints() {
        List<Point2D.Double> res = new ArrayList<>(route.length);

        for (int i : route) {
            res.add(points[route[i]]);
        }
        return res;
    }

    public int[] getRoute() {
        return route;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    /**
     * This clone method makes s partially shallow copy of Schedule data.
     * All object-fields reference the same objects except array rout[].
     *
     * @return a copy with fully copied rout[]
     */
    @Override
    public ScheduleData clone() {
        try {
            ScheduleData copy = (ScheduleData) super.clone();
            copy.setRoute(route.clone());
            return copy;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Provides a shallow copy with specified route[] field.
     *
     * @param route
     * @return
     */
    public ScheduleData clone(int[] route) {
        try {
            ScheduleData copy = (ScheduleData) super.clone();
            copy.setRoute(route);
            return copy;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a negative integer as this object is less
     * than the specified object.
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(ScheduleData o) {
        if (this.getCost() < o.getCost())
            return -1;
        if (this.getCost() > o.getCost())
            return 1;
        return 0;
    }

    /**
     * compares two routes through points, which are in this ScheduleData object.
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(int[] o1, int[] o2) {
        if (getCost(o1) < getCost(o2))
            return -1;
        if (getCost(o1) > getCost(o2))
            return 1;
        return 0;
    }

    public int getFitFunctionCallsCount() {
        return fitFunctionCallsCount;
    }


    // todo: ad method to serialize and deserialise data to/from file as json.
}
