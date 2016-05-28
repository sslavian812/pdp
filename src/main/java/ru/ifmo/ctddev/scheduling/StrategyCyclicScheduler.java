package ru.ifmo.ctddev.scheduling;

import ru.ifmo.ctddev.Config;
import ru.ifmo.ctddev.scheduling.strategies.Strategy;

/**
 * This class is a scheduler, which uses a fixed strategy to choose an {@code SmallMove} for each step.
 * <p>
 * Created by viacheslav on 21.05.2016.
 */
public class StrategyCyclicScheduler implements Scheduler {

    private Strategy strategy;

    /**
     * The scheduler will perform {@code convergenceReserve * N^2} operations.
     * {@code 10 * N^2} by default
     */
    private int convergenceReserve = 12;

    public StrategyCyclicScheduler(Strategy strategy) {
        this.strategy = strategy;
    }


    /**
     * Changes provided {@code scheduleData} to be optimized.
     * <p>
     * Note: this method may take a long time.
     *
     * @param scheduleData
     * @return returns optimisation ratio: (initialCost - reachedCost) / initialCost
     */
    @Override
    public double schedule(ScheduleData scheduleData) {
        double initialCost = scheduleData.getCycleCost();

        int t = convergenceReserve * scheduleData.getOrdersNum() * scheduleData.getOrdersNum();

        for (int i = 0; i < t ; ++i)
            performStep(scheduleData);

        double reachedCost = scheduleData.getCycleCost();


        return (initialCost - reachedCost) / initialCost;
    }

    @Override
    public String getComment() {
        return strategy.getComment();
    }


    /**
     * Performs one optimisation step (one small-move);
     * Is constructed route is acceptable, new route is set to {@code scheduleData}.
     * Otherwise, {@code scheduleData} will be left unchanged.
     *
     * @param scheduleData data to be scheduled
     */
    private void performStep(ScheduleData scheduleData) {

        int[] r = strategy.getSmallMove().oneStep(scheduleData);

        // check and accept or reject:
        if (scheduleData.checkConstraints(r)) {
            if (scheduleData.getCycleCost(r) < scheduleData.getCycleCost()) {
                scheduleData.setRoute(r);
                strategy.receiveReward(1.0);
            } else {
                strategy.receiveReward(-1.0);
            }
        } else {
            strategy.receiveReward(0.0);
        }
    }
}

