package ru.ifmo.ctddev.experiments;

import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by viacheslav on 30.04.2016.
 */
public class NDataSetsNTimesScheduleTester implements Callable<List<List<Double>>> {
    private Scheduler scheduler;
    private List<ScheduleData> data;
    private int times;

    private ThreadPoolExecutor executor; // optional

    public NDataSetsNTimesScheduleTester(Scheduler scheduler, List<ScheduleData> data, int times) {
        this.scheduler = scheduler;
        this.data = data;
        this.times = times;
    }

    public NDataSetsNTimesScheduleTester setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Executes scheduling procedure for each dataset in {@code data} {@code times} times.
     *
     * @return for each dataset returns list of optimisation ratios.
     */
    @Override
    public List<List<Double>> call() {
        List<List<Double>> ratiosPerDataset = new ArrayList<>();
        if (executor == null)
            executor = new ThreadPoolExecutor(4, 10, 500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

        List<Future<List<Double>>> futures = new ArrayList<>();

        for (ScheduleData iData : data) {
            ScheduleData currentData = iData.clone();

            List<Double> ratios = new NTimeScheduleTester(scheduler, currentData, times).call();
            ratiosPerDataset.add(ratios);

// todo uncomment this if you wand multithreading
//            futures.add(executor.submit(new NTimeScheduleTester(scheduler, currentData, times)));
//        }
//
//        for (Future<List<Double>> f : futures) {
//
//            try {
//                List<Double> ratios = f.get();
//                ratiosPerDataset.add(ratios);
//            } catch (ExecutionException | InterruptedException e) {
//                e.printStackTrace();
//                ratiosPerDataset.add(new ArrayList<>());
//            }
        }
        return ratiosPerDataset;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}

