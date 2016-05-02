package ru.ifmo.ctddev.ml;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.util.Pair;
import ru.ifmo.ctddev.datasets.DatasetProvider;
import ru.ifmo.ctddev.scheduling.ConcurrentStrategyScheduler;
import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Strategy;
import ru.ifmo.ctddev.scheduling.StrategyProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by viacheslav on 02.05.2016.
 */
public class AsnwerBuilder {

    private static ThreadPoolExecutor threadPoolExecutor;
    private static ConcurrentStrategyScheduler scheduler = new ConcurrentStrategyScheduler();
    private static List<Strategy> strategies = StrategyProvider.provideAllStrategies();
    private static BlockingQueue<Pair<String, Future<List<Double>>>> queue = new ArrayBlockingQueue<>(1000);

    private static final String targetDir = "src/resources/ml/data/";

    public static void main(String[] args) {
        File dir = new File(targetDir);
        File[] directoryListing = dir.listFiles();
        threadPoolExecutor = new ThreadPoolExecutor(4, 8, 10, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(50));
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
            FileWriter writer = new FileWriter(targetDir + "answers/" + datasetName + ".csv", false);
            writer.write("strategyName;Probabilities;result");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("task initialized for file " + datasetName);

        for (Strategy s : strategies) {
            Future<List<Double>> ratios = threadPoolExecutor.submit(new NTimeStatelessRepeater(
                    scheduler, s, data.clone(), 1, 12 * data.getOrdersNum() * data.getOrdersNum()));
            try {
                queue.put(new Pair<String, Future<List<Double>>>(
                        datasetName + "#" + StrategyProvider.getNameAndProbabilities(s), ratios));
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
                        String strategy = pair.getKey().split("#")[1];
                        if (name.equals("stop"))
                            return;
                        List<Double> ratios = pair.getValue().get();
                        double result = calcAverage(ratios);
                        FileWriter writer = new FileWriter(targetDir + "answers/" + name + ".csv", true);
                        writer.write(strategy + ";" + result);
                        writer.write(System.lineSeparator());
                        writer.flush();
                        writer.close();
                    } catch (ExecutionException | IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private static Double calcAverage(List<Double> list) {
        double acc = 0;
        for (Double x : list) {
            acc += x;
        }
        acc /= list.size();
        return acc;
    }
}
