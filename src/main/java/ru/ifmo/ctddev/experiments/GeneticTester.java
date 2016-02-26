package ru.ifmo.ctddev.experiments;

import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.scheduling.genetics.GeneticStrategyScheduler;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Strategy;
import ru.ifmo.ctddev.scheduling.smallmoves.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by viacheslav on 21.02.2016.
 */
public class GeneticTester {
    public static final int times = 100;
    public static final int size = 50;
    public static final int start = 0;

    public static final int generationSize = 10;
    public static final int generations = 12* size* size;


    public static void main(String[] args) {

        List<SmallMove> all = new ArrayList<>(5);
        all.add(new Lin2opt());
        all.add(new CoupleExchange());
        all.add(new DoubleBridge());
        all.add(new PointExchange());
        all.add(new RelocateBlock());

        List<SmallMove> two = new ArrayList<>(5);
        two.add(new Lin2opt());
        two.add(new PointExchange());


        List<Strategy> strategies = new ArrayList<>();
        strategies.add(new Strategy(new Lin2opt()));
        strategies.add(new Strategy(new CoupleExchange()));
        strategies.add(new Strategy(new DoubleBridge()));
        strategies.add(new Strategy(new PointExchange()));
        strategies.add(new Strategy(two));
        strategies.add(new Strategy(all));
        strategies.add(new Strategy(new RelocateBlock()));



        List<ScheduleData> datasets = new ArrayList<>();
        datasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT, null));
        datasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT, null));
        datasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT, null));
        datasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT, null));
        datasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT, null));
        datasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT, null));
        datasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT, null));


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 10, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100));
        List<Future<List<Double>>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < strategies.size(); ++i) {
            futures.add(threadPoolExecutor.submit(
                    new NTimeScheduleTester(new GeneticStrategyScheduler(strategies.get(i)), datasets.get(i), times)
            ));
        }

        System.out.println(GeneticTester.class.getName());

        for (int i = 0; i < futures.size(); ++i) {
            try {
                List<Double> ratios = futures.get(i).get();
                System.out.println(strategies.get(i));
                System.out.println("size: " + size);
                System.out.println(Arrays.toString(ratios.toArray()));
                System.out.println("================");
                System.out.println();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println("time spent: " + (System.currentTimeMillis()-startTime) /1000 + " s");

        threadPoolExecutor.shutdown();

    }
}
