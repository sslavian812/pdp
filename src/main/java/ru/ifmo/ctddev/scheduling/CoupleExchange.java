package ru.ifmo.ctddev.scheduling;

/**
 * Created by viacheslav on 05.12.2015.
 */
public class CoupleExchange implements Optimiser {
    public int[] oneStep(ScheduleData data) {
        int n = data.ordersNum;
        int i = (int) (Math.random() * (n));
        int j = (int) (Math.random() * (n - 1));
        if (j == i)
            ++j;

        // perform swap:
//        System.out.println("swap: " + i + " and " + j);
//        System.out.println("swap: " + n+i + " and " + n+j);
        int[] r = new int[data.getSize()];
        System.arraycopy(data.getRoute(), 0, r, 0, data.getSize());

        int t = r[i];
        r[i] = r[j];
        r[j] = t;

        t = r[n + i];
        r[n + i] = r[n + j];
        r[n + j] = t;

        return r;
    }
}
