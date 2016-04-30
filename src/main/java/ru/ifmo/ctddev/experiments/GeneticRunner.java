package ru.ifmo.ctddev.experiments;

import com.sun.org.apache.xpath.internal.SourceTree;
import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.StrategyProvider;
import ru.ifmo.ctddev.scheduling.genetics.GeneticStrategyScheduler;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Strategy;
import ru.ifmo.ctddev.scheduling.genetics.GeneticsSchedulerFctory;
import ru.ifmo.ctddev.scheduling.smallmoves.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by viacheslav on 21.02.2016.
 */
public class GeneticRunner {
    public static final int times = 100;
    public static final int size = 50;

    public static final int generationSize = 10;
    public static final int generations = 12 * size * size;


    public static void main(String[] args) {

        List<Strategy> strategies = StrategyProvider.provideAllStrategies();
        GeneticsSchedulerFctory factory = GeneticsSchedulerFctory.getInstance();

        List<GeneticStrategyScheduler> schedulers = new ArrayList<>();
        for (Strategy strategy : strategies) {
            schedulers.add(factory.getOnePluOneScheduler(strategy, generations));
            schedulers.add(factory.getOnePlusNScheduler(strategy, generations, (int) Math.sqrt(size / 2.0)));
            schedulers.add(factory.getOneCommaNScheduler(strategy, generations, (int) Math.sqrt(size / 2.0)));
            schedulers.add(factory.getBigMutationsScheduler(strategy, generations, (int) Math.sqrt(size / 2)));
            schedulers.add(factory.getKPlusKNScheduler(strategy, generations, (int) Math.sqrt(size / 2.0), (int) Math.sqrt(size / 4.0)));
        }


        int start = 0;
        List<ScheduleData> datasets = new ArrayList<>();

        while (start + size <= 8000) {
            datasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT,
                    "uniform8000.csv", "uniform8000_" + start + "_" + (start + size)));
            start += size;
        }

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 10, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        List<Future<List<Double>>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (Scheduler scheduler : schedulers) {

            long time = System.currentTimeMillis();
            List<List<Double>> ratios = new NDataSetsNTimesScheduleTester(scheduler, datasets, times).setExecutor(threadPoolExecutor).call();
            

            System.out.println(scheduler.getComment());
            System.out.println("Dataset: " + datasets.size() + ". Size of one dataset: " + size);
            System.out.println("# total time: " + (System.currentTimeMillis() - time) + " ms =~ " + (System.currentTimeMillis() - time) / 1000 + " s");

            List<Double> averagePerNTimes = new ArrayList<>();
            int i = 0;
            for (List<Double> list : ratios) {
                String indent = "    ";
                System.out.println(indent + "dataset: " + i + "-" + (i + size) + ": ");
                System.out.println(indent + "ratios=" + Arrays.toString(list.toArray()));
                averagePerNTimes.add(calcAverage(list));
                System.out.println(indent + "average ratio for dataset: "
                        + averagePerNTimes.get(averagePerNTimes.size() - 1));
                System.out.println();
            }

            System.out.println("==================================================");
            System.out.println();
        }


        System.out.println("time spent: " + (System.currentTimeMillis() - startTime) / 1000 + " s");
        threadPoolExecutor.shutdown();
    }

    private static Double calcAverage(List<Double> list) {
        double acc = 0;
        for (Double x : list) {
            acc += x;
        }
        acc /= list.size();
        return acc;
    }
}
