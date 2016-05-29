package ru.ifmo.ctddev.scheduling.genetics;

import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.strategies.ConstantStrategy;
import ru.ifmo.ctddev.scheduling.strategies.SmartL2OandRBStrategy;
import ru.ifmo.ctddev.scheduling.strategies.StatefulStrategy;
import ru.ifmo.ctddev.scheduling.strategies.Strategy;

import java.util.ArrayList;
import java.util.List;

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
        g.setComment("1+1");
        return g;
    }

    public GeneticStrategyScheduler getOnePlusNScheduler(Strategy strategy, int generations, int n) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, 1, n, 1.0, 1, 1, generations, false);
        g.setComment("1+N");
        return g;
    }

    public GeneticStrategyScheduler getOneCommaNScheduler(Strategy strategy, int generations, int n) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, 1, n, 1.0, 1, 1, generations, true);
        g.setComment("1,N");
        return g;
    }

    public GeneticStrategyScheduler getKPlusKNScheduler(Strategy strategy, int generations, int n, int k) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, k, n, 1.0, k, 1, generations, false);
        g.setComment("K+KN");
        return g;
    }

    public GeneticStrategyScheduler getBigMutationsScheduler(Strategy strategy, int generations, int n) {
        GeneticStrategyScheduler g = new GeneticStrategyScheduler(strategy, 1, n, 1.0, 1, 1, generations, false, true);
        g.setComment("1+N,BM");
        return g;
    }


    /**
     * Singlethread.
     *
     * @param strategy
     * @param pairs
     * @return
     */
    public List<Scheduler> getSimpleSchedulers(Strategy strategy, int pairs) {
        List<Scheduler> schedulers = new ArrayList<>();

        int generations = 12 * pairs * pairs;
        int F = 200_000;
        int N = (int) Math.sqrt(pairs / 2.0);
        int K = (int) Math.sqrt(pairs / 4.0);
        schedulers.add(new SimpleGeneticsStrategyScheduler("1+1", strategy, 1, 1, F, generations, false, false));
        schedulers.add(new SimpleGeneticsStrategyScheduler("1+N", strategy, 1, N, F, generations, false, false));
        schedulers.add(new SimpleGeneticsStrategyScheduler("1,N", strategy, 1, N, F, generations, true, false));
        schedulers.add(new SimpleGeneticsStrategyScheduler("1+N,BM", strategy, 1, N, F, generations, false, true));
        schedulers.add(new SimpleGeneticsStrategyScheduler("K+KN", strategy, K, N, F, generations, false, false));

        return schedulers;
    }


    public List<Scheduler> getGraphickMakingSchedulers(Strategy strategy, int pairs) {
        List<Scheduler> schedulers = new ArrayList<>();

        int generations = 12 * pairs * pairs;
        int F = 200_000;
        int N = (int) Math.sqrt(pairs / 2.0);
        int K = (int) Math.sqrt(pairs / 4.0);
        schedulers.add(new SimpleGeneticsStrategyScheduler("1+1", strategy, 1, 1, F, generations, false, false));
        schedulers.add(new SimpleGeneticsStrategyScheduler("1+N", strategy, 1, N, F, generations, false, false));
        schedulers.add(new SimpleGeneticsStrategyScheduler("1,N", strategy, 1, N, F, generations, true, false));
        schedulers.add(new SimpleGeneticsStrategyScheduler("1+N,BM", strategy, 1, N, F, generations, false, true));
        schedulers.add(new SimpleGeneticsStrategyScheduler("K+KN", strategy, K, N, F, generations, false, false));

        schedulers.stream()
        .forEach(scheduler -> {
            ((SimpleGeneticsStrategyScheduler) scheduler).setWriteGraphics(true);
        });
        return schedulers;
    }
}
