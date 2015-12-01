package ru.ifmo.ctddev;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Created by viacheslav on 01.12.2015.
 */
public interface DatasetGenerator {
    public static double SIZE = 100.0;

    /**
     * generates points in 100x100 by gaussian
     * @param N
     * @param center
     * @return
     */
    List<Point2D> generate(int N, Point2D center);

}
