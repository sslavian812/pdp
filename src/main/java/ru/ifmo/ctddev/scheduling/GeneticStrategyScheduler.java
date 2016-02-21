package ru.ifmo.ctddev.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 18.02.2016.
 */
public class GeneticStrategyScheduler implements Scheduler {

    private Strategy strategy;
    private int generationSize;
    private int generations;
    private ScheduleData originalScheduleData;

    List<int[]> currentGeneration;

    public GeneticStrategyScheduler(Strategy strategy, int generationSize, int generations) {
        this.strategy = strategy;
        this.generationSize = generationSize;
        this.generations = generations;
        currentGeneration = new ArrayList<>(generationSize);
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

        for (int i = 0; i < generationSize; ++i) {
            currentGeneration.add(scheduleData.getRoute().clone());
        }

        for (int i = 0; i < generations; ++i) {
            List<int[]> temp = currentGeneration.stream()
                    .map(old -> expandOneStepCopies(old, generationSize))
                    .flatMap(List<int[]>::stream)
                    .collect(Collectors.toList());
            temp = getTopMembers(temp, generationSize);
            currentGeneration = mergeGenerations(currentGeneration, temp);
        }

        Collections.sort(currentGeneration, originalScheduleData);
        scheduleData.setRoute(currentGeneration.get(0));

        return (initialCost - scheduleData.getCost()) / initialCost;
    }

    private List<int[]> mergeGenerations(List<int[]> oldGeneration, List<int[]> newGeneration) {
        Collections.shuffle(oldGeneration);
        Collections.shuffle(newGeneration);

        for (int i = oldGeneration.size() / 2; i < newGeneration.size() && i < oldGeneration.size(); ++i) {
            oldGeneration.set(i, newGeneration.get(i));
        }

        return oldGeneration;
    }

    private List<int[]> getTopMembers(List<int[]> generation, int maxSize) {
        Collections.sort(generation, originalScheduleData);
        return generation.subList(0, Math.min(maxSize, generation.size()));
    }


    /**
     * Returns List of not more than {@code times} one-step-modified copies of given {@code scheduleData}.
     *
     * @param oldRoute
     * @param times
     * @return
     */
    private List<int[]> expandOneStepCopies(int[] oldRoute, int times) {
        List<int[]> list = new ArrayList<>(times);
        for (int i = 0; i < times; ++i) {
            originalScheduleData.setRoute(oldRoute);
            int[] route = strategy.getOptimiser().oneStep(originalScheduleData);
            if (originalScheduleData.checkConstraints(route)
                    && originalScheduleData.getCost(route) < originalScheduleData.getCost(oldRoute)) {
                list.add(route);
            }
        }
        if (list.isEmpty())
            list.add(oldRoute); // todo is this a good idea?
        return list;
    }
}
