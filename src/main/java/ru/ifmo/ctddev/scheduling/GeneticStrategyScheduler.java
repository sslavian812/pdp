package ru.ifmo.ctddev.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 18.02.2016.
 */
public class GeneticStrategyScheduler implements Scheduler {

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


    public GeneticStrategyScheduler(Strategy strategy) {
        this(strategy, 10, 5, 0.99, 5, 10);
    }

    public GeneticStrategyScheduler(Strategy strategy, int g, int r, double pm, int e, int t) {
        this.strategy = strategy;
        currentGeneration = new ArrayList<>();
        G = g;
        R = r;
        Pm = pm;
        E = e;
        T = t;
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
        S = scheduleData.getSize()*scheduleData.getSize();
        double initialCost = scheduleData.getCost();
        currentGeneration.clear();

        // form first generation:
        currentGeneration.add(scheduleData.getRoute().clone());
        for (int i = 1; i < G; ++i) {
            currentGeneration.add(scheduleData.getRoute().clone());
        }

        for (int i = 0; i < T; ++i) {
            List<int[]> reproducted = reproduction(currentGeneration);
            List<int[]> mutated = mutation(reproducted);
            currentGeneration = selection(mutated);
        }

        Collections.sort(currentGeneration, originalScheduleData);
        scheduleData.setRoute(currentGeneration.get(0));

        return (initialCost - scheduleData.getCost()) / initialCost;
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
                    if (r.nextDouble() < Pm) {
                        originalScheduleData.setRoute(individual);
                        int[] route = strategy.getOptimiser().oneStep(originalScheduleData);
                        if (originalScheduleData.checkConstraints(route)
                                && originalScheduleData.getCost(route) < originalScheduleData.getCost(individual)) {
                            return route;
                        } else
                            return individual;
                    } else
                        return individual;
                }).collect(Collectors.toList());
    }

    /**
     * Emulates reproduction process.
     * Each individual from generation performs budding reproduction to
     * @param generation
     * @return
     */
    private List<int[]> reproduction(List<int[]> generation) {
        return generation.stream()
                .map(parent -> budding(parent, G))
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
//        list.add(oldRoute.clone());
        while (list.size() < times) {
            originalScheduleData.setRoute(oldRoute);
            int[] route = strategy.getOptimiser().oneStep(originalScheduleData);
            if (originalScheduleData.checkConstraints(route)
                    && originalScheduleData.getCost(route) < originalScheduleData.getCost(oldRoute)) {
                list.add(route);
            } else {
                list.add(oldRoute); // todo: low performance - lot's of objects will be the same.
            }
        }
        return list;
    }
}
