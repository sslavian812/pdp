package ru.ifmo.ctddev.generation;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Created by viacheslav on 01.12.2015.
 */
public interface DatasetGenerator {
    /**
     * generates points in rectangle leftUP -> rightDown by distribution
     * @param N
     * @param center
     * @param leftUp
     * @param rightDown
     * @return
     */
    @Deprecated
    List<Point2D.Double> generate(int N, Point2D center, Point2D leftUp, Point2D rightDown);

    List<Point2D.Double> generate(Point2D leftUp, Point2D rightDown, List<Integer> sizes, List<Point2D> centers);

    String getName();
}
