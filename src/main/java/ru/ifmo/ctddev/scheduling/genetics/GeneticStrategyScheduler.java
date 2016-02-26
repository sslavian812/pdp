package ru.ifmo.ctddev.scheduling.genetics;

import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.Strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 18.02.2016.
 */
public class GeneticStrategyScheduler implements Scheduler {

    public static final int BIG_MUTATION_AMOUNT = 100;

    // internals:
    private Strategy strategy;
    private ScheduleData originalScheduleData;
    private List<int[]> currentGeneration;


    //parameters:
    private int G; // generation - size of generation
    private int R; // number of new created individuals at point of reproduction
    private double Pm; // probability of mutation for each inividual.
    private int S; // steps of algorith to be done
    private int E; // (E <= G) number of "elite" individuals to be selected to new generation
    private int T; // number of tournaments to select G-E non-elite inidividuals

    /**
     * if true, new population will be chosen only from children.
     * if false, new population will be chosen from parents and children.
     */
    private boolean onlyChildren;

    private boolean isBigMutationAllowed;


    public GeneticStrategyScheduler(Strategy strategy) {
        this(strategy, 100, 10, 1.0, 20, 10, 200, false);
    }

    public GeneticStrategyScheduler(Strategy strategy, int g, int r, double pm, int e, int t, int s) {
        this.strategy = strategy;
        currentGeneration = new ArrayList<>();
        G = g;
        R = r;
        Pm = pm;
        E = e;
        T = t;
        S = s;
        this.onlyChildren = false;
        this.isBigMutationAllowed = false;
        if (E > G)
            throw new RuntimeException("should be E <= G. ");
    }

    public GeneticStrategyScheduler(Strategy strategy, int g, int r, double pm, int e, int t, int s, boolean onlyChildren) {
        this.strategy = strategy;
        currentGeneration = new ArrayList<>();
        G = g;
        R = r;
        Pm = pm;
        E = e;
        T = t;
        S = s;
        this.onlyChildren = onlyChildren;
        isBigMutationAllowed = false;
        if (E > G)
            throw new RuntimeException("should be E <= G. ");
    }


    public GeneticStrategyScheduler(Strategy strategy, int g, int r, double pm, int e, int t, int s, boolean onlyChildren, boolean isBigMutationAllowed) {
        this.strategy = strategy;
        currentGeneration = new ArrayList<>();
        G = g;
        R = r;
        Pm = pm;
        E = e;
        T = t;
        S = s;
        this.onlyChildren = onlyChildren;
        this.isBigMutationAllowed = isBigMutationAllowed;
        if (E > G)
            throw new RuntimeException("should be E <= G. ");
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
        double initialCost = scheduleData.getCost();
        currentGeneration.clear();

        // form first generation:
        currentGeneration.add(scheduleData.getRoute().clone());
        for (int i = 1; i < G; ++i) {
            currentGeneration.add(scheduleData.getRoute().clone());
        }

        for (int i = 0; i < S; ++i) {
            if (i == S / 4 || i == S / 2 || S == 3.0 / 4.0 * (double) S) {
                currentGeneration = bigMutations(currentGeneration);
            }
            List<int[]> reproducted = reproduction(currentGeneration);
            List<int[]> mutated = mutation(reproducted);
            currentGeneration = selection(mutated);
        }

        Collections.sort(currentGeneration, originalScheduleData);
        scheduleData.setRoute(currentGeneration.get(0));

        return (initialCost - scheduleData.getCost()) / initialCost;
    }

    private List<int[]> bigMutations(List<int[]> generation) {
        return generation.stream().map(individual -> {
            for (int i = 0; i < BIG_MUTATION_AMOUNT; ++i)
                individual = mutate(individual);
            return individual;
        }).collect(Collectors.toList());
    }

    private int[] mutate(int[] individual) {
        originalScheduleData.setRoute(individual);
        int[] route = strategy.getOptimiser().oneStep(originalScheduleData);
        if (originalScheduleData.checkConstraints(route))
            return route;
        else
            return individual;
    }

    private List<int[]> selection(List<int[]> generation) {
        List<int[]> list = new ArrayList<>(G);
        Collections.sort(generation, originalScheduleData);
        list.addAll(generation.subList(0, E));
        for (int i = 0; i < G - E; ++i) {
            list.add(tournament(generation));
        }
        return list;
    }

    /**
     * Emulates a tournament.
     * T individuals are randomly chosen from generation.
     * The winner is the individual with the best cost.
     *
     * @param generation
     * @return
     */
    private int[] tournament(List<int[]> generation) {
        Random r = new Random();
        List<int[]> participants = new ArrayList<>(T);
        while (participants.size() < T) {
            participants.add(generation.get((int) (r.nextDouble() * generation.size())));
        }
        Collections.sort(participants, originalScheduleData);
        return participants.get(0);
    }

    /**
     * Emulates mutation process.
     * For each individual in generation there will be one try of Optimizer with probability Pm.
     *
     * @param generation
     * @return
     */
    private List<int[]> mutation(List<int[]> generation) {
        final Random r = new Random();
        return generation.stream()
                .map(individual -> {
                    if (r.nextDouble() < Pm)
                        return mutate(individual);
                    else
                        return individual;
                }).collect(Collectors.toList());
    }

    /**
     * Emulates reproduction process.
     * Each individual from generation performs budding reproduction to
     *
     * @param generation
     * @return
     */
    private List<int[]> reproduction(List<int[]> generation) {
        return generation.stream()
                .map(parent -> budding(parent, R))
                .flatMap(List<int[]>::stream)
                .collect(Collectors.toList());
    }


    /**
     * Represents budding reproduction process of one individual.
     *
     * @param oldRoute
     * @param times
     * @return
     */
    private List<int[]> budding(int[] oldRoute, int times) {
        List<int[]> list = new ArrayList<>(times);
        if (!onlyChildren)
            list.add(oldRoute.clone());

        while (list.size() < times) {
            originalScheduleData.setRoute(oldRoute);
            int[] route = strategy.getOptimiser().oneStep(originalScheduleData);
            if (originalScheduleData.checkConstraints(route)) {
                list.add(route);
//            } else {
//                list.add(oldRoute); // todo: low performance - lot's of objects will be the same.
            }
        }
        return list;
    }
}
