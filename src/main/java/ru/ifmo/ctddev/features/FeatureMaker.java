package ru.ifmo.ctddev.features;

import ru.ifmo.ctddev.scheduling.ScheduleData;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class extracts features from original, packed in {@link ScheduleData} object.
 * Created by viacheslav on 14.02.2016.
 * <p>
 * Features:
 * width = maxX-minX
 * height = maxY-minY
 * meanX
 * meanY
 */
public class FeatureMaker {

    private Moments momentsCalculator;

    public FeatureMaker() {
        momentsCalculator = new Moments();
    }

    public List<Feature> getFeatures(ScheduleData scheduleData) {
        List<Double> xs = Arrays.asList(scheduleData.getPoints()).stream().map(p -> p.getX()).collect(Collectors.toList());
        List<Double> ys = Arrays.asList(scheduleData.getPoints()).stream().map(p -> p.getY()).collect(Collectors.toList());
        List<Feature> features = momentsCalculator.extractStatisticalFeatures(xs, "xs_");
        features.addAll(momentsCalculator.extractStatisticalFeatures(ys, "ys_"));

        features.add(new Feature(
                "n", xs.size(), "amount of points in range"
        ));
        return features;
    }

    public List<Feature> getMomentsForAllXs(ScheduleData scheduleData) {
        List<Double> xs = Arrays.asList(scheduleData.getPoints()).stream().map(p -> p.getX()).collect(Collectors.toList());
        return momentsCalculator.extractStatisticalFeatures(xs, "all_xs_");
    }

    public List<Feature> getMomentsForAllYs(ScheduleData scheduleData) {
        List<Double> xs = Arrays.asList(scheduleData.getPoints()).stream().map(p -> p.getY()).collect(Collectors.toList());
        return momentsCalculator.extractStatisticalFeatures(xs, "all_ys_");
    }

    public List<Feature> getMomentsForSrcXs(ScheduleData scheduleData) {
        List<Double> xs = Arrays.asList(scheduleData.getPoints()).stream().map(p -> p.getX()).collect(Collectors.toList());
        return momentsCalculator.extractStatisticalFeatures(xs, "src_xs_");
    }
}
