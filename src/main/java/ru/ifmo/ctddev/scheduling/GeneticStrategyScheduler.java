package ru.ifmo.ctddev.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 18.02.2016.
 */
public class GeneticStrategyScheduler implements Scheduler{

    private Strategy strategy;
    private int generationSize;
    private int generations;

    List<ScheduleData> currentGeneration;

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
        double initialCost = scheduleData.getCost();

        for (int i = 0; i < generationSize; ++i) {
            ScheduleData testScheduteData = scheduleData.clone();
            currentGeneration.add(testScheduteData);
        }


        for (int i = 0; i < generations; ++i) {
            List<ScheduleData> temp = currentGeneration.stream()
                    .map(old -> expandOneStepCopies(old, generationSize))
                    .flatMap(List<ScheduleData>::stream)
                    .collect(Collectors.toList());
            temp = getTopMembers(temp, generationSize);
            currentGeneration = mergeGenerations(currentGeneration, temp);
        }

        Collections.sort(currentGeneration);
        scheduleData = currentGeneration.get(0);

        return (initialCost - scheduleData.getCost()) / initialCost;
    }

    private List<ScheduleData> mergeGenerations(List<ScheduleData> oldGeneration, List<ScheduleData> newGeneration) {
        Collections.shuffle(oldGeneration);
        Collections.shuffle(newGeneration);
        List<ScheduleData> merged = oldGeneration.subList(0, oldGeneration.size() / 2);
        merged.addAll(newGeneration.subList(0, newGeneration.size() / 2));
        return merged;
    }

    private List<ScheduleData> getTopMembers(List<ScheduleData> generation, int maxSize) {
        Collections.sort(generation);
        Collections.reverse(generation);
        return generation.subList(0, Math.min(maxSize, generation.size()));
    }


    /**
     * Returns List of not more than {@code times} one-step-modified copies of given {@code scheduleData}.
     *
     * @param scheduleData
     * @param times
     * @return
     */
    private List<ScheduleData> expandOneStepCopies(ScheduleData scheduleData, int times) {
        List<ScheduleData> list = new ArrayList<>(times);
        for (int i = 0; i < times; ++i) {
            int[] route = strategy.getOptimiser().oneStep(scheduleData);
            if (scheduleData.checkConstraints(route)
                    && scheduleData.getCost(route) > scheduleData.getCost()) {
                list.add(scheduleData.clone(route));
            }
        }
        list.add(scheduleData.clone()); // todo is this a good idea?
        return list;
    }
}
