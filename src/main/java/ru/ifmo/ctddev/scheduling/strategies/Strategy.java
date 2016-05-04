package ru.ifmo.ctddev.scheduling.strategies;

import ru.ifmo.ctddev.scheduling.smallmoves.SmallMove;

import java.util.Arrays;

/**
 *
 * Strategy interface. The most important method  - getSmallMove.
 * Created by viacheslav on 04.05.2016.
 */
public interface Strategy {

    SmallMove getSmallMove();

    /**
     * This method gets called, when the strategy gets a reward.
     *
     * @param reward
     */
    default void receiveReward(double reward) {
        return;
    }

    @Deprecated
    String getComment();

    /**
     * The name of this particular strategy for displaying in answers.csv etc.
     * @return
     */
    String getDisplayName();

    void setDisplayName(String displayName);
}
