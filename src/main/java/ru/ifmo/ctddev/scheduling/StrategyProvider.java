package ru.ifmo.ctddev.scheduling;

import ru.ifmo.ctddev.scheduling.smallmoves.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslav on 30.04.2016.
 */
public class StrategyProvider {

    /**
     * Provides all existing base(hardcoded, manualy created) strategies.
     * @return
     */
    public static List<Strategy> provideAllStrategies() {
        List<SmallMove> allSmallMoves = new ArrayList<>(5);
        allSmallMoves.add(new Lin2opt());
        allSmallMoves.add(new CoupleExchange());
        allSmallMoves.add(new DoubleBridge());
        allSmallMoves.add(new PointExchange());
        allSmallMoves.add(new RelocateBlock());

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
        strategies.add(new Strategy(new Lin2opt()));
        strategies.add(new Strategy(new CoupleExchange()));
        strategies.add(new Strategy(new DoubleBridge()));
        strategies.add(new Strategy(new PointExchange()));
        strategies.add(new Strategy(L2OandPX));
        strategies.add(new Strategy(L2OandDB));
        strategies.add(new Strategy(L2Oand3more, new double[]{3.0/6, 1.0/6, 1.0/6, 1.0/6 }));
        strategies.add(new Strategy(allSmallMoves));
        strategies.add(new Strategy(L2OandRB));
        strategies.add(new Strategy(new RelocateBlock()));

        return strategies;
    }
}
