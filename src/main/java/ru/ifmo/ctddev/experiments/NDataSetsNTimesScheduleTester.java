package ru.ifmo.ctddev.experiments;

import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;

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

    public NDataSetsNTimesScheduleTester setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
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
//        if (executor == null)
//            executor = new ThreadPoolExecutor(4, 10, 500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

//        List<Future<List<Double>>> futures = new ArrayList<>();

        NTimeScheduleTester  nTimeTester = new NTimeScheduleTester(scheduler, null, times);

        int i=0;
        for (ScheduleData iData : data) {
            System.out.println("/// dataset processing: " + i);
            ++i;
            ScheduleData currentData = iData.clone();

            List<Double> ratios = nTimeTester.setData(currentData).call();
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

