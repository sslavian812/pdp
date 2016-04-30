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

    protected List<SmallMove> smallMoves;
    protected double[] probabilities;
    protected double[] partialSums;

    private Random random;
    private String comment;

    /**
     * Initializes probabilities by default.
     *
     * @param smallMoves
     */
    public Strategy(List<SmallMove> smallMoves) {
        this.smallMoves = smallMoves;
        this.random = new Random();
        this.comment = smallMoves.stream().map(s -> s.toString()).collect(Collectors.joining(","));
        this.probabilities = new double[smallMoves.size()];
        this.partialSums = new double[smallMoves.size()];
        for (int i = 0; i < smallMoves.size(); ++i) {
            probabilities[i] = 1.0 / smallMoves.size();
            partialSums[i] = probabilities[i];
            if (i > 0)
                partialSums[i] += partialSums[i - 1];
        }
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
        this.random = new Random();
        this.comment = smallMoves.stream().map(s -> s.toString()).collect(Collectors.joining(","));
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
     * Provides an SmallMove according to the strategy.
     *
     * @return SmallMove according to the strategy
     */
    public SmallMove getSmallMove() {
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


    /**
     * This method gets called, when the strategy gets a reward.
     * For future active learning.
     *
     * @param reward
     */
    public void receiveReward(double reward) {
        return;
    }

    @Override
    public String toString() {
        return "Strategy{" + System.lineSeparator()
                + (comment == null ? "" : "    comment=" + comment + System.lineSeparator())
                + "    smallMoves={" + smallMoves.stream()
                .map(SmallMove::toString)
                .collect(Collectors.joining(", ")) + "," + System.lineSeparator()
                + "    probabilities=" + Arrays.toString(probabilities) + System.lineSeparator()
                + "}";
    }

    public String toString(String indent) {
        return "Strategy{" + System.lineSeparator()
                + (comment == null ? "" : indent + "    comment=" + comment + System.lineSeparator())
                + indent + "    smallMoves=[" + smallMoves.stream().map(SmallMove::toString)
                .collect(Collectors.joining(", ")) + "]," + System.lineSeparator()
                + indent + "    probabilities=" + Arrays.toString(probabilities) + System.lineSeparator()
                + indent + "}";
    }

    public String getComment() {
        return comment == null ? "" : comment + " - " + Arrays.toString(probabilities);
    }
}
