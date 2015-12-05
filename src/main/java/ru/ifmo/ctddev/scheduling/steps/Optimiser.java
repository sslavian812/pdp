package ru.ifmo.ctddev.scheduling.steps;

import ru.ifmo.ctddev.scheduling.ScheduleData;

/**
 * Created by viacheslav on 05.12.2015.
 */
public interface Optimiser {

    /**
     * Try one optimization step;
     * @return new solution
     */
    int[] oneStep(ScheduleData data);
}
