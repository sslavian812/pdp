package ru.ifmo.ctddev.scheduling.strategies;

import ru.ifmo.ctddev.scheduling.smallmoves.Lin2opt;
import ru.ifmo.ctddev.scheduling.smallmoves.RelocateBlock;
import ru.ifmo.ctddev.scheduling.smallmoves.SmallMove;


/**
 * Created by viacheslav on 04.05.2016.
 */
public class SmartL2OandRBStrategy implements Strategy, Cloneable {

    private SmallMove lin2Opt = new Lin2opt();
    private SmallMove relocateBlock = new RelocateBlock();
    private boolean isL2O = true;
    private int misses = 0;
    public static final int bound = 9;

    @Override
    public SmallMove getSmallMove() {
        if (isL2O)
            return lin2Opt;
        else {
            isL2O = true;
            misses = 0;
            return relocateBlock;
        }
    }

    @Override
    public void receiveReward(double reward) {
        if (reward > 0)
            misses = 0;
        else {
            ++misses;
            if (misses >= bound)
                isL2O = false;
        }
    }

    @Deprecated
    @Override
    public String getComment() {
        return "smart L2O & RB";
    }

    @Override
    public String getDisplayName() {
        return "smart L2O & RB [" + bound + "]";
    }

    @Override
    public void setDisplayName(String displayName) {
        return;
    }

    public SmartL2OandRBStrategy clone() {
        try {
            SmartL2OandRBStrategy copy = (SmartL2OandRBStrategy) super.clone();
            copy.isL2O = true;
            copy.misses = 0;
            return copy;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
