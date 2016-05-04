package ru.ifmo.ctddev.scheduling.strategies;

import ru.ifmo.ctddev.scheduling.smallmoves.SmallMove;

import java.util.List;

/**
 * Created by viacheslav on 04.05.2016.
 */
public class Policy extends ConstantStrategy {
    public Policy(List<SmallMove> smallMoves) {
        super(smallMoves);
    }

    public Policy(SmallMove smallMove) {
        super(smallMove);
    }

    public Policy(List<SmallMove> smallMoves, double[] probabilities) {
        super(smallMoves, probabilities);
    }
}
