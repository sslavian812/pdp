package ru.ifmo.ctddev.gui;

import ru.ifmo.ctddev.generate.DatasetGenerator;
import ru.ifmo.ctddev.generate.GausianDatasetGeneratorImpl;
import ru.ifmo.ctddev.scheduling.DecartScheduleData;
import ru.ifmo.ctddev.scheduling.smallmoves.*;

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
        List<Point2D.Double> points = generator.generate(100, new Point2D.Double(50, 50),
                new Point2D.Double(0, 100), new Point2D.Double(100, 0));


//        System.out.println("============================================");
//        System.out.println("Lin2Opt");
//        SchedulerTemp scheduler = new SchedulerTemp(new Lin2opt());
//        scheduler.schedule(new DecartScheduleData(points));
//
//        System.out.println("============================================");
//        System.out.println("Relocate-couple");
//        scheduler.setSmallMove(new RelocateCouple());
//        scheduler.schedule(new DecartScheduleData(points));
//
//        System.out.println("============================================");
//        System.out.println("Double-bridge");
//        scheduler.setSmallMove(new DoubleBridge());
//        scheduler.schedule(new DecartScheduleData(points));
//
//        System.out.println("============================================");
//        System.out.println("Point-exchange");
//        scheduler.setSmallMove(new PointExchange());
//        scheduler.schedule(new DecartScheduleData(points));
//
//        System.out.println("============================================");
//        System.out.println("Couple-exchange");
//        scheduler.setSmallMove(new CoupleExchange());
//        scheduler.schedule(new DecartScheduleData(points));
//
//        System.out.println("============================================");
//        System.out.println("Relocate-Block");
//        scheduler.setSmallMove(new RelocateBlock());
//        scheduler.schedule(new DecartScheduleData(points));

    }
}
