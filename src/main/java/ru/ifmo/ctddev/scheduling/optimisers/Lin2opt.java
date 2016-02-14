package ru.ifmo.ctddev.scheduling.optimisers;

import ru.ifmo.ctddev.scheduling.ScheduleData;

/**
 * Created by viacheslav on 05.12.2015.
 */
public class Lin2opt implements Optimiser {
    public int[] oneStep(ScheduleData data) {
        // 2 randoms:  j+1 < k
        int k = (int) (Math.random() * (data.getSize() - 2) + 1);
        int j = (int) (Math.random() * (k - 2) + 1);


        // perform swap:
//        System.out.println("swap: " + j + " and " + k);
        int[] r = new int[data.getSize()];
        System.arraycopy(data.getRoute(), 0, r, 0, data.getSize());

        int t = r[j + 1];
        r[j + 1] = r[k];
        r[k] = t;

        // perform reverse:
        for (int i = 1; i < k - (j + 1) - 1; ++i) {
            int src = (j + 1) + i;
            int dst = k - i;
            if (src >= dst)
                break;
//            System.out.println("swap: " + ((j + 1) + i) + " and " + (k - i));
            int st = r[src];
            r[src] = r[dst];
            r[dst] = st;
        }

        return r;
    }

    public String getName() {
        return "Lin2Opt";
    }
}
