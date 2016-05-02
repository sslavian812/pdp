package ru.ifmo.ctddev.scheduling;

import ru.ifmo.ctddev.Config;

/**
 * This class is a lightweight stateless version of StrategyScheduler.
 * Created by viacheslav on 02.05.2016.
 */
public class ConcurrentStrategyScheduler {

    /**
     * Creates a schedule for this {@code scheduleData}
     * <p>
     * Note: this method may take a long time.
     *
     * @param scheduleData
     * @param strategy
     * @param steps
     * @return returns optimisation ratio: (initialCost - reachedCost) / initialCost
     */
    public double schedule(ScheduleData scheduleData, Strategy strategy, int steps) {

        double initialCost = scheduleData.getCost();

        for (int i = 0; i < steps; ++i)
            performStep(scheduleData, strategy);

        double reachedCost = scheduleData.getCost();

        return (initialCost - reachedCost) / initialCost;
    }


    /**
     * Performs one optimisation step (one small-move);
     * Is constructed route is acceptable, new route is set to {@code scheduleData}.
     * Otherwise, {@code scheduleData} will be left unchanged.
     * <p>
     * @param scheduleData data to be scheduled
     * @param strategy
     */
    private void performStep(ScheduleData scheduleData, Strategy strategy) {
        int[] r = strategy.getSmallMove().oneStep(scheduleData);

        // check and accept or reject:
        if (scheduleData.checkConstraints(r)) {
            if (scheduleData.getCost(r) < scheduleData.getCost()) {
                scheduleData.setRoute(r);
            }
        }
    }
}
