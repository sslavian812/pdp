package ru.ifmo.ctddev.scheduling.optimisers;

import ru.ifmo.ctddev.scheduling.ScheduleData;

import java.util.Arrays;

/**
 * Created by viacheslav on 05.12.2015.
 */
public class DoubleBridge implements Optimiser {


    public int[] oneStep(ScheduleData data) {
        // 4
        int[] indices = new int[4];
        for (int i = 0; i < 4; ++i) {
            indices[i] = (int) (Math.random() * (data.getSize() - 1));
        }

        Arrays.sort(indices);
        int j = indices[0];
        int k = indices[1];
        int l = indices[2];
        int h = indices[3];


        // perform swaps:
        int[] r = new int[data.getSize()];
        System.arraycopy(data.getRoute(), 0, r, 0, data.getSize());

        // j+1 -> l+1
        int t = r[j + 1];
        r[j + 1] = r[l + 1];
        r[l + 1] = t;

        // k+1 -> h+1
        t = r[k + 1];
        r[k + 1] = r[h + 1];
        r[h + 1] = t;

        // l+1 -> j+1
        t = r[l + 1];
        r[l + 1] = r[j + 1];
        r[j + 1] = t;

        // h+1 -> k+1
        t = r[h + 1];
        r[h + 1] = r[k + 1];
        r[k + 1] = t;

        return r;
    }

    public String getName() {
        return "DoubleBridge";
    }
}
