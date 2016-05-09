package ru.ifmo.ctddev.experiments;

import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.ml.AnswerBuilder;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.strategies.SmartL2OandRBStrategy;
import ru.ifmo.ctddev.scheduling.strategies.Strategy;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;
import ru.ifmo.ctddev.scheduling.StrategyScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by viacheslav on 14.02.2016.
 */
public class StrategyRunner {

    public static final int times = 10;
    public static final int size = 50;
    public static int start = 0;
    public static final int n_datasets = 5;
    public static final boolean shuffled = false;

    public static void main(String[] args) {
//        List<Strategy> strategies = StrategyProvider.provideAllStrategies();

        SmartL2OandRBStrategy strategy = (SmartL2OandRBStrategy) StrategyProvider.getSmartL2ORBStrategy();
        List<ScheduleData> uDatasets = new ArrayList<>();
        List<ScheduleData> nDatasets = new ArrayList<>();
        List<ScheduleData> tDatasets = new ArrayList<>();
        while (start + size <= size * n_datasets) {
            uDatasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT,
                    "uniform8000.csv", null));
            nDatasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT,
                    "gaussian8000.csv", null));
            tDatasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT,
                    "taxi8000.csv", null));

            start += size;
        }

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 10, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100));
        List<Future<List<Double>>> uFutures = new ArrayList<>();
        List<Future<List<Double>>> nFutures = new ArrayList<>();
        List<Future<List<Double>>> tFutures = new ArrayList<>();

        long startTime = System.currentTimeMillis();


        for (int i = 0; i < uDatasets.size(); ++i) {
            uFutures.add(threadPoolExecutor.submit(
                    new NTimeScheduleTester(new StrategyScheduler(strategy.clone()), uDatasets.get(i), times)
            ));
            nFutures.add(threadPoolExecutor.submit(
                    new NTimeScheduleTester(new StrategyScheduler(strategy.clone()), nDatasets.get(i), times)
            ));
            tFutures.add(threadPoolExecutor.submit(
                    new NTimeScheduleTester(new StrategyScheduler(strategy.clone()), tDatasets.get(i), times)
            ));
        }

        System.out.println(strategy.getDisplayName());

        processFutures(uFutures, "uniform");
        processFutures(nFutures, "gaussian");
        processFutures(tFutures, "taxi");


        System.out.println("time spent: " + (System.currentTimeMillis() - startTime) / 1000 + " s");

        threadPoolExecutor.shutdown();

    }

    private static void processFutures(List<Future<List<Double>>> futures, String prefix) {
        System.out.println("processing: " + prefix);
        List<Double> avearagesPerDataset = new ArrayList<>();
        for (Future<List<Double>> future : futures) {
            try {
                List<Double> ratios = future.get();
                System.out.println(Arrays.toString(ratios.toArray()));
                avearagesPerDataset.add(AnswerBuilder.calcAverage(ratios));

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println();
        System.out.println("average per all datasets: " + AnswerBuilder.calcAverage(avearagesPerDataset));

        System.out.println("================");
    }
}
