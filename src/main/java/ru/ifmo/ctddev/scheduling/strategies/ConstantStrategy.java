package ru.ifmo.ctddev.scheduling.strategies;

import ru.ifmo.ctddev.scheduling.smallmoves.SmallMove;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;

import static ru.ifmo.ctddev.scheduling.strategies.StrategyProvider.getAllSmallMoves;

/**
 * Created by viacheslav on 14.02.2016.
 * <p>
 * This class represents an {@code optimisation strategy}.
 * Strategy is a probability distribution on {@code smallMoves}.
 */
public class ConstantStrategy implements Strategy {

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
    public ConstantStrategy(List<SmallMove> smallMoves) {
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


    public ConstantStrategy(SmallMove smallMove) {
        this(Arrays.asList(smallMove));
    }

    /**
     * Creates Strategy with specified smallMoves and probabilities.
     *
     * @param smallMoves
     * @param probabilities
     */
    public ConstantStrategy(List<SmallMove> smallMoves, double[] probabilities) {
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
    @Override
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

    @Override
    public String getComment() {
        return comment == null ? "" : comment + " - " + Arrays.toString(probabilities);
    }

    @Override
    public String getDisplayName() {
        Map<String, Double> map = new HashMap<>();

        for (int i = 0; i < smallMoves.size(); ++i) {
            map.put(smallMoves.get(i).toString(), probabilities[i]);
        }

        StringBuilder stringBuilder = new StringBuilder("[");

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#0.000", otherSymbols);

        stringBuilder.append(String.join(",", getAllSmallMoves().stream().map(smallMove -> {
            if (map.containsKey(smallMove.toString()))
                return df.format(map.get(smallMove.toString()));
            else
                return "" + 0.0;

        }).collect(Collectors.toList())));


        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public void setDisplayName(String displayName) {
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

    public double[] getProbabilities() {
        return probabilities;
    }

//    @Override
    public List<SmallMove> getSmallMoves() {
        return smallMoves;
    }




}
