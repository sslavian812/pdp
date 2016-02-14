package ru.ifmo.ctddev;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.ifmo.ctddev.scheduling.Strategy;
import ru.ifmo.ctddev.scheduling.optimisers.*;

import java.util.Arrays;

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
}
