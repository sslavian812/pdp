package ru.ifmo.ctddev.scheduling.genetics;

import ru.ifmo.ctddev.scheduling.strategies.ConstantStrategy;

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

    public GeneticStrategyScheduler getOnePluOneScheduler(ConstantStrategy strategy, int generations) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, 1, 1, 1.0, 1, 1, generations, false);
        g.setComment("1+1");
        return g;
    }

    public GeneticStrategyScheduler getOnePlusNScheduler(ConstantStrategy strategy, int generations, int n) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, 1, n, 1.0, 1, 1, generations, false);
        g.setComment("1+N");
        return g;
    }

    public GeneticStrategyScheduler getOneCommaNScheduler(ConstantStrategy strategy, int generations, int n) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, 1, n, 1.0, 1, 1, generations, true);
        g.setComment("1,N");
        return g;
    }

    public GeneticStrategyScheduler getKPlusKNScheduler(ConstantStrategy strategy, int generations, int n, int k) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, k, n, 1.0, k, 1, generations, false);
        g.setComment("K+KN");
        return g;
    }

    public GeneticStrategyScheduler getBigMutationsScheduler(ConstantStrategy strategy, int generations, int n) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, 1, n, 1.0, 1, 1, generations, false, true);
        g.setComment("1+N,BM");
        return g;
    }


}
