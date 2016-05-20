package ru.ifmo.ctddev.ml;

import javafx.util.Pair;
import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.scheduling.ConcurrentStrategyScheduler;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.strategies.Strategy;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

import static util.Util.calcAverage;

/**
 * Created by viacheslav on 02.05.2016.
 */
public class AnswerBuilder {

    private static int times = 10;

    private static ThreadPoolExecutor threadPoolExecutor;
    private static ConcurrentStrategyScheduler scheduler = new ConcurrentStrategyScheduler();
    private static List<Strategy> strategies = StrategyProvider.provideAllStrategies();
    private static BlockingQueue<Pair<String, Future<List<Double>>>> queue = new ArrayBlockingQueue<>(500);

    private static final String targetDir = "./src/main/resources/ml";

    public static void main(String[] args) {
        File dir = new File(targetDir + "/data");
        File[] directoryListing = dir.listFiles();
        threadPoolExecutor = new ThreadPoolExecutor(4, 8, 15, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(500));
        long startTime = System.currentTimeMillis();

        writeAnswersInSeparateThread();

        if (directoryListing != null) {
            for (File child : directoryListing) {
                // Do something with child
                ScheduleData data = DatasetProvider.getFromFile(child.getAbsolutePath());
                initTasks(data, child.getName().split("\\.")[0]);

            }
            try {
                queue.put(new Pair<>("stop", null));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
            System.err.println("not a directory.");
        }

        threadPoolExecutor.shutdown();


        System.out.println();
        System.out.println("answers generation is complete.");
        System.out.println("time spent: " + (System.currentTimeMillis() - startTime) / 1000 + " s");

    }

    private static void initTasks(ScheduleData data, String datasetName) {
        try {
            FileWriter writer = new FileWriter(targetDir + "/answers/" + datasetName + ".csv", false);
            writer.write("S=[LO,CX,DB,PX,RB];result");
            writer.write(System.lineSeparator());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("task initialized for file " + datasetName);

        for (Strategy s : strategies) {
            Future<List<Double>> ratios = null;
            try {
                ratios = threadPoolExecutor.submit(new NTimeStatelessRepeater(
                        scheduler, s, data.clone(), times, 12 * data.getOrdersNum() * data.getOrdersNum()));
            } catch (RejectedExecutionException e) {
                System.err.println("rejected: " + datasetName);
                e.printStackTrace();
            }
            try {
                queue.put(new Pair<String, Future<List<Double>>>(
                        datasetName + "#" + s.getDisplayName(), ratios));
//                        datasetName + "#" + StrategyProvider.getProbabilities(s), ratios));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeAnswersInSeparateThread() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Pair<String, Future<List<Double>>> pair = queue.take();
                        String name = pair.getKey().split("#")[0];
                        if (name.equals("stop"))
                            return;
                        String strategy = pair.getKey().split("#")[1];
                        List<Double> ratios = pair.getValue().get();
                        double result = calcAverage(ratios);
                        FileWriter writer = new FileWriter(targetDir + "/answers/" + name + ".csv", true);
                        writer.write(strategy + ";" + result);
                        writer.write(System.lineSeparator());
                        writer.flush();
                        writer.close();
                        System.out.println(name + " - " + strategy + " - OK");
                    } catch (ExecutionException | IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
