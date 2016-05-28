package ru.ifmo.ctddev.minizinc;


import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.StrategyCyclicScheduler;
import ru.ifmo.ctddev.scheduling.StrategyScheduler;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by viacheslav on 20.05.2016.
 */
public class MinizincDatasetGenerator {
    public static void main(String[] args) {

        String dataset = "gaussian8000";
        String csv = ".csv";
        int start = 0;


        for (int size = 4; size <= 20; size += 2) {

            String curDataset = dataset + "_" + start + "_" + (start + size);

            ScheduleData initialData = DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT,
                    dataset + csv, curDataset + csv);

            Scheduler scheduler = new StrategyCyclicScheduler(StrategyProvider.getProportionalStrategy());
            ScheduleData data = initialData.clone();
            double ratio = scheduler.schedule(data);


            System.out.println("dataset: " + curDataset);
            System.out.println("initial cost: " + initialData.getCycleCost());
            System.out.println("reached cost: " + data.getCycleCost());
            System.out.println("ratio: " + ratio);

            String file = "pdp_" + curDataset;
            String dnz = ".dzn";
            String datasets = "minizinc/datasets/";
            try {

                FileWriter writer;
                writer= new FileWriter(datasets + file + "_any" + dnz);
                writer.write(scheduleDataToMinizinc(initialData));
                writer.flush();
                writer.close();

                writer = new FileWriter(datasets + file + "_initial" + dnz);
                writer.write(scheduleDataToMinizinc(initialData));
                writer.flush();
                writer.close();

                writer = new FileWriter(datasets + file + "_reached" + dnz);
                writer.write(scheduleDataToMinizinc(data));
                writer.flush();
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("dzn filed generated: " + file);
            System.out.println();
        }
    }


    public static String scheduleDataToMinizinc(ScheduleData scheduleData) {
        StringBuilder builder = new StringBuilder();

        builder.append("bound = ").append(Math.round(scheduleData.getCycleCost() + 1)).append(";");
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
