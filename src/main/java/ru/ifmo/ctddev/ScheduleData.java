package ru.ifmo.ctddev;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Created by viacheslav on 01.12.2015.
 */
public class ScheduleData {
    private List<Point2D> points;
    private Point2D depot;
    private List<Point2D> route;
    private double cost;


    public ScheduleData(List<Point2D> points) {
        this(points, new Point2D.Double(0, 0));
    }

    public ScheduleData(List<Point2D> points, Point2D depot) {
        this.points = points;
        this.depot = depot;
    }


    /**
     * checks construction constraints. (pairing, capacity, tw if present)
     *
     * @return
     */
    public boolean checkConstraints() {
        // todo
        return false;
    }


    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


    public List<Point2D> getPoints() {
        return points;
    }

    public void setPoints(List<Point2D> points) {
        this.points = points;
    }

    public Point2D getDepot() {
        return depot;
    }

    public void setDepot(Point2D depot) {
        this.depot = depot;
    }

    public List<Point2D> getRoute() {
        return route;
    }

    public void setRoute(List<Point2D> route) {
        this.route = route;
    }
}
