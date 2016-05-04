package ru.ifmo.ctddev.scheduling.strategies;

import ru.ifmo.ctddev.scheduling.smallmoves.Lin2opt;
import ru.ifmo.ctddev.scheduling.smallmoves.RelocateBlock;
import ru.ifmo.ctddev.scheduling.smallmoves.SmallMove;


/**
 * Created by viacheslav on 04.05.2016.
 */
public class SmartL2OandRBStrategy implements Strategy {

    SmallMove lin2Opt = new Lin2opt();
    SmallMove relocateBlock = new RelocateBlock();
    boolean isL2O = true;
    int misses = 0;
    public static final int bound = 10;

    @Override
    public SmallMove getSmallMove() {
        if(isL2O)
            return lin2Opt;
        else {
            isL2O = true;
            misses = 0;
            return relocateBlock;
        }
    }

    @Override
    public void receiveReward(double reward) {
        if(reward > 0)
            misses = 0;
        else{
            ++misses;
            if(misses >= bound)
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
        return "smart L2O & RB";
    }

    @Override
    public void setDisplayName(String displayName) {
        return;
    }
}
