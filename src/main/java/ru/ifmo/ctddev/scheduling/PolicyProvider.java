package ru.ifmo.ctddev.scheduling;

import ru.ifmo.ctddev.scheduling.strategies.EpsGreedyPolicy;
import ru.ifmo.ctddev.scheduling.strategies.Strategy;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;

import java.util.List;

/**
 * Created by viacheslav on 02.06.2016.
 */
public class PolicyProvider {

    List<Strategy> provideAllPolicies() {
        return null;
    }

    public static Strategy provideEpsGreegyPolicy(double eps) {
        if (eps > 1 || eps < 0) {
            throw new RuntimeException("eps should be 0..1");
        }
        return new EpsGreedyPolicy(eps, StrategyProvider.getAllSmallMoves());
    }
}
