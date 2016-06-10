package ru.ifmo.ctddev.scheduling.strategies;

import ru.ifmo.ctddev.scheduling.smallmoves.SmallMove;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by viacheslav on 04.05.2016.
 */
public class EpsGreedyPolicy implements Strategy, Cloneable {

    protected List<SmallMove> smallMoves;
    protected int[] hits;
    protected int calls;
    protected double eps;


    protected Random random = ThreadLocalRandom.current();
    protected String name = "eps-greedy";
    protected int lastSmallMove;


    // should be used to pick each strategy first time.
    private int stackpointer;


    public EpsGreedyPolicy(double eps, List<SmallMove> smallMoves) {
        this.eps = eps;
        this.calls = 0;
        this.lastSmallMove = -1;
        this.smallMoves = smallMoves;
        this.stackpointer = smallMoves.size() - 1;
        this.hits = new int[smallMoves.size()];
    }

    public void trim() {
        this.calls = 0;
        this.lastSmallMove = -1;
        this.stackpointer = smallMoves.size() - 1;
        this.hits = new int[smallMoves.size()];
    }

    @Override
    public SmallMove getSmallMove() {
        if (stackpointer >= 0) {
            --stackpointer;
            return smallMoves.get(stackpointer + 1);
        }

        // todo hard logic here
        int maxhits = Integer.MIN_VALUE;
        for (int i = 0; i < hits.length; ++i)
            if (hits[i] > maxhits)
                maxhits = hits[i];

        List<Integer> tops = new ArrayList<>(5);
//        List<Integer> others = new ArrayList<>(5);
        for (int i = 0; i < hits.length; ++i) {
            if (hits[i] == maxhits)
                tops.add(i);
//            else
//                others.add(i);
        }

        if (tops.size() > 0 && random.nextDouble() < eps) {
            // random from tops
            lastSmallMove = random.nextInt(tops.size());
        } else {
            // random from other
            lastSmallMove = random.nextInt(smallMoves.size());
        }

        return smallMoves.get(lastSmallMove);
    }


    @Override
    public void receiveReward(double reward) {
        ++calls;
        if (reward > 0) {
            ++hits[lastSmallMove];
        }
    }

    @Override
    public String getComment() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return name + "[" + new DecimalFormat("#0.0").format(eps) + "]";
    }

    @Override
    public void setDisplayName(String displayName) {
        name = displayName;
    }

    public EpsGreedyPolicy clone() {
        try {
            EpsGreedyPolicy copy = (EpsGreedyPolicy) super.clone();
            copy.stackpointer = smallMoves.size() - 1;
            copy.hits = new int[smallMoves.size()];
            copy.calls = 0;
            copy.lastSmallMove = -1;
            return copy;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
