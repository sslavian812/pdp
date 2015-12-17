package ru.ifmo.ctddev.generation;

import cern.jet.random.Normal;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;
import ru.ifmo.ctddev.generation.DatasetGenerator;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;

/**
 * Created by viacheslav on 01.12.2015.
 */
public class GausianDatasetGeneratorImpl implements DatasetGenerator {

    @Deprecated
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

    public List<Point2D> generate(Point2D leftUp, Point2D rightDown, List<Integer> sizes, List<Point2D> centers) {

        BinaryOperator operator = new BinaryOperator<Integer>() {
            public Integer apply(Integer a, Integer b) {
                return a + b;
            }
        };

        int total = sizes.stream().reduce(0, operator);
        Point2D[] points = new Point2D[2*total];


        int lastPos = 0;

        for(int cluster=0; cluster<sizes.size(); ++cluster) {
            int n = sizes.get(cluster);
            Point2D center = centers.get(cluster);

            RandomEngine engine = new DRand((int)System.currentTimeMillis()/1000); // fixed seed!!!
            double dx = rightDown.getX() - leftUp.getX();
            double dy = leftUp.getY() - rightDown.getY();
            Normal normalX = new Normal(center.getX(), dx / 8, engine);
            Normal normalY = new Normal(center.getY(), dy / 8, engine);


            for (int i = 0; i < n; ++i) {
                double x = normalX.nextDouble();
                double y = normalY.nextDouble();
                x = x % dx;
                y = y % dy;
                points[lastPos + i] = new Point2D.Double(x, y); // pick up point

                x = normalX.nextDouble();
                y = normalY.nextDouble();
                x = x % dx;
                y = y % dy;
                points[lastPos + n + i] = new Point2D.Double(x, y); // delivery point
            }

            lastPos += n;
        }
            return Arrays.asList(points);
    }

    public String getName() {
        return "gaussian";
    }
}
