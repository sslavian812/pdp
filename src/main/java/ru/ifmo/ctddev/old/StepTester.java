package ru.ifmo.ctddev.old;

import ru.ifmo.ctddev.generation.DatasetGenerator;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.old.SchedulerTemp;
import ru.ifmo.ctddev.scheduling.optimisers.Optimiser;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by viacheslav on 16.12.2015.
 */
public class StepTester {

    private Optimiser optimiser;
    private DatasetGenerator generator;

    private Point2D leftUp;
    private Point2D rightDown;

    private List<Point2D> centers;
    private List<Integer> sizes;

    private int times;

    public StepTester(DatasetGenerator generator, Optimiser optimiser) {
        this.generator = generator;
        this.optimiser = optimiser;
        this.leftUp = new Point2D.Double(0, 100);
        this.rightDown = new Point2D.Double(100, 0);
        this.centers = Arrays.asList(new Point2D[]{new Point2D.Double(50, 50)});
        this.sizes = Arrays.asList(100);
        this.times = 100;
    }


    public void setTimes(int times) {
        this.times = times;
    }

    public List<Double> run() {

        List<Double> res = new ArrayList<Double>(10);
        for (int i = 0; i < times; ++i) {
            List<Point2D.Double> points = generator.generate(leftUp, rightDown, sizes, centers);
            SchedulerTemp scheduler = new SchedulerTemp(optimiser);
            double ratio = scheduler.schedule(new ScheduleData(points));
            res.add(ratio);
        }
        String[] ss = new String[res.size()];
        for (int i = 0; i < res.size(); ++i)
            ss[i] = "" + res.get(i);


        System.out.println();
        System.out.println("figure");
        System.out.println("hist([" + String.join(" ", ss) + "], 25)");
        System.out.println("xlabel('" + optimiser.toString() + ":  " + generator.getName() + "', 'FontSize', 30);");
        System.out.println();
        return res;
    }
}
