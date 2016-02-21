package ru.ifmo.ctddev;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.datasets.Util;
import ru.ifmo.ctddev.features.Feature;
import ru.ifmo.ctddev.features.FeatureMaker;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Strategy;
import ru.ifmo.ctddev.scheduling.optimisers.*;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }


    public void testToString() {
        Strategy strategy = new Strategy(
                Arrays.asList(
                        new CoupleExchange(),
                        new DoubleBridge(),
                        new Lin2opt(),
                        new PointExchange(),
                        new RelocateBlock(),
                        new RelocateCouple()
                )
        );

        System.out.println(strategy.toString());
        Assert.assertNotNull(strategy.toString());
    }

    public void testResourcesAvailability() {
        System.out.println(Config.maxCapacity);
        Assert.assertNotNull(Config.maxCapacity);
        Assert.assertTrue(Config.maxCapacity > 0);
    }

    public void testMercatorProjection() {
        Point2D.Double point = new Point2D.Double(51.698768, -0.183759);
//        Point2D.Double point = new Point2D.Double(1.0,1.0);
        Point2D.Double projected = Util.convertLatLonToXY(point);
        Assert.assertNotNull(projected);
        System.out.println(projected.getX() + ", " + projected.getY());
    }

    public void testDatasetProvider() {
        ScheduleData data = DatasetProvider.getDataset(10, 0, DatasetProvider.Direction.RIGHT, null);
        Assert.assertNotNull(data);
        Assert.assertEquals(20, data.getPoints().length);
        System.out.println(Arrays.toString(data.getRoute()));
    }

    public void testFeatures() {
        ScheduleData data = DatasetProvider.getDataset(10, 0, DatasetProvider.Direction.RIGHT, null);
        FeatureMaker featureMaker = new FeatureMaker();
        List<Feature> features = featureMaker.getFeatures(data);
        Assert.assertNotNull(features);
        Assert.assertEquals(15, features.size());
        features.stream().forEach(f -> System.out.println(f.toString()));
    }
}
