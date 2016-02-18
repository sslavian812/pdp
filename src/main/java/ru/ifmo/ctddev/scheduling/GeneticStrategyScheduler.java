package ru.ifmo.ctddev.scheduling;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslav on 18.02.2016.
 */
public class GeneticStrategyScheduler {

    private Strategy strategy;
    private int generationSize;
    private int generations;

    List<ScheduleData> oldGeneration, newGeneration;

    public GeneticStrategyScheduler(Strategy strategy, int generationSize, int generations) {
        this.strategy = strategy;
        this.generationSize = generationSize;
        this.generations = generations;
        oldGeneration = new ArrayList<>(generationSize);
        newGeneration = new ArrayList<>(generationSize);
    }

    /**
     * Creates a schedule for this {@code scheduleData} in genetic way.
     * <p>
     * Note: this method may take a long time.
     *
     * @param scheduleData
     * @return returns optimisation ratio: (initialCost - reachedCost) / initialCost
     */
    public double schedule(ScheduleData scheduleData) {
        for (int i = 0; i < generationSize; ++i) {
            ScheduleData testScheduteData = scheduleData.clone();
            oldGeneration.add(testScheduteData);
        }

        for (int i = 0; i < generations; ++i) {
            // todo manipulate and swap generations;
            // todo log properly
        }
        return 0.0;
    }

}
