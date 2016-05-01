package ru.ifmo.ctddev.experiments;

import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Strategy;
import ru.ifmo.ctddev.scheduling.StrategyProvider;
import ru.ifmo.ctddev.scheduling.StrategyScheduler;
import ru.ifmo.ctddev.scheduling.smallmoves.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by viacheslav on 14.02.2016.
 */
public class StrategyRunner {

    public static final int times = 100;
    public static final int size = 50;
    public static int start = 0;
    public static final int n_datasets = 5;
    public static final boolean shuffled = false;

    public static void main(String[] args) {
        List<Strategy> strategies = StrategyProvider.provideAllStrategies();

        List<ScheduleData> datasets = new ArrayList<>();
        while (start + size < size * n_datasets) {
            datasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT,
                    "uniform8000.csv", null));
            start +=size;
        }

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4, 10, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100));
        List<Future<List<Double>>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < strategies.size(); ++i) {
            futures.add(threadPoolExecutor.submit(
                    new NTimeScheduleTester(new StrategyScheduler(strategies.get(i)), datasets.get(i), times)
            ));
        }

        for (int i = 0; i < futures.size(); ++i) {
            try {
                List<Double> ratios = futures.get(i).get();
                System.out.println(strategies.get(i));
                System.out.println(Arrays.toString(ratios.toArray()));
                System.out.println("================");
                System.out.println();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println("time spent: " + (System.currentTimeMillis() - startTime) / 1000 + " s");

        threadPoolExecutor.shutdown();

    }
}
