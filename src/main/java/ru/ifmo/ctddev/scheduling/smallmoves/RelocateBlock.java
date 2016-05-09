package ru.ifmo.ctddev.scheduling.smallmoves;

import ru.ifmo.ctddev.scheduling.ScheduleData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslav on 05.12.2015.
 */
public class RelocateBlock implements SmallMove {
    public int[] oneStep(ScheduleData data) {
        int n = data.getOrdersNum();

//        int pickS = (int) (Math.random() * (n-1));
//        int dropS = pickS + (int) (Math.random() *  (n - pickS-1) + 1);

        int s = (int) (Math.random() * (2*n -1));
        int f = (int) (Math.random() * (2*n ));
        if(s == f)
            ++f;

        int[] r = data.getRoute();


        List<Integer> route = new ArrayList<Integer>(data.getSize()); // without block;
        List<Integer> block = new ArrayList<Integer>(data.getSize()); // block
        for (int i = 0; i < r.length; ++i) {
            if (i >= s && i < f)
                block.add(r[i]);
            else
                route.add(r[i]);
        }

        int bestPos = 0;
//        double bestCost = Double.MAX_VALUE; // todo was initially
        double bestCost = data.getCost(); // todo beter effort?

        for (int i = 0; i < route.size(); ++i) {
            route.addAll(i, block);
            double curCost = data.getCost(route);
            if (curCost <= bestCost) {
                bestCost = curCost;
                bestPos = i;
            }
            route.removeAll(block);
        }

        route.addAll(bestPos, block);
        int[] array = new int[route.size()];
        for (int i = 0; i < route.size(); i++)
            array[i] = route.get(i);

        return array;
    }

    public String toString() {
        return "RelocateBlock";
    }
}
