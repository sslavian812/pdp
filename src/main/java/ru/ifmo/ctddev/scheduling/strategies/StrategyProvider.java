package ru.ifmo.ctddev.scheduling.strategies;

import ru.ifmo.ctddev.scheduling.smallmoves.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viacheslav on 30.04.2016.
 */
public class StrategyProvider {


    /**
     * Provides all existing base(hardcoded, manualy created) strategies.
     *
     * @return
     */
    public static List<Strategy> provideAllStrategies() {
        List<SmallMove> allSmallMoves = getAllSmallMoves();
//        allSmallMoves.add(new Lin2opt());
//        allSmallMoves.add(new CoupleExchange());
//        allSmallMoves.add(new DoubleBridge());
//        allSmallMoves.add(new PointExchange());
//        allSmallMoves.add(new RelocateBlock());

        List<SmallMove> L2OandPX = new ArrayList<>(2);
        L2OandPX.add(new Lin2opt());
        L2OandPX.add(new PointExchange());

        List<SmallMove> L2OandRB = new ArrayList<>(2);
        L2OandRB.add(new Lin2opt());
        L2OandRB.add(new RelocateBlock());

        List<SmallMove> L2OandDB = new ArrayList<>(2);
        L2OandDB.add(new Lin2opt());
        L2OandDB.add(new DoubleBridge());

        List<SmallMove> L2Oand3more = new ArrayList<>(4);
        L2Oand3more.add(new Lin2opt());
        L2Oand3more.add(new PointExchange());
        L2Oand3more.add(new RelocateBlock());
        L2Oand3more.add(new DoubleBridge());

        // creating strategies:

        List<Strategy> strategies = new ArrayList<>();
        strategies.add(new ConstantStrategy(new Lin2opt()));
        strategies.add(new ConstantStrategy(new CoupleExchange()));
        strategies.add(new ConstantStrategy(new DoubleBridge()));
        strategies.add(new ConstantStrategy(new PointExchange()));

        strategies.add(new ConstantStrategy(new RelocateBlock()));

        strategies.add(new ConstantStrategy(allSmallMoves));
        strategies.add(new ConstantStrategy(L2OandDB));
        strategies.add(new ConstantStrategy(L2OandPX));
        strategies.add(new ConstantStrategy(L2OandRB));
        strategies.add(new ConstantStrategy(L2Oand3more, new double[]{
                3.0 / 6,
                1.0 / 6,
                1.0 / 6,
                1.0 / 6}));

        strategies.add(new ConstantStrategy(L2Oand3more));

        strategies.add(getEconomicStrategy(50));
        strategies.add(getProportionalStrategy());
        strategies.add(getYNStrategy());
        strategies.add(getProportionalEconomicStrategy(50));
        strategies.add(provideFastStrategy());

//        strategies.add(getSmartL2ORBStrategy());

        return strategies;
    }


    public static Strategy provideFastStrategy() {
        List<SmallMove> fastMoves = new ArrayList<>(4);
        fastMoves.add(new Lin2opt());
        fastMoves.add(new CoupleExchange());
        fastMoves.add(new DoubleBridge());
        fastMoves.add(new PointExchange());
        // [0.25,0.25,0.25,0.25,0]

        return new ConstantStrategy(fastMoves);
    }

    public static List<Strategy> provideStatefulStrategies() {
        List<Strategy> strategies = new ArrayList<>();
        strategies.add(new StatefulStrategy(new Lin2opt()));
        strategies.add(new StatefulStrategy(new CoupleExchange()));
        strategies.add(new StatefulStrategy(new DoubleBridge()));
        strategies.add(new StatefulStrategy(new PointExchange()));
        strategies.add(new StatefulStrategy(new RelocateBlock()));
        return strategies;
    }

    public static List<SmallMove> getAllSmallMoves() {
        List<SmallMove> allSmallMoves = new ArrayList<>(5);
        allSmallMoves.add(new Lin2opt());
        allSmallMoves.add(new CoupleExchange());
        allSmallMoves.add(new DoubleBridge());
        allSmallMoves.add(new PointExchange());
        allSmallMoves.add(new RelocateBlock());
        return allSmallMoves;
    }

    private static Map<String, String> getShortNamesMap() {
        Map<String, String> map = new HashMap<>(5);
        map.put(new Lin2opt().toString(), "LO");
        map.put(new CoupleExchange().toString(), "CX");
        map.put(new DoubleBridge().toString(), "DB");
        map.put(new PointExchange().toString(), "PX");
        map.put(new RelocateBlock().toString(), "RB");
        return map;
    }

//    public static String getProbabilities(Strategy strategy) {
//        Map<String, Double> map = new HashMap<>();
//        List<SmallMove> smallMoves = strategy.getSmallMoves();
//        double[] probabilities = strategy.getProbabilities();
//
//        for (int i = 0; i < smallMoves.size(); ++i) {
//            map.put(smallMoves.get(i).toString(), probabilities[i]);
//        }
//
//        StringBuilder stringBuilder = new StringBuilder("[");
////        Map<String, String > shortMap = getShortNamesMap();
////        stringBuilder.append(String.join(",", getAllSmallMoves().stream()
////                .map(smallMove -> shortMap.get(smallMove.toString()))
////                .collect(Collectors.toList())));
////        stringBuilder.append("];[");
//
//        stringBuilder.append(String.join(",", getAllSmallMoves().stream().map(smallMove -> {
//            if (map.containsKey(smallMove.toString()))
//                return "" + map.get(smallMove.toString());
//            else
//                return "" + 0.0;
//
//        }).collect(Collectors.toList())));
//
//
//        stringBuilder.append("]");
//        return stringBuilder.toString();
//    }

    public static Strategy getSmartL2ORBStrategy() {
        return new SmartL2OandRBStrategy(12);
    }

    public static Map<String, String> getStrategiesMapToClasses() {
        Map<String, String> propToClassMap = new HashMap<>();
        final int[] i = {1};

        provideAllStrategies().stream().map(s -> s.getDisplayName())
                .forEach(p -> {
                    propToClassMap.put(p, "" + i[0]);
                    ++i[0];
                });
        return propToClassMap;
    }


    public static Strategy getEconomicStrategy(int ordersNum) {
        List<SmallMove> sm = StrategyProvider.getAllSmallMoves();


        double[] probabilities = distributeAccordingTo(
                new double[]{1, 1, 1, 1, 1.0 / ordersNum}); // this is 1/ni

        ConstantStrategy strategy = new ConstantStrategy(sm, probabilities);
        strategy.setComment("economic (ni-)strategy");

        return strategy;
    }

    public static Strategy getProportionalStrategy() {
        List<SmallMove> sm = StrategyProvider.getAllSmallMoves();

        double[] probabilities = distributeAccordingTo(new double[]{
                196,
                95,
                54,
                96,
                159
        });  // this is proportional to f_i * total_calls

        ConstantStrategy strategy = new ConstantStrategy(sm, probabilities);
        strategy.setComment("proportional (fi-)strategy");
        strategy.setDisplayName(strategy.getComment());
        return strategy;
    }

    public static Strategy getYNStrategy() {
        List<SmallMove> sm = StrategyProvider.getAllSmallMoves();

        double[] probabilities = distributeAccordingTo(new double[]{
                6.91,
                3.17,
                3.29,
                3.17,
                3.30,
        });  // this is proportional to c_i

        ConstantStrategy strategy = new ConstantStrategy(sm, probabilities);
        strategy.setComment("yn (ci-)strategy");
        strategy.setDisplayName(strategy.getComment());
        return strategy;
    }

    public static Strategy getProportionalEconomicStrategy(int ordersNum) {
        List<SmallMove> sm = StrategyProvider.getAllSmallMoves();

        double[] probabilities = distributeAccordingTo(new double[]{
                196,
                95,
                54,
                96,
                159.0 / ordersNum
        });


        ConstantStrategy strategy = new ConstantStrategy(sm, probabilities);
        strategy.setComment("prop-econ (fi/ni-)strategy");
        strategy.setDisplayName(strategy.getComment());
        return strategy;
    }

    /**
     * Provides an array with probabilities according to double[] arguments
     *
     * @param doubles
     * @return
     */
    private static double[] distributeAccordingTo(double[] doubles) {
        double sum = 0.0;
        for (double d : doubles)
            sum += d;
        double[] probabilities = new double[doubles.length];
        for (int i = 0; i < doubles.length; ++i)
            probabilities[i] = doubles[i] / sum;
        return probabilities;
    }

    public static Map<String, String> getTop5() {
        Map<String, String> map = new HashMap<>();
        map.put("[0.32666666666666666,0.15833333333333333,0.09,0.16,0.265]", "1");
        map.put("[0.3482862903225806,0.15977822580645162,0.1658266129032258,0.15977822580645162,0.1663306451612903]", "2");
        map.put("smart L2O & RB [10]", "3");
        map.put("[0.5,0.0,0.16666666666666666,0.16666666666666666,0.16666666666666666]", "4");
        map.put("[0.5,0.0,0.0,0.0,0.5]", "5");
        return map;
    }
}
