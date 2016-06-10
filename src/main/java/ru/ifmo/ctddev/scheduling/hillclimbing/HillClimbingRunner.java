package ru.ifmo.ctddev.scheduling.hillclimbing;

import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.experiments.NDataSetsNTimesScheduleTester;
import ru.ifmo.ctddev.features.Moments;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static util.Util.calcAverage;

/**
 * Created by viacheslav on 09.06.2016.
 */
public class HillClimbingRunner {

    public static final int times = 20; // 20
    public static final int size = 50; // 50
    public static final int from = 0;
    public static final int n_datasets = 10; //10



    public static void main(String[] args) {
        int start = 0;
        List<ScheduleData> datasets = new ArrayList<>();

        String prefix = "gaussian";

        System.out.println(prefix + "8000.csv: \n\n");
        while (from + start + size <= from + (size * n_datasets)) {
            datasets.add(DatasetProvider.getDataset(size, from + start, DatasetProvider.Direction.RIGHT,
                    prefix + "8000.csv", null));
            start += size;
        }


        List<Scheduler> schedulers = new ArrayList<Scheduler>(1);
        schedulers.add(new HillClimbingScheduler());


        // warmup:
        System.out.println("wamup");
        for (Scheduler scheduler : schedulers) {
            ScheduleData data = datasets.get(0).clone();
            scheduler.schedule(data);
        }
        System.out.println("end warmup");


        long startTime = System.currentTimeMillis();

        NDataSetsNTimesScheduleTester nDataSetsNTimesScheduleTester = new NDataSetsNTimesScheduleTester(null, datasets, times);
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
                System.out.println(indent + "dataset: " + (from + i) + "-" + (from + i + size) + ": ");
                i += size;
                System.out.println(indent + "ratios=" + Arrays.toString(list.toArray()));
                averagePerNTimes.add(calcAverage(list));
                System.out.println(indent + "average ratio for i-th dataset: "
                        + averagePerNTimes.get(averagePerNTimes.size() - 1));
                System.out.println(indent + moments.extractStatisticalFeatures(list).get(3).toCSVString());
                System.out.println();
            }

            System.out.println("average for scheduler: " + calcAverage(averagePerNTimes));

            System.out.println("==================================================");
            System.out.println();
        }


        System.out.println("time spent: " + (System.currentTimeMillis() - startTime) / 1000 + " s");



    }
}
