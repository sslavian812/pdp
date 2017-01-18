package ru.ifmo.ctddev.scheduling.annealing;

import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.smallmoves.SmallMove;
import ru.ifmo.ctddev.scheduling.strategies.Strategy;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by viacheslav on 09.06.2016.
 */
public class AnnealingScheduler implements Scheduler, SmallMove {


    private double T0 = 100;
    private double bound = 1000;
    private double alpha = 0.9999;

    private Random random = ThreadLocalRandom.current();

    private Strategy strategy = null;

    public AnnealingScheduler setStrategy(Strategy strategy) {
        this.strategy = strategy;
        return this;
    }

    @Override
    public double schedule(ScheduleData scheduleData) {
        double T = T0;
        int frozen = 0;

        double initialConst = scheduleData.getCost();


        while (true) {
            if (frozen > bound)
                break;

            int[] route;

            if (strategy == null) {
                route = oneStep(scheduleData);
            } else {
                route = strategy.getSmallMove().oneStep(scheduleData);
            }


            double c0 = scheduleData.getCost();
            double c1 = scheduleData.getCost(route);

            if (c1 < c0) {
                scheduleData.setRoute(route);
                frozen = 0;
                T = alpha * T;
            } else {
                frozen++;
                if (random.nextDouble() < Math.exp(-(c1 - c0) / T)) {
                    scheduleData.setRoute(route);
                }
            }
        }

//        System.out.println("reached: " + scheduleData.getCost());

        return (initialConst - scheduleData.getCost()) / initialConst;
    }

    @Override
    public String getComment() {
        return "Annealing: [" + T0 + "-> Tx" + alpha + "], [" + bound + "] "
                + (strategy != null ? " & strategy.getDisplayName()" : "");
    }


    @Override
    public int[] oneStep(ScheduleData data) {

        int[] cur = new int[data.getSize()];
        System.arraycopy(data.getRoute(), 0, cur, 0, data.getSize());

        int i = random.nextInt(cur.length);
        int j = random.nextInt(cur.length);

        // swap
        int t = cur[i];
        cur[i] = cur[j];
        cur[j] = t;

        // step
        if (data.checkConstraints(cur)) {
            return cur;
        } else {
            return data.getRoute();
        }
    }
}

