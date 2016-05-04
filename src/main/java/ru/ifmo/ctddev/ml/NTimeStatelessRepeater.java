package ru.ifmo.ctddev.ml;

import ru.ifmo.ctddev.scheduling.ConcurrentStrategyScheduler;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.strategies.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by viacheslav on 02.05.2016.
 */
public class NTimeStatelessRepeater implements Callable<List<Double>> {

    private ConcurrentStrategyScheduler scheduler;
    private ScheduleData data;
    private Strategy strategy;
    private int times;
    private int steps;


    public NTimeStatelessRepeater(ConcurrentStrategyScheduler scheduler, Strategy strategy,
                                  ScheduleData data, int times, int steps) {
        this.scheduler = scheduler;
        this.data = data;
        this.strategy = strategy;
        this.times = times;
        this.steps = steps;
    }

    /**
     * Repeats {@code times} times a schedule for the same original and returns optimisation ratios.
     *
     * @return return list of optimisation ratios.
     */
    @Override
    public List<Double> call() {
        List<Double> optimisationRatios = new ArrayList<>();

        for (int i = 0; i < times; ++i) {
            data.clearRoute();
            double ratio = scheduler.schedule(data, strategy, steps);
            optimisationRatios.add(ratio);
        }
        return optimisationRatios;
    }
}

