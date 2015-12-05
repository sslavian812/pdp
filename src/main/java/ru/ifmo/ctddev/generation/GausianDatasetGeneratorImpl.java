package ru.ifmo.ctddev.generation;

import cern.jet.random.Normal;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;
import ru.ifmo.ctddev.generation.DatasetGenerator;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

/**
 * Created by viacheslav on 01.12.2015.
 */
public class GausianDatasetGeneratorImpl implements DatasetGenerator {

    public List<Point2D> generate(int N, Point2D center, Point2D leftUp, Point2D rightDown) {

        Point2D[] points = new Point2D[2*N];
        RandomEngine engine = new DRand();
        double dx = rightDown.getX() - leftUp.getX();
        double dy = leftUp.getY() - rightDown.getY();
        Normal normalX = new Normal(center.getX(), dx / 8, engine);
        Normal normalY = new Normal(center.getY(), dy / 8, engine);


        for (int i = 0; i < N; ++i) {
            double x = normalX.nextDouble();
            double y = normalY.nextDouble();
            x = x % dx;
            y = y % dy;
            points[i] = new Point2D.Double(x,y); // pick up point

            x = normalX.nextDouble();
            y = normalY.nextDouble();
            x = x % dx;
            y = y % dy;
            points[N+i] = new Point2D.Double(x,y); // delivery point
        }
        return Arrays.asList(points);
    }
}
