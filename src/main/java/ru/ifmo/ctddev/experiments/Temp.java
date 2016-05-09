package ru.ifmo.ctddev.experiments;

import ru.ifmo.ctddev.scheduling.strategies.Strategy;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;

/**
 * Created by viacheslav on 09.05.2016.
 */
public class Temp {
    public static void main(String[] args) {

        System.out.println("economic: "  + StrategyProvider.getEconomicStrategy(50).getDisplayName());
        System.out.println("proportional: "  + StrategyProvider.getProportionalStrategy().getDisplayName());
        System.out.println("YN: "  + StrategyProvider.getYNStrategy().getDisplayName());
        System.out.println("economic proportional: "  + StrategyProvider.getProportionalEconomicStrategy(50).getDisplayName());

    }
}
