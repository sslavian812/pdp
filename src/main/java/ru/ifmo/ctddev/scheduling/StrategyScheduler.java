package ru.ifmo.ctddev.scheduling;

import ru.ifmo.ctddev.Config;

/**
 * This class is a scheduler, which uses a fixed strategy to choose an {@code SmallMove} for each step.
 * <p>
 * Created by viacheslav on 14.02.2016.
 */
public class StrategyScheduler implements Scheduler {


    private Strategy strategy;

    /**
     * The scheduler will perform {@code convergenceReserve * N^2} operations.
     * {@code 10 * N^2} by default
     */
    private int convergenceReserve = 10;

    public StrategyScheduler(Strategy strategy) {
        this.strategy = strategy;
    }


    /**
     * Creates a schedule for this {@code scheduleData}
     * <p>
     * Note: this method may take a long time.
     *
     * @param scheduleData
     * @return returns optimisation ratio: (initialCost - reachedCost) / initialCost
     */
    @Override
    public double schedule(ScheduleData scheduleData) {

        if (Config.showInfo) {
            System.out.println("initial state:");

            if (Config.showTrace)
                for (int i : scheduleData.getRoute())
                    System.out.print(i + " ");

            System.out.println("");
            System.out.println("constraint: " + scheduleData.checkConstraints());
            System.out.println("cost: " + scheduleData.getCost());
        }

        double initialCost = scheduleData.getCost();

        int t = convergenceReserve * scheduleData.getOrdersNum() * scheduleData.getOrdersNum();

        for (int i = 0; i < t ; ++i)
            performStep(scheduleData);

        if (Config.showInfo) {
            System.out.println();
            System.out.println("final state:");

            if (Config.showTrace)
                for (int i : scheduleData.getRoute())
                    System.out.print(i + " ");

            System.out.println("");
            System.out.println("constraint: " + scheduleData.checkConstraints());
            System.out.println("cost: " + scheduleData.getCost());
        }
        double reachedCost = scheduleData.getCost();

        if (Config.showInfo) {
            System.out.println("optimization ratio: " + (initialCost - reachedCost) / initialCost * 100 + "%");
            System.out.println();
        }

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
     * <p>
     * Node: if {@code show.trace} flag is enabled in {@code application.config},
     * this method produces lots of output.
     *
     * @param scheduleData data to be scheduled
     */
    private void performStep(ScheduleData scheduleData) {
        int[] r = strategy.getSmallMove().oneStep(scheduleData);

        // check and accept or reject:
        if (scheduleData.checkConstraints(r)) {
            if (Config.showTrace)
                System.out.println("constraints: OK");

            if (scheduleData.getCost(r) < scheduleData.getCost()) {
                if (Config.showTrace) {
                    System.out.println("initial: " + scheduleData.getCost() + "; reached: " + scheduleData.getCost(r));
                    System.out.println("ACCEPTED");
                }

                scheduleData.setRoute(r);
            } else {
                if (Config.showTrace)
                    System.out.println("rejected");
            }
        } else {
            if (Config.showTrace)
                System.out.println("constraints NO");
        }
    }

    public int getConvergenceReserve() {
        return convergenceReserve;
    }

    public void setConvergenceReserve(int convergenceReserve) {
        this.convergenceReserve = convergenceReserve;
    }
}
