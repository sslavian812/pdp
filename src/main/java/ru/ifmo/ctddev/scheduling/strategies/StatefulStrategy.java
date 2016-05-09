package ru.ifmo.ctddev.scheduling.strategies;

import ru.ifmo.ctddev.scheduling.smallmoves.SmallMove;

/**
 * Created by viacheslav on 05.05.2016.
 */
public class StatefulStrategy implements Strategy {

    private SmallMove smallMove;
    private int called = 0;
    private int succeed = 0;


    public StatefulStrategy(SmallMove smallMove) {
        this.smallMove = smallMove;
        called = 0;
        succeed = 0;
    }

    @Override
    public SmallMove getSmallMove() {
        return smallMove;
    }

    @Override
    public void receiveReward(double reward) {
        if(reward != 0.0)
            ++called;

        if (reward > 0)
            ++succeed;

    }

    @Override
    public String getComment() {
        return "called: " + called + "; succeed: " + succeed;
    }

    @Override
    public String getDisplayName() {
        return "stateful: " + smallMove.toString();
    }

    @Override
    public void setDisplayName(String displayName) {
    }

    public void trim() {
        called = 0;
        succeed = 0;
    }

    public int getSucceed() {
        return succeed;
    }

    public int getCalled() {
        return called;
    }
}
