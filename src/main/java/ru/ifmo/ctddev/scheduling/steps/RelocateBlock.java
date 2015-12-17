package ru.ifmo.ctddev.scheduling.steps;

import ru.ifmo.ctddev.scheduling.ScheduleData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslav on 05.12.2015.
 */
public class RelocateBlock implements Optimiser {
    public int[] oneStep(ScheduleData data) {
        int n = data.getOrdersNum();
        int s = (int) (Math.random() * (n));

        int pickS = (int) (Math.random() * (n-1));
        int dropS = pickS + (int) (Math.random() *  (n - pickS-1) + 1);

        int[] r = data.getRoute();


        List<Integer> route = new ArrayList<Integer>(data.getSize()); // without block;
        List<Integer> block = new ArrayList<Integer>(dropS - pickS + 1); // block
        for (int i = 0; i < r.length; ++i) {
            if (i >= pickS && i <= dropS)
                block.add(r[i]);
            else
                route.add(r[i]);
        }

        int bestPos = 0;
        double bestCost = Double.MAX_VALUE;

        for (int i = 0; i < route.size() - 1; ++i) {
            route.addAll(i, block);
            double curCost = data.getCost(route);
            if (curCost < bestCost) {
                bestCost = curCost;
                bestPos = i;
            }
            route.removeAll(block);
        }

        route.addAll(bestPos, block);
        int[] array = new int[route.size()];
        for (int i = 0; i < route.size(); i++) array[i] = route.get(i);

        return array;
    }

    public String getName() {
        return "RelocateBlock";
    }
}
