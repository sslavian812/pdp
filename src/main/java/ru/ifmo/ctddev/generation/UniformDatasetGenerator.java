package ru.ifmo.ctddev.generation;

import cern.jet.random.Uniform;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

/**
 * Created by viacheslav on 16.12.2015.
 */
public class UniformDatasetGenerator implements DatasetGenerator {

    @Deprecated
    public List<Point2D.Double> generate(int N, Point2D center, Point2D leftUp, Point2D rightDown) {
        return null;
    }

    public List<Point2D.Double> generate(Point2D leftUp, Point2D rightDown, List<Integer> sizes, List<Point2D> centers) {
        int total = sizes.get(0);
        Point2D.Double[] points = new Point2D.Double[2*total];


        for(int cluster=0; cluster<sizes.size(); ++cluster) {
            int n = sizes.get(cluster);
            Point2D center = centers.get(cluster);

            RandomEngine engine = new DRand((int)System.currentTimeMillis()/1000);
            double dx = rightDown.getX() - leftUp.getX();
            double dy = leftUp.getY() - rightDown.getY();
            Uniform uniformX = new Uniform(leftUp.getX(), rightDown.getX(), engine);
            Uniform uniformY = new Uniform(rightDown.getY(), leftUp.getY(), engine);


            for (int i = 0; i < n; ++i) {
                double x = uniformX.nextDouble();
                double y = uniformY.nextDouble();
                x = x % dx;
                y = y % dy;
                points[i] = new Point2D.Double(x, y); // pick up point

                x = uniformX.nextDouble();
                y = uniformY.nextDouble();
                x = x % dx;
                y = y % dy;
                points[n + i] = new Point2D.Double(x, y); // delivery point
            }
        }
        return Arrays.asList(points);
    }

    public String getName() {
        return "Uniform";
    }
}
