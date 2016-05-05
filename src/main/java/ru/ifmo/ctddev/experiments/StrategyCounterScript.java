package ru.ifmo.ctddev.experiments;

import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.scheduling.ConcurrentStrategyScheduler;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.strategies.StatefulStrategy;
import ru.ifmo.ctddev.scheduling.strategies.Strategy;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * For one time use
 * Created by viacheslav on 05.05.2016.
 */
public class StrategyCounterScript {

    public static final int times = 100;
    public static final int size = 50;
    public static int start = 0;
    public static final int n_datasets = 20;
    public static final boolean shuffled = false;

    public static void main(String[] args) {
        List<Strategy> strategies = StrategyProvider.provideStatefulStrategies();

        List<ScheduleData> datasets = new ArrayList<>();
        while (start + size <= size * n_datasets) {
            datasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT,
                    "uniform8000.csv", null));
            datasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT,
                    "gaussian8000.csv", null));
            datasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT,
                    "taxi8000.csv", null));

            start += size;
        }

        long startTime = System.currentTimeMillis();


        List<Integer> perStrategyCalled = new ArrayList<>(strategies.size());
        List<Integer> perStrategySucceed = new ArrayList<>(strategies.size());

        ConcurrentStrategyScheduler concurrentStrategyScheduler = new ConcurrentStrategyScheduler();

        for (int i = 0; i < strategies.size(); ++i) {
            System.out.println("strategy: " + strategies.get(i).getDisplayName());
            List<Integer> succeeds = new ArrayList<>();
            List<Integer> called = new ArrayList<>();

            for (ScheduleData scheduleData : datasets) {
                ((StatefulStrategy) strategies.get(i)).trim(); // drops numbers
                concurrentStrategyScheduler.schedule(scheduleData.clone(), strategies.get(i),
                        12 * scheduleData.getOrdersNum() * scheduleData.getOrdersNum());

                succeeds.add(((StatefulStrategy) strategies.get(i)).getSucceed());
                called.add(((StatefulStrategy) strategies.get(i)).getCalled());

                System.out.println(((StatefulStrategy) strategies.get(i)).getSucceed());
            }

            perStrategyCalled.add(calcAverage(called));
            perStrategySucceed.add(calcAverage(succeeds));
        }


        for (int i = 0; i < strategies.size(); ++i) {
            System.out.println(strategies.get(i).getDisplayName()
                    + " -  succeed: " + perStrategySucceed.get(i)
                    + " called: " + perStrategyCalled.get(i)
                    + " ratio: " + ((double) perStrategySucceed.get(1)) / ((double) perStrategyCalled.get(i)));
        }
        System.out.println("time spent: " + (System.currentTimeMillis() - startTime) / 1000 + " s");

    }

    private static int calcAverage(List<Integer> list) {
        long acc = 0;
        for (int x : list) {
            acc += x;
        }
        acc /= list.size();
        return (int) acc;
    }
}
