package ru.ifmo.ctddev.scheduling;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by viacheslav on 01.12.2015.
 */
public class ScheduleData {
    private Point2D[] points;
    private Point2D depot;
    private int[] route;
    private double cost;
    int ordersNum;

    public ScheduleData(List<Point2D> points, Point2D depot) {
        this.ordersNum = points.size() / 2;
        this.points = points.toArray(new Point2D[1]);
        this.depot = depot;
        this.route = new int[points.size()];
        this.cost = -1;
        clearRoute();
    }

    public ScheduleData(List<Point2D> points) {
        this(points, new Point2D.Double(0, 0));
    }

    public void clearRoute() {
        for (int i = 0; i < this.points.length; ++i)
            route[i] = i;
    }


    public void setRoute(int[] route) {
        this.route = route;
        this.cost = -1;
    }

    /**
     * checks construction constraints. (pairing, capacity, tw if present)
     *
     * @return
     */
    public boolean checkConstraints(int[] route) {
        Set<Integer> picked = new HashSet<Integer>();
        for (int p : route) {
            if (p < ordersNum)
                picked.add(p);
            else {
                if (!picked.contains(p - ordersNum)) {
                    System.out.println("pairing constraint not satisfied for: "
                            + (p - ordersNum) + " -> " + p);
                    return false;
                }
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


    public int getSize() {
        return points.length;
    }

    public double getCost() {
        if (cost < 0) {
            this.cost = getCost(route);
        }
        return this.cost;
    }

    public double getCost(int[] route) {
        double acc = 0.0;
        for (int i = 1; i < route.length; ++i) {
            acc += dist(route[i - 1], route[i]);
        }
        return acc;
    }

    public double dist(int s, int d) {
        return Math.sqrt((points[s].getX() - points[d].getX()) * (points[s].getX() - points[d].getX())
                + (points[s].getY() - points[d].getY()) * (points[s].getY() - points[d].getY()));
    }


    public void setCost(double cost) {
        this.cost = cost;
    }


    public Point2D[] getPoints() {
        return points;
    }

    public void setPoints(Point2D[] points) {
        this.points = points;
    }

    public Point2D getDepot() {
        return depot;
    }

    public void setDepot(Point2D depot) {
        this.depot = depot;
    }

    public List<Point2D> getRouteAsPoints() {
        List<Point2D> res = new ArrayList<Point2D>(route.length);

        for (int i : route) {
            res.add(points[route[i]]);
        }
        return res;
    }

    public int[] getRoute() {
        return route;
    }


}
