package ru.ifmo.ctddev.gui;

import ru.ifmo.ctddev.generation.DatasetGenerator;
import ru.ifmo.ctddev.generation.GausianDatasetGeneratorImpl;
import ru.ifmo.ctddev.scheduling.optimisers.*;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Hello world!
 */
public class App {


    static void shuffleArray(int[] ar) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public static void main(String[] args) {
        DatasetGenerator generator = new GausianDatasetGeneratorImpl();
        List<Point2D> points = generator.generate(100, new Point2D.Double(50, 50),
                new Point2D.Double(0, 100), new Point2D.Double(100, 0));


        System.out.println("============================================");
        System.out.println("Lin2Opt");
        Scheduler scheduler = new Scheduler(new Lin2opt());
        scheduler.schedule(new ScheduleData(points));

        System.out.println("============================================");
        System.out.println("Relocate-couple");
        scheduler.setOptimiser(new RelocateCouple());
        scheduler.schedule(new ScheduleData(points));

        System.out.println("============================================");
        System.out.println("Double-bridge");
        scheduler.setOptimiser(new DoubleBridge());
        scheduler.schedule(new ScheduleData(points));

        System.out.println("============================================");
        System.out.println("Point-exchange");
        scheduler.setOptimiser(new PointExchange());
        scheduler.schedule(new ScheduleData(points));

        System.out.println("============================================");
        System.out.println("Couple-exchange");
        scheduler.setOptimiser(new CoupleExchange());
        scheduler.schedule(new ScheduleData(points));

        System.out.println("============================================");
        System.out.println("Relocate-Block");
        scheduler.setOptimiser(new RelocateBlock());
        scheduler.schedule(new ScheduleData(points));

    }
}
