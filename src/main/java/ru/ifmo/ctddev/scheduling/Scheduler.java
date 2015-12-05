package ru.ifmo.ctddev.scheduling;

/**
 * Created by viacheslav on 01.12.2015.
 */
public class Scheduler {

    /**
     * simple algorithm
     *
     * @param data
     * @return
     */
    public double runSceduler(ScheduleData data) {

        System.out.println("initial state:");
        for (int i : data.getRoute())
            System.out.print(i + " ");
        System.out.println("");
        System.out.println("constraint: " + data.checkConstraints());
        System.out.println("cost: " + data.getCost());

        for (int i = 0; i < data.ordersNum * data.ordersNum; ++i)
            persorm2Opt(data);

        System.out.println();
        System.out.println("final state:");
        for (int i : data.getRoute())
            System.out.print(i + " ");
        System.out.println("");
        System.out.println("constraint: " + data.checkConstraints());
        System.out.println("cost: " + data.getCost());

        return data.getCost();
    }

    // todo construction method (bestInsertion or other)
    // todo small-steps-heuristics with strategy.


    public void persorm2Opt(ScheduleData data) {
        Lin2opt l2o = new Lin2opt();
        int[] r = l2o.oneStep(data);

        // check and accept or reject:
        if (data.checkConstraints(r)) {
            System.out.println("constraints: OK");
            System.out.println("initial: " + data.getCost() + "; reached: " + data.getCost(r));
            if (data.getCost(r) < data.getCost()) {
                System.out.println("ACCEPTED");
                data.setRoute(r);
            } else
                System.out.println("rejected");
        } else
            System.out.println("constraints NO");
    }

}
