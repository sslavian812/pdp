package ru.ifmo.ctddev.scheduling.smallmoves;

import ru.ifmo.ctddev.scheduling.ScheduleData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslav on 05.12.2015.
 */
public class CoupleExchange implements SmallMove {
    public int[] oneStep(ScheduleData data) {
        int n = data.getOrdersNum();
        int i = (int) (Math.random() * (n - 3)); // max = n-4
        int j = (int) (Math.random() * (n - 1)); // max = n-2
        if (j == i)
            j = j + 2;

        List<Integer> route = new ArrayList<Integer>(data.getSize());
        for (int e : data.getRoute()) {
            route.add(e);
        }

        int pi = route.indexOf(i);
        int pni = route.indexOf(-n - i);
        int pj = route.indexOf(j);
        int pnj = route.indexOf(-n - j);

        int t, tn;
        t = route.get(pi);
        tn = route.get(pni);
        route.set(pi, route.get(pj));
        route.set(pni, route.get(pnj));
        route.set(pj, t);
        route.set(pnj, tn);

        int[] array = new int[route.size()];
        for (int g = 0; g < route.size(); g++)
            array[g] = route.get(g);
        return array;
    }

    public String toString() {
        return "CoupleExchange";
    }
}
