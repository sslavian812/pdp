package ru.ifmo.ctddev.scheduling.optimisers;

import ru.ifmo.ctddev.scheduling.ScheduleData;

/**
 * Created by viacheslav on 05.12.2015.
 * {@code optimiser} is {@code small-move}-provider.
 */
public interface Optimiser {

    /**
     * Try one optimization step({@code small-move}).
     * If constraints are satisfied, data {@code data} should be updated.
     * @param data
     * @return new solution
     */
    int[] oneStep(ScheduleData data);

    String toString();
}
