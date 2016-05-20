package ru.ifmo.ctddev.datasets;


import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.StrategyScheduler;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by viacheslav on 20.05.2016.
 */
public class MinizincDatasetGenerator {
    public static void main(String[] args) {
        ScheduleData initialData = DatasetProvider.getFromFile("src/main/resources/ml/data/gaussian8000_0_50.csv");
        Scheduler scheduler = new StrategyScheduler(StrategyProvider.getProportionalStrategy());
        ScheduleData data = initialData.clone();
        scheduler.schedule(data);


        System.out.println("dataset: gaussian8000_0_50");
        System.out.println("initial cost: " + initialData.getCost());
        System.out.println("reached cost: " + data.getCost());
        System.out.println("dzn filed generated");
        try {
            FileWriter writer = new FileWriter("pdp_gaussian8000_0_50_initial.dzn");
            writer.write(scheduleDataToMinizinc(initialData));
            writer.flush();
            writer.close();

            writer = new FileWriter("pdp_gaussian8000_0_50_reached.dzn");
            writer.write(scheduleDataToMinizinc(data));
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String scheduleDataToMinizinc(ScheduleData scheduleData) {
        StringBuilder builder = new StringBuilder();

        builder.append("bound = ").append(Math.round(scheduleData.getCost())).append(";");
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
                builder.append(Math.round(scheduleData.dist(i - 1, j - 1))).append(",").append(System.lineSeparator());
//                if (i != scheduleData.getSize() && j != scheduleData.getSize() - 1)
//                    builder.append(",").append(System.lineSeparator());
            }
        }

        builder.append("];").append(System.lineSeparator());


        return builder.toString();
    }
}
