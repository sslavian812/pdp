package ru.ifmo.ctddev.scheduling.steps;

import ru.ifmo.ctddev.scheduling.ScheduleData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslav on 05.12.2015.
 */
public class RelocateCouple implements Optimiser {
    public int[] oneStep(ScheduleData data) {
        int n = data.getOrdersNum();
        int k = (int) (Math.random() * (n));

        List<Integer> route = new ArrayList<Integer>(data.getSize());
        for (int e : data.getRoute()) {
            route.add(e);
        }

        int bestk = 0;
        int bestnk = 1;
        double bestCost = Double.MAX_VALUE;

        for (int i = 0; i < route.size() - 1; ++i) {
            for (int j = i + 1; j < route.size(); ++j) {
                route.remove(new Integer(k));
                route.remove(new Integer(n + k));
                route.add(i, k);
                route.add(j, n + k);
                double curCost = data.getCost(route);
                if (curCost < bestCost) {
                    bestCost = curCost;
                    bestk = i;
                    bestnk = j;
                }
            }
        }

        route.remove(new Integer(k));
        route.remove(new Integer(n + k));

        route.add(bestk, k);
        route.add(bestnk, n + k);
        int[] array = new int[route.size()];
        for (int i = 0; i < route.size(); i++) array[i] = route.get(i);

        return array;
    }

    public String getName() {
        return "RelocateCouple";
    }
}
