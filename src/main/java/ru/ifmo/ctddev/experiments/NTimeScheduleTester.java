package ru.ifmo.ctddev.experiments;

import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.StrategyScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Runs scheduler N times with specified strategy and the same dataset each time.
 * <p>
 * Created by viacheslav on 14.02.2016.
 */
public class NTimeScheduleTester implements Callable<List<Double>> {
    private Scheduler scheduler;
    private ScheduleData data;
    private int times;


    public NTimeScheduleTester(StrategyScheduler scheduler, ScheduleData data, int times) {
        this.scheduler = scheduler;
        this.data = data;
        this.times = times;
    }

    /**
     * Repeats {@code times} times a schedule for the same dataset and returns optimisation ratios.
     * @return return list of optimisation ratios.
     */
    @Override
    public List<Double> call() {
        List<Double> optimisationRatios = new ArrayList<>();

        for (int i = 0; i < times; ++i) {
            data.clearRoute();
            double ratio = scheduler.schedule(data);
            optimisationRatios.add(ratio);
        }
        return optimisationRatios;
    }

}
