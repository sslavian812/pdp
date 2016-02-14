package ru.ifmo.ctddev.datasets;

import com.jhlabs.map.proj.MercatorProjection;

import java.awt.geom.Point2D;

/**
 * Provides some useful utilities.
 * Created by viacheslav on 14.02.2016.
 */
public class Util {

    public static final double scale = 1000;

    /**
     * Converts geo-coordinates (latitude, longitude) coordinates to (x,y)
     * using Mercator projection from JMapProjLib
     *
     * @param coordinate geo-coordinate in (latitude, longitude)
     * @return (x, y)-coordinates of this point according to Mercator projection
     * @see <a href="https://github.com/OSUCartography/JMapProjLib">https://github.com/OSUCartography/JMapProjLib</a>
     */
    public static Point2D.Double convertLatLonToXY(Point2D.Double coordinate) {

        MercatorProjection projection = new MercatorProjection();

        double latitude = coordinate.getX();
        double longitude = coordinate.getY();

        // convert to radian
        latitude = latitude * Math.PI / 180;
        longitude = longitude * Math.PI / 180;

        Point2D.Double d = projection.project(longitude, latitude, new Point2D.Double());

        // scaling to make the map bigger
        double magnifiedX = d.x * scale;
        double magnifiedY = d.y * scale;

        return new Point2D.Double(magnifiedX, magnifiedY);
    }
}

