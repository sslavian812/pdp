package ru.ifmo.ctddev.scheduling.genetics;

import ru.ifmo.ctddev.scheduling.Strategy;

/**
 * Created by viacheslav on 26.02.2016.
 */
public class GeneticsSchedulerFactory {

    private static final int K = 12;

    private GeneticsSchedulerFactory() {
    }

    private static class Holder {
        public static final GeneticsSchedulerFactory INSTANCE = new GeneticsSchedulerFactory();
    }

    public static GeneticsSchedulerFactory getInstance() {
        return Holder.INSTANCE;
    }

//    public GeneticStrategyScheduler getOnePluOneNoMutationScheduler(Strategy strategy, int generations) {
//        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, 1, 1, 0.0, 1, 1, generations, false);
//        g.setComment("OnePluOneNoMutationScheduler");
//        return g;
//    }

    public GeneticStrategyScheduler getOnePluOneScheduler(Strategy strategy, int generations) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, 1, 1, 1.0, 1, 1, generations, false);
        g.setComment("OnePluOneScheduler");
        return g;
    }

    public GeneticStrategyScheduler getOnePlusNScheduler(Strategy strategy, int generations, int n) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, 1, n, 1.0, 1, 1, generations, false);
        g.setComment("OnePlusNScheduler");
        return g;
    }

    public GeneticStrategyScheduler getOneCommaNScheduler(Strategy strategy, int generations, int n) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, 1, n, 1.0, 1, 1, generations, true);
        g.setComment("OneCommaNScheduler");
        return g;
    }

    public GeneticStrategyScheduler getKPlusKNScheduler(Strategy strategy, int generations, int n, int k) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, k, n, 1.0, k, 1, generations, false);
        g.setComment("KPlusKNScheduler");
        return g;
    }

    public GeneticStrategyScheduler getBigMutationsScheduler(Strategy strategy, int generations, int n) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, 1, n, 1.0, 1, 1, generations, false, true);
        g.setComment("BigMutationsScheduler");
        return g;
    }


}
