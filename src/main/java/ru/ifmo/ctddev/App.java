package ru.ifmo.ctddev;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        DatasetGenerator generator = new DatasetGeneratorImpl();
        List<Point2D> points = generator.generate(100, new Point2D.Double(50,50));
    }
}
