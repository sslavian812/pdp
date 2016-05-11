package util;

import java.util.List;

/**
 * Created by viacheslav on 11.05.2016.
 */
public class Util {
    public static Double calcAverage(List<Double> list) {
        double acc = 0;
        for (Double x : list) {
            acc += x;
        }
        acc /= list.size();
        return acc;
    }
}
