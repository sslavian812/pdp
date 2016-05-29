package ru.ifmo.ctddev.minizinc;


import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.StrategyCyclicScheduler;
import ru.ifmo.ctddev.scheduling.StrategyScheduler;
import ru.ifmo.ctddev.scheduling.genetics.SimpleGeneticsStrategyScheduler;
import ru.ifmo.ctddev.scheduling.strategies.Strategy;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslav on 20.05.2016.
 */
public class MinizincDatasetGenerator {
    public static void main(String[] args) {

        String dataset = "taxi8000";
        String csv = ".csv";
        int start = 0;

        boolean firstTime = true;

        for (int size = 4; size <= 20; size += 2) {

            String curDataset = dataset + "_" + start + "_" + (start + size);

            ScheduleData initialData = DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT,
                    dataset + csv, curDataset + csv);

            Strategy strategy = StrategyProvider.getProportionalEconomicStrategy(size);
            int generations = 12 * size * size;
//            int F = (int) (1.6 * (double) size) * size * size;
            int F = 200_000;
            int N = (int) Math.sqrt(size / 2.0);
            int K = (int) Math.sqrt(size / 4.0);

            Scheduler OnePlusOne = new SimpleGeneticsStrategyScheduler("1+1", strategy, 1, 1, F, generations, false, false);
            Scheduler OnePlusN = new SimpleGeneticsStrategyScheduler("1+N", strategy, 1, N, F, generations, false, false);
            Scheduler KPlusKN = new SimpleGeneticsStrategyScheduler("K+KN", strategy, K, N, F, generations, false, false);

            List<Scheduler> schedulers = new ArrayList<>();
            schedulers.add(OnePlusOne);
            schedulers.add(OnePlusN);
            schedulers.add(KPlusKN);


            if(firstTime) {
                System.out.println("warmup:");
                for (Scheduler scheduler : schedulers) {
                    ScheduleData data = initialData.clone();
                    scheduler.schedule(data);
                }
                System.out.println("end warmup");
                firstTime = false;
            }

            double bestCost = Double.MAX_VALUE;

            for (Scheduler scheduler : schedulers) {

                ScheduleData data = initialData.clone();
                long startTime = System.currentTimeMillis();
                double ratio = scheduler.schedule(data);
                bestCost = Math.min(bestCost, data.getCycleCost());
                long spent = System.currentTimeMillis() - startTime;

                System.out.println("dataset: " + curDataset);
                System.out.println("fit function calls: " + F);
                System.out.println("initial cost: " + initialData.getCycleCost() + " ~ " + Math.round(initialData.getCycleCost()));
                System.out.println("reached cost: " + data.getCycleCost() + " ~ " + Math.round(data.getCycleCost()));
                System.out.println("ratio: " + ratio);
                System.out.println("time: " + spent + "ms ~ " + spent / 1000 + "s");
            }

            String file = "pdp_" + curDataset;
            String dnz = ".dzn";
            String datasets = "minizinc/datasets/taxi/";
            try {

                FileWriter writer;
                writer = new FileWriter(datasets + file + "_any" + dnz);
                writer.write(scheduleDataToMinizinc(initialData, 99999999));
                writer.flush();
                writer.close();

                writer = new FileWriter(datasets + file + "_initial" + dnz);
                writer.write(scheduleDataToMinizinc(initialData, Math.round(initialData.getCycleCost()) + 1));
                writer.flush();
                writer.close();

                writer = new FileWriter(datasets + file + "_reached" + dnz);
                writer.write(scheduleDataToMinizinc(initialData, Math.round(bestCost) + 1));
                writer.flush();
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("dzn filed generated: " + file);
            System.out.println();
        }
    }


    public static String scheduleDataToMinizinc(ScheduleData scheduleData, long bound) {
        StringBuilder builder = new StringBuilder();

        builder.append("bound = ").append(bound).append(";");

        builder.append(System.lineSeparator());
        builder.append("n = ").append(scheduleData.getSize()).append(";").append(System.lineSeparator());
        builder.append("p = ").append(scheduleData.getOrdersNum()).append(";").append(System.lineSeparator());
        builder.append("num_edges = ").append(
                scheduleData.getSize() * (scheduleData.getSize() - 1)).append(";").append(System.lineSeparator());

        builder.append("E = array2d(1..num_edges, 1..2, [").append(System.lineSeparator());

        for (int i = 1; i <= scheduleData.getSize(); ++i) {
            for (int j = 1; j <= scheduleData.getSize(); ++j) {
                if (i == j)
                    continue;
                builder.append(i).append(",").append(j).append(System.lineSeparator());
                if (i != scheduleData.getSize() || j != scheduleData.getSize() - 1)
                    builder.append(",");
            }
        }

        builder.append("]);").append(System.lineSeparator());

        builder.append("c=[").append(System.lineSeparator());

        for (int i = 1; i <= scheduleData.getSize(); ++i) {
            for (int j = 1; j <= scheduleData.getSize(); ++j) {
                if (i == j)
                    continue;
                builder.append(Math.round(scheduleData.dist(i - 1, j - 1)));//.append(",").append(System.lineSeparator());
                if (i != scheduleData.getSize() || j != scheduleData.getSize() - 1)
                    builder.append(",").append(System.lineSeparator());
            }
        }

        builder.append("];").append(System.lineSeparator());


        return builder.toString();
    }
}
