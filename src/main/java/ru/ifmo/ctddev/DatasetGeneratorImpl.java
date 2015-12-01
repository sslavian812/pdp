package ru.ifmo.ctddev;

import cern.jet.random.Normal;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

/**
 * Created by viacheslav on 01.12.2015.
 */
public class DatasetGeneratorImpl implements DatasetGenerator {
    public List<Point2D> generate(int N, Point2D center) {
        Point2D[] points = new Point2D[2*N];
        RandomEngine engine = new DRand();
        Normal normalX = new Normal(center.getX(), SIZE / 8, engine);
        Normal normalY = new Normal(center.getY(), SIZE / 8, engine);

        System.out.println((-50) % 100);

        for (int i = 0; i < N; ++i) {
            double x = normalX.nextDouble();
            double y = normalY.nextDouble();
            x = x % SIZE;
            y = y % SIZE;
            points[i] = new Point2D.Double(x,y); // pick up point

            x = normalX.nextDouble();
            y = normalY.nextDouble();
            x = x % SIZE;
            y = y % SIZE;
            points[N+i] = new Point2D.Double(x,y); // delivery point
        }
        return Arrays.asList(points);
    }
}
