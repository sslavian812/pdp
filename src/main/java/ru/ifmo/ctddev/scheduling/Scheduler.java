package ru.ifmo.ctddev.scheduling;

/**
 * Created by viacheslav on 01.12.2015.
 */
public class Scheduler {


    private Optimiser optimiser;

    public static final int K = 10;

    public Scheduler(Optimiser optimizer) {
        this.optimiser = optimizer;
    }


    public double schedule(ScheduleData data) {

        System.out.println("initial state:");
        for (int i : data.getRoute())
            System.out.print(i + " ");
        System.out.println("");
        System.out.println("constraint: " + data.checkConstraints());
        double initialCost = data.getCost();
        System.out.println("cost: " + initialCost);

        for (int i = 0; i < K * data.ordersNum * data.ordersNum; ++i)
            performStep(data);

        System.out.println();
        System.out.println("final state:");
        for (int i : data.getRoute())
            System.out.print(i + " ");
        System.out.println("");
        System.out.println("constraint: " + data.checkConstraints());
        System.out.println("cost: " + data.getCost());

        double reachedCost = data.getCost();
        System.out.println("optimization ratio: " + (initialCost - reachedCost) / initialCost * 100 + "%");
        System.out.println();

        return data.getCost();
    }

    // todo construction method (bestInsertion or other)
    // todo small-steps-heuristics with strategy.


    public void performStep(ScheduleData data) {
        int[] r = optimiser.oneStep(data);

        // check and accept or reject:
        if (data.checkConstraints(r)) {
//            System.out.println("constraints: OK");
            if (data.getCost(r) < data.getCost()) {
//                System.out.println("initial: " + data.getCost() + "; reached: " + data.getCost(r));
//                System.out.println("ACCEPTED");
                data.setRoute(r);
            } else {
//                System.out.println("rejected");
            }
        } else {
//            System.out.println("constraints NO");
        }
    }

}
