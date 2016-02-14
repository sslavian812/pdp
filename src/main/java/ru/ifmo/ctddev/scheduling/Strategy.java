package ru.ifmo.ctddev.scheduling;

import ru.ifmo.ctddev.scheduling.optimisers.Optimiser;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 14.02.2016.
 * <p>
 * This class represents an {@code optimisation strategy}.
 * Strategy is a probability distribution on {@code optimisers}.
 */
public class Strategy {

    private List<Optimiser> optimisers;
    private double[] probabilities;
    private double[] partialSums;

    private Random random;
    private String comment;

    /**
     * Initializes probabilities by default.
     *
     * @param optimisers
     */
    public Strategy(List<Optimiser> optimisers) {
        this.optimisers = optimisers;
        this.probabilities = new double[optimisers.size()];
        this.partialSums = new double[optimisers.size()];
        for (int i = 0; i < optimisers.size(); ++i) {
            probabilities[i] = 1.0 / optimisers.size();
            partialSums[i] = probabilities[i];
            if (i > 0)
                partialSums[i] += partialSums[i - 1];
        }
        random = new Random();
    }

    public Strategy(Optimiser optimiser) {
        this(Arrays.asList(optimiser));
    }

    /**
     * Creates Strategy with specified optimisers and probabilities.
     *
     * @param optimisers
     * @param probabilities
     */
    public Strategy(List<Optimiser> optimisers, double[] probabilities) {
        this.optimisers = optimisers;
        this.probabilities = probabilities;
        this.partialSums = new double[optimisers.size()];
        for (int i = 0; i < optimisers.size(); ++i) {
            partialSums[i] = probabilities[i];
            if (i > 0)
                partialSums[i] += partialSums[i - 1];
        }
    }

    public void setSeed(long seed) {
        random.setSeed(seed);
    }


    /**
     * Provides and optimizer according to the strategy.
     *
     * @return on optimiser according to the strategy
     */
    public Optimiser getOptimiser() {
        double p = random.nextDouble();
        for (int i = 0; i < partialSums.length; ++i) {
            if (p < partialSums[i])
                return optimisers.get(i);
        }
        return optimisers.get(optimisers.size() - 1);
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Strategy{\n"
                + (comment == null ? "" : "comment=" + comment)
                + "    optimisers={" + optimisers.stream()
                .map(optimiser -> optimiser.toString())
                .collect(Collectors.joining(", "))
                + ",\n    probabilities=" + Arrays.toString(probabilities)
                + "\n}";
    }
}
