package ru.ifmo.ctddev.scheduling.genetics;

import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.strategies.SmartL2OandRBStrategy;
import ru.ifmo.ctddev.scheduling.strategies.Strategy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 29.05.2016.
 */
public class GeneticsCycleScheduler implements Scheduler {

    private String comment;

    // internals:
    private Strategy strategy;
    private ScheduleData originalScheduleData;
    private List<ScheduleData> currentGeneration;


    //parameters:
    private int K; // generation - size of generation
    private int G; // number of generations, which will suffer bug mutations.
    private int N; // number of new created individuals at point of reproduction
    private int F; // fit function calls

    /**
     * if true, new population will be chosen only from children.
     * if false, new population will be chosen from parents and children.
     */
    private boolean onlyChildren;
    private boolean isBigMutationAllowed;

    private double initialCost;

    // for graphics
    private BufferedWriter logFile = null;
    private List<String> xs = new ArrayList<>();
    private List<String> ys = new ArrayList<>();

    public GeneticsCycleScheduler(String comment, Strategy strategy,
                                           int k, int n, int f, int g,
                                           boolean onlyChildren, boolean isBigMutationAllowed) {
        this.strategy = strategy;
        this.comment = comment;
        K = k;
        N = n;
        F = f;
        G = g;
        this.onlyChildren = onlyChildren;
        this.isBigMutationAllowed = isBigMutationAllowed;
    }


    /**
     * Creates a schedule for this {@code scheduleData} in genetic way.
     * <p>
     * Note: this method may take a long time.
     *
     * @param scheduleData
     * @return returns optimisation ratio: (initialCost - reachedCost) / initialCost
     */
    @Override
    public double schedule(ScheduleData scheduleData) {

        originalScheduleData = scheduleData;
        initialCost = scheduleData.getCost();
        currentGeneration = new ArrayList<>();
        scheduleData.trimFitFunctionCalls();

        if (strategy instanceof SmartL2OandRBStrategy)
            ((SmartL2OandRBStrategy) strategy).trim();

        // form first generation:
        for (int i = 0; i < K; ++i) {
            currentGeneration.add(scheduleData.clone());
        }

        int i=0;
        for (i = 0; ; ++i) {
            if (isBigMutationAllowed && (i == G / 4 || i == G / 2 || i == 3.0 / 4.0 * (double) G)) {
                currentGeneration = bigMutations(currentGeneration, (int) Math.sqrt(scheduleData.getSize() / 2));
            }
            List<ScheduleData> children = reproduction(currentGeneration);
            List<ScheduleData> mutated = mutation(children);

            if (!onlyChildren) {
                mutated.addAll(currentGeneration);
            }

            currentGeneration = selection(mutated);

            if (originalScheduleData.getFitFunctionCallsCount() >= F)
                break;

        }
        System.err.println(getComment() + " - Stopped on " + i + "-th generation");

        ScheduleData winner = currentGeneration.get(0);
        originalScheduleData.setRoute(winner.getRoute()); // changes original data

        return (initialCost - winner.getCost()) / initialCost;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment + " " + strategy.getDisplayName();
    }


    private List<ScheduleData> bigMutations(List<ScheduleData> generation, int amount) {
        return generation.stream().map(individual -> {
            for (int i = 0; i < amount; ++i)
                individual = mutate(individual);
            return individual;
        }).collect(Collectors.toList());
    }

    private ScheduleData mutate(ScheduleData individual) {
        int[] route = strategy.getSmallMove().oneStep(individual);

//        double prev = individual.getCost();

        if (individual.checkConstraints(route)) {
            individual.setRoute(route);
        }

//        if (prev > individual.getCost())
//            strategy.receiveReward(1.0);
//        else if (prev < individual.getCost())
//            strategy.receiveReward(-1.0);
//        else
//            strategy.receiveReward(0);

        return individual;
    }


    private List<ScheduleData> selection(List<ScheduleData> generation) {
        List<ScheduleData> list = new ArrayList<>(G);
        Collections.sort(generation);
        list.addAll(generation.subList(0, K));
        return list;
    }


    /**
     * Emulates mutation process.
     *
     * @param generation
     * @return
     */
    private List<ScheduleData> mutation(List<ScheduleData> generation) {
        return generation.stream()
                .map(individual -> mutate(individual))
                .collect(Collectors.toList());
    }

    /**
     * Emulates reproduction process.
     * Each individual from generation performs budding reproduction to
     *
     * @param generation
     * @return
     */
    private List<ScheduleData> reproduction(List<ScheduleData> generation) {
        return generation.stream()
                .map(parent -> budding(parent, N))
                .flatMap(List<ScheduleData>::stream)
                .collect(Collectors.toList());
    }


    /**
     * Represents budding reproduction process of one individual.
     *
     * @param oldRoute
     * @param buds     - number of "children"
     * @return
     */
    private List<ScheduleData> budding(ScheduleData oldRoute, int buds) {
        List<ScheduleData> list = new ArrayList<>(buds);

        while (list.size() < buds) {
            list.add(oldRoute.clone());
        }

        return list;
    }


    @Override
    public String toString() {
        return "GeneticStrategyScheduler{" + System.lineSeparator() +
                "    comment='" + comment + '\'' + "," + System.lineSeparator() +
                "    strategy=" + strategy.toString() + "," + System.lineSeparator() +
                "    G=" + G + "," + System.lineSeparator() +
                "    K=" + K + "," + System.lineSeparator() +
                "    N=" + N + "," + System.lineSeparator() +
                "    onlyChildren=" + onlyChildren + "," + System.lineSeparator() +
                "    isBigMutationAllowed=" + isBigMutationAllowed + System.lineSeparator() +
                "}";
    }
}

