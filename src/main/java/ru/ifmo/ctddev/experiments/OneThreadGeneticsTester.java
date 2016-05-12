package ru.ifmo.ctddev.experiments;

import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.strategies.Strategy;
import ru.ifmo.ctddev.scheduling.strategies.ConstantStrategy;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;
import ru.ifmo.ctddev.scheduling.genetics.GeneticStrategyScheduler;
import ru.ifmo.ctddev.scheduling.genetics.GeneticsSchedulerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by viacheslav on 26.02.2016.
 */
public class OneThreadGeneticsTester {
    public static final int times = 1;
    public static final int size = 50;
    public static final int start = 0;


    public static void main(String[] args) {


        List<Strategy> strategies = StrategyProvider.provideAllStrategies();

        GeneticsSchedulerFactory factory = GeneticsSchedulerFactory.getInstance();
        ScheduleData scheduleData = DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT,
                "gaussian8000.csv", null);
        int size = scheduleData.getOrdersNum();
        int generations = 12 * size * size;

        List<GeneticStrategyScheduler> schedulers = new ArrayList<>();
        for (Strategy strategy : strategies) {
            schedulers.add(factory.getOnePluOneScheduler(strategy, generations));
            schedulers.add(factory.getOnePlusNScheduler(strategy, generations, (int) Math.sqrt(size / 2.0)));
            schedulers.add(factory.getOneCommaNScheduler(strategy, generations, (int) Math.sqrt(size / 2.0)));
            schedulers.add(factory.getBigMutationsScheduler(strategy, generations, (int) Math.sqrt(size / 2)));
            schedulers.add(factory.getKPlusKNScheduler(strategy, generations, (int) Math.sqrt(size / 2.0), (int) Math.sqrt(size / 4.0)));
        }
//        for (Strategy strategy: strategies)

        long startTime = System.currentTimeMillis();

//        for (Scheduler scheduler : schedulers) {
//
//            long time = System.currentTimeMillis();
//            List<Double> ratios = new NTimeScheduleTester(scheduler, scheduleData.clone(), times).call();
//            long averagePerRun = (long) (((double) System.currentTimeMillis() - time) / ratios.size());
//            System.out.println(scheduler.toString());
//            System.out.println("averagePerRun: " + averagePerRun + " ms == " + averagePerRun/1000 + " s");
//            System.out.println("total time: " + (System.currentTimeMillis() - time) + " ms == " + (System.currentTimeMillis() - time)/1000 + " s");
//            System.out.println("ratios:");
//            System.out.println(Arrays.toString(ratios.toArray()));
//            System.out.println("#================#");
//            System.out.println();
//        }
//


        for (Scheduler scheduler : schedulers) {

            long time = System.currentTimeMillis();
            List<Double> ratios = new NTimeScheduleTester(scheduler, scheduleData.clone(), times).call();
            long averagePerRun = (long) (((double) System.currentTimeMillis() - time) / ratios.size());
            System.out.println(scheduler.getComment());
            System.out.println(times + "x times");
            System.out.println("# averagePerRun: " + averagePerRun + " ms =~ " + averagePerRun/1000 + " s");
            System.out.println("# total time: " + (System.currentTimeMillis() - time) + " ms =~ " + (System.currentTimeMillis() - time)/1000 + " s");
            System.out.println("ratios=" + Arrays.toString(ratios.toArray()));

//            System.out.println(((GeneticStrategyScheduler)scheduler).getJuliaHist(5, 50, averagePerRun));
            System.out.println("#================#");
            System.out.println();
        }

//        String res = schedulers.stream().map(o -> o.getJulaiCells(5, 50)).collect(Collectors.joining(",\n"));
//        System.out.println(res);
        System.out.println("Total time spent: " + (System.currentTimeMillis() - startTime) / 1000 + " s");
    }
}