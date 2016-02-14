package ru.ifmo.ctddev.features;

import ru.ifmo.ctddev.scheduling.ScheduleData;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class extracts features from dataset, packed in {@link ScheduleData} object.
 * Created by viacheslav on 14.02.2016.
 *
 * Features:
 * width = maxX-minX
 * height = maxY-minY
 * meanX
 * meanY
 *
 */
public class FeatureMaker {

    public List<Feature> getFeatures(ScheduleData scheduleData) {
        Moments moments = new Moments();
        List<Double> xs = Arrays.asList(scheduleData.getPoints()).stream().map(p -> p.getX()).collect(Collectors.toList());
        List<Double> ys = Arrays.asList(scheduleData.getPoints()).stream().map(p -> p.getY()).collect(Collectors.toList());
        List<Feature> features = moments.extractStatisticalFeatures(xs, "xs");
        features.addAll(moments.extractStatisticalFeatures(ys, "ys"));

        features.add(new Feature(
                "n", xs.size(), "amount of points in range"
        ));
        return features;
    }
}
