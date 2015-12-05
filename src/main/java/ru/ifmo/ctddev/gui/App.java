package ru.ifmo.ctddev.gui;

import ru.ifmo.ctddev.generation.DatasetGenerator;
import ru.ifmo.ctddev.generation.GausianDatasetGeneratorImpl;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        DatasetGenerator generator = new GausianDatasetGeneratorImpl();
        List<Point2D> points = generator.generate(100, new Point2D.Double(50,50),
                new Point2D.Double(0,100), new Point2D.Double(100,0) );
        ScheduleData data = new ScheduleData(points);
        Scheduler scheduler = new Scheduler();
        scheduler.runSceduler(data);
    }
}
