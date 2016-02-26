package ru.ifmo.ctddev.scheduling;

import ru.ifmo.ctddev.scheduling.smallmoves.SmallMove;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 14.02.2016.
 * <p>
 * This class represents an {@code optimisation strategy}.
 * Strategy is a probability distribution on {@code smallMoves}.
 */
public class Strategy {

    private List<SmallMove> smallMoves;
    private double[] probabilities;
    private double[] partialSums;

    private Random random;
    private String comment;

    /**
     * Initializes probabilities by default.
     *
     * @param smallMoves
     */
    public Strategy(List<SmallMove> smallMoves) {
        this.smallMoves = smallMoves;
        this.probabilities = new double[smallMoves.size()];
        this.partialSums = new double[smallMoves.size()];
        for (int i = 0; i < smallMoves.size(); ++i) {
            probabilities[i] = 1.0 / smallMoves.size();
            partialSums[i] = probabilities[i];
            if (i > 0)
                partialSums[i] += partialSums[i - 1];
        }
        random = new Random();
    }

    public Strategy(SmallMove smallMove) {
        this(Arrays.asList(smallMove));
    }

    /**
     * Creates Strategy with specified smallMoves and probabilities.
     *
     * @param smallMoves
     * @param probabilities
     */
    public Strategy(List<SmallMove> smallMoves, double[] probabilities) {
        this.smallMoves = smallMoves;
        this.probabilities = probabilities;
        this.partialSums = new double[smallMoves.size()];
        for (int i = 0; i < smallMoves.size(); ++i) {
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
    public SmallMove getOptimiser() {
        double p = random.nextDouble();
        for (int i = 0; i < partialSums.length; ++i) {
            if (p < partialSums[i])
                return smallMoves.get(i);
        }
        return smallMoves.get(smallMoves.size() - 1);
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Strategy{\n"
                + (comment == null ? "" : "comment=" + comment)
                + "    smallmoves={" + smallMoves.stream()
                .map(smallMove -> smallMove.toString())
                .collect(Collectors.joining(", "))
                + ",\n    probabilities=" + Arrays.toString(probabilities)
                + "\n}";
    }
}
