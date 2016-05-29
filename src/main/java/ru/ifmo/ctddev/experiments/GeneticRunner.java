package ru.ifmo.ctddev.experiments;

import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.features.Moments;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.genetics.GeneticStrategyScheduler;
import ru.ifmo.ctddev.scheduling.genetics.GeneticsSchedulerFactory;
import ru.ifmo.ctddev.scheduling.strategies.Strategy;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static util.Util.calcAverage;

/**
 * Created by viacheslav on 21.02.2016.
 */
public class GeneticRunner {
    public static final int times = 20;
    public static final int size = 50;
    public static final int from = 0;

    public static final int n_datasets = 10;
    public static final int generations = 12 * size * size;


    public static void main(String[] args) {

        List<Strategy> strategies = StrategyProvider.provideAllStrategies();
        GeneticsSchedulerFactory factory = GeneticsSchedulerFactory.getInstance();

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

        System.out.println("taxi8000.csv: \n\n");
        while (from + start + size <= from + (size * n_datasets)) {
            datasets.add(DatasetProvider.getDataset(size, from + start, DatasetProvider.Direction.RIGHT,
                    "taxi8000.csv", null));
            start += size;
        }

//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 10, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
//        List<Future<List<Double>>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        NDataSetsNTimesScheduleTester nDataSetsNTimesScheduleTester = new NDataSetsNTimesScheduleTester(null, datasets, times);

        Moments moments = new Moments();

        for (Scheduler scheduler : schedulers) {

            long time = System.currentTimeMillis();
            List<List<Double>> ratios = nDataSetsNTimesScheduleTester.setScheduler(scheduler).call();


            System.out.println(scheduler.getComment());
            System.out.println("Datasets: " + datasets.size() + ". Size of one dataset: " + size);
            System.out.println("# total time: " + (System.currentTimeMillis() - time) + " ms =~ " + (System.currentTimeMillis() - time) / 1000 + " s");

            List<Double> averagePerNTimes = new ArrayList<>();
            int i = 0;
            for (List<Double> list : ratios) {
                String indent = "    ";
                System.out.println(indent + "dataset: " + (from  +i) + "-" + (from + i + size) + ": ");
                i+=size;
                System.out.println(indent + "ratios=" + Arrays.toString(list.toArray()));
                averagePerNTimes.add(calcAverage(list));
                System.out.println(indent + "average ratio for i-th dataset: "
                        + averagePerNTimes.get(averagePerNTimes.size() - 1));
                System.out.println(indent + moments.extractStatisticalFeatures(list).get(3).toCSVString());
                System.out.println();
            }

            System.out.println("average for scheduler: " + calcAverage(averagePerNTimes));

            System.out.println("==================================================");
            System.out.println();
        }


        System.out.println("time spent: " + (System.currentTimeMillis() - startTime) / 1000 + " s");
//        threadPoolExecutor.shutdown();
    }
}
