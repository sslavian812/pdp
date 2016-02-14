package ru.ifmo.ctddev.features;

import ru.ifmo.ctddev.Config;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * This class implements logic to extract statistical features from a range of numbers.
 *
 * Created by viacheslav on 14.02.2016.
 */
public class Moments {


    /**
     * Extracts statistical features.
     *
     * @param doubles numbers whick from features should be extracted.
     * @return extracted statistical features.
     */
    public List<Feature> extractStatisticalFeatures(List<Double> doubles) {
        return extractStatisticalFeatures(doubles, "");
    }


    /**
     * Extracts statistical features.
     *
     * @param doubles numbers whick from features should be extracted.
     * @param prefix  prefix, which should be appended to feature name, if needed.
     * @return extracted statistical features.
     */
    public List<Feature> extractStatisticalFeatures(List<Double> doubles, String prefix) {

        /**
         * the median value of numbers range.
         */
        double median = 0.0;

        /**
         * EX. The average value of numbers in range. mean = sum / n
         */
        double mean = 0.0; // The first raw moment

        /**
         * average difference between the observed value and mean of values
         */
        double averageDeviation = 0.0;

        /**
         * A measure that is used to quantify the amount of variation
         * or dispersion of a set of data values. A standard deviation
         * close to 0 indicates that the data points tend
         * to be very close to the mean
         */
        double standardDeviation = 0.0;


        /**
         * Ex2: The second central moment.
         * Variance measures how far a set of numbers are spread out.
         * A variance of zero indicates that all the values are identical.
         * Variance is always non-negative: a small variance indicates that
         * the data points tend to be very close to the mean.
         */
        double variance = 0.0;

        /**
         *  Ex3: The third central moment.
         *  It is a measure of the lopsidedness of the distribution;
         *  Any symmetric distribution will have a third central moment of zero.
         *  A distribution that is skewed to the right (the tail of the
         *  distribution is longer on the right), will have a positive skewness.
         */
        double skew = 0.0;


        /**
         * Ex4: The fourth central moment is a measure of the heaviness
         * of the tail of the distribution, compared to the normal distribution
         * of the same variance. Since it is the expectation of a fourth power,
         * the fourth central moment, where defined, is always positive;
         */
        double kurtosis = 0.0;


        double sum = 0;
        for (Double d : doubles) {
            sum += d;
        }
        int n = doubles.size();
        mean = sum / n;

        for (int i = 0; i < n; i++) {
            double deviation = doubles.get(i) - mean;

            averageDeviation += Math.abs(deviation);
            variance += Math.pow(deviation, 2);
            skew += Math.pow(deviation, 3);
            kurtosis += Math.pow(deviation, 4);
        }
        averageDeviation /= n;
        variance /= (n - 1);
        standardDeviation = Math.sqrt(variance);

        if (variance != 0.0) {
            skew /= (n * variance * standardDeviation);
            kurtosis = kurtosis / (n * variance * variance) - 3.0;
        }

        Collections.sort(doubles);

        int mid = (n / 2);
        median = (n % 2 != 0) ?
                doubles.get(mid) :
                (doubles.get(mid) + doubles.get(mid - 1)) / 2;

        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(6);
        nf.setMinimumFractionDigits(6);

        if (Config.showTrace) {
            System.out.println("n:                  " + n);
            System.out.println("median:             " + nf.format(median));
            System.out.println("mean:               " + nf.format(mean));
            System.out.println("average_deviation:  " + nf.format(averageDeviation));
            System.out.println("standard_deviation: " + nf.format(standardDeviation));
            System.out.println("variance:           " + nf.format(variance));
            System.out.println("skew:               " + nf.format(skew));
            System.out.println("kurtosis:           " + nf.format(kurtosis));
        }

        List<Feature> features = new ArrayList<>(7);
        features.add(new Feature(
                prefix + "median", median, "the median value of numbers range"
        ));
        features.add(new Feature(
                prefix + "mean", mean, "Ex. The average value of numbers in range. mean = sum / n"
        ));
        features.add(new Feature(
                prefix + "average_deviation", averageDeviation,
                "average difference between the observed value and mean of values"
        ));
        features.add(new Feature(
                prefix + "standatd_deviation", standardDeviation,
                "the amount of dispersion"
        ));
        features.add(new Feature(
                prefix + "variance", variance, "Ex2. measures, how far a set of numbers are spread out"
        ));
        features.add(new Feature(
                prefix + "skew", skew, "Ex3. measure of the lopsidedness of the distribution"
        ));
        features.add(new Feature(
                prefix + "kurtosis", kurtosis,
                "Ex4. measure of the heaviness of the tail of the distribution, compared to the normal distribution"
        ));

        return features;
    }
}
