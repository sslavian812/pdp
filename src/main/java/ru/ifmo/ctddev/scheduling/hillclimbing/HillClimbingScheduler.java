package ru.ifmo.ctddev.scheduling.hillclimbing;

import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.smallmoves.SmallMove;
import weka.core.pmml.Array;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by viacheslav on 09.06.2016.
 */
public class HillClimbingScheduler implements Scheduler, SmallMove {


    private int bound = 2;
    private int randoms = 1_000_000;

    static void shuffleArray(int[] ar, int s, int f) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = s; i < f; ++i) {
            int index = s + rnd.nextInt(f - s);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public static void main(String[] args) {
        int[] a = new int[]{1, 2, 3, 4, 5, 6, 1, 1, 1, 1};
        shuffleArray(a, 1, 6);
        System.out.println(Arrays.toString(a));
    }

    @Override
    public double schedule(ScheduleData scheduleData) {

        int frozen = 0;
        double initialConst = scheduleData.getCost();
//        System.out.println("initial: " + scheduleData.getCost());

        int bestCur[] = new int[scheduleData.getSize()];
        System.arraycopy(scheduleData.getRoute(), 0, bestCur, 0, scheduleData.getSize());
        for (int i = 0; i < randoms; ++i) {
            int[] cur = new int[scheduleData.getSize()];
            System.arraycopy(scheduleData.getRoute(), 0, cur, 0, scheduleData.getSize());

            shuffleArray(cur, 0, scheduleData.getOrdersNum());
            shuffleArray(cur, scheduleData.getOrdersNum(), scheduleData.getSize());

            if (!scheduleData.checkConstraints(cur)) {
                throw new RuntimeException("broken");
            }

            if (scheduleData.getCost(cur) < scheduleData.getCost(bestCur))
                bestCur = cur;

        }

        scheduleData.setRoute(bestCur);
//        System.out.println("random generated: " + scheduleData.getCost());




        while (true) {
            if (frozen > bound)
                break;

            int[] route = oneStep(scheduleData);

            if (scheduleData.getCost(route) < scheduleData.getCost()) {
                scheduleData.setRoute(route);
                frozen = 0;
            } else {
                frozen++;
            }
        }

//        System.out.println("reached: " + scheduleData.getCost());

        return (initialConst - scheduleData.getCost()) / initialConst;
    }

    @Override
    public String getComment() {
        return "Hill Climbing";
    }

    /**
     * Hill Climbing step. It might be very long, actually.
     *
     * @param data
     * @return
     */
    @Override
    public int[] oneStep(ScheduleData data) {
        ScheduleData tempData = data.clone();

        int[] cur = new int[tempData.getSize()];

        for (int i = 0; i < tempData.getOrdersNum(); ++i) {
            for (int j = i + 1; j < tempData.getOrdersNum(); ++j) {

                // copy
                System.arraycopy(tempData.getRoute(), 0, cur, 0, tempData.getSize());

                // swap
                int t = cur[i];
                cur[i] = cur[j];
                cur[j] = t;

                // step
                if (tempData.checkConstraints(cur)) {
                    if (tempData.getCost(cur) < tempData.getCost()) {
                        tempData.setRoute(cur);
                        cur = new int[tempData.getSize()];
                    }
                }
            }
        }
        return tempData.getRoute();
    }

}
