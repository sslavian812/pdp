package ru.ifmo.ctddev.experiments;

import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.features.Moments;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.genetics.GeneticsSchedulerFactory;
import ru.ifmo.ctddev.scheduling.strategies.Strategy;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static util.Util.calcAverage;

/**
 * Created by viacheslav on 29.05.2016.
 */
public class GeneticsRunnerGraphics {

    public static final int times = 1;
    public static final int size = 50;
    public static final int n_datasets = 1;
    public static final int from = 50;


    public static void main(String[] args) {

        GeneticsSchedulerFactory factory = GeneticsSchedulerFactory.getInstance();

        List<Scheduler> schedulers = new ArrayList<>();


        for (Strategy s : StrategyProvider.provideAllStrategies()) {
            schedulers.addAll(factory.getGraphickMakingSchedulers(s, size));
        }

        int start = from;
        List<ScheduleData> datasets = new ArrayList<>();

        String filename = "gaussian8000.csv";

        while (start + size <= from + (size * n_datasets)) {
            datasets.add(DatasetProvider.getDataset(size, start, DatasetProvider.Direction.RIGHT,
                    filename, null));
            start += size;
        }
        System.out.println(filename + ": \n\n");


        long startTime = System.currentTimeMillis();

        NDataSetsNTimesScheduleTester nDataSetsNTimesScheduleTester =
                new NDataSetsNTimesScheduleTester(null, datasets, times);

        Moments moments = new Moments();


        for (Scheduler scheduler : schedulers) {

            long time = System.currentTimeMillis();
            List<List<Double>> ratios = nDataSetsNTimesScheduleTester.setScheduler(scheduler).call();


            System.out.println(scheduler.getComment());
            System.out.println("Datasets: " + datasets.size() + ". Size of one dataset: " + size);
            System.out.println("# total time: " + (System.currentTimeMillis() - time) + " ms =~ " + (System.currentTimeMillis() - time) / 1000 + " s");

            List<Double> averagePerNTimes = new ArrayList<>();
            int i = 0;
            for (List<Double> list : ratios) {
                String indent = "    ";
                System.out.println(indent + "dataset: " + i + "-" + (i + size) + ": ");
                i += size;
                System.out.println(indent + "ratios=" + Arrays.toString(list.toArray()));
                averagePerNTimes.add(calcAverage(list));
                System.out.println(indent + "average ratio for i-th dataset: "
                        + averagePerNTimes.get(averagePerNTimes.size() - 1));
//                System.out.println(indent + moments.extractStatisticalFeatures(list).get(3).toCSVString());
                System.out.println();
            }

            System.out.println("average for scheduler: " + calcAverage(averagePerNTimes));

            System.out.println("==================================================");
            System.out.println();
        }

        System.out.println("time spent: " + (System.currentTimeMillis() - startTime) / 1000 + " s");
    }
}
