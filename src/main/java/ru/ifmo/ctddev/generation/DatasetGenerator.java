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
     *@param rightDown @return
     */
    List<Point2D> generate(int N, Point2D center, Point2D leftUp, Point2D rightDown);
}
