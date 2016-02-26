package ru.ifmo.ctddev.scheduling.genetics;

import ru.ifmo.ctddev.scheduling.Strategy;

/**
 * Created by viacheslav on 26.02.2016.
 */
public class GeneticsSchedulerFctory {

    private static final int K = 12;

    private GeneticsSchedulerFctory() {
    }

    private static class Holder {
        public static final GeneticsSchedulerFctory INSTANCE = new GeneticsSchedulerFctory();
    }

    public GeneticsSchedulerFctory getInstance() {
        return Holder.INSTANCE;
    }

    public GeneticStrategyScheduler getOnePluOneScheduler(Strategy strategy, int generations) {
        return new GeneticStrategyScheduler(strategy, 1, 1, 1.0, 1, 1, generations, false);
    }

    public GeneticStrategyScheduler getOnePlusNScheduler(Strategy strategy, int generations, int n) {
        return new GeneticStrategyScheduler(strategy, 1, n, 1, 1, 1, generations, false);
    }

    public GeneticStrategyScheduler getOneCommaNScheduler(Strategy strategy, int generations, int n) {
        return new GeneticStrategyScheduler(strategy, 1, n, 1, 1, 1, generations, true);
    }

    public GeneticStrategyScheduler getKCommaKNScheduler(Strategy strategy, int generations, int n, int k) {
        return new GeneticStrategyScheduler(strategy, k, n, 1, k, 1, generations, false);
    }

    public GeneticStrategyScheduler getBigMutationsScheduler(Strategy strategy, int generations, int n) {
        return new GeneticStrategyScheduler(strategy, 1, n, 1, 1, 1, generations, false, true);
    }
}
