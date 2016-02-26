package ru.ifmo.ctddev.experiments;

import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.Strategy;
import ru.ifmo.ctddev.scheduling.genetics.GeneticStrategyScheduler;
import ru.ifmo.ctddev.scheduling.genetics.GeneticsSchedulerFctory;
import ru.ifmo.ctddev.scheduling.smallmoves.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by viacheslav on 26.02.2016.
 */
public class OneThreadGeneticsTester {
    public static final int times = 1;
    public static final int size = 20;
    public static final int start = 0;


    public static void main(String[] args) {

        List<SmallMove> all = new ArrayList<>(5);
        all.add(new Lin2opt());
        all.add(new CoupleExchange());
        all.add(new DoubleBridge());
        all.add(new PointExchange());


        List<Strategy> strategies = new ArrayList<>();
        strategies.add(new Strategy(new Lin2opt()));
        strategies.add(new Strategy(new CoupleExchange()));
        strategies.add(new Strategy(new DoubleBridge()));
        strategies.add(new Strategy(new PointExchange()));
        strategies.add(new Strategy(all));

        GeneticsSchedulerFctory factory = GeneticsSchedulerFctory.getInstance();
        ScheduleData scheduleData = DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT, null);
        int size = scheduleData.getSize();
        int generations = size * size * 12;

        List<GeneticStrategyScheduler> schedulers = new ArrayList<>();
        for (Strategy strategy : strategies) {
            schedulers.add(factory.getOnePluOneScheduler(strategy, generations));
            schedulers.add(factory.getOnePlusNScheduler(strategy, generations, size / 2));
            schedulers.add(factory.getOneCommaNScheduler(strategy, generations, size / 2));
            schedulers.add(factory.getKCommaKNScheduler(strategy, generations, size / 2, size / 4));
            schedulers.add(factory.getBigMutationsScheduler(strategy, generations, size / 2));
        }

        long startTime = System.currentTimeMillis();

        for (Scheduler scheduler : schedulers) {
            long time = System.currentTimeMillis();
            List<Double> ratios = new NTimeScheduleTester(scheduler, scheduleData.clone(), times).call();
            long averagePerRun = (long) (((double) System.currentTimeMillis() - time) / ratios.size());
            System.out.println(scheduler.toString());
            System.out.println("averagePerRun: " + averagePerRun/1000 + " s");
            System.out.println("ratios:");
            System.out.println(Arrays.toString(ratios.toArray()));
            System.out.println("================");
            System.out.println();
        }

        System.out.println("Total time spent: " + (System.currentTimeMillis() - startTime) / 1000 + " s");
    }
}