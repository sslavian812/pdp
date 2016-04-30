package ru.ifmo.ctddev.datasets;

import ru.ifmo.ctddev.Config;
import ru.ifmo.ctddev.generate.DatasetGenerator;
import ru.ifmo.ctddev.generate.GausianDatasetGeneratorImpl;
import ru.ifmo.ctddev.generate.UniformDatasetGeneratorImpl;
import ru.ifmo.ctddev.gui.App;
import ru.ifmo.ctddev.scheduling.ScheduleData;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * This class provides custom datasets from the whole one.
 * The whole original is stored in a file, whose path is written in the {@code application.properties} file.
 * <p>
 * Created by viacheslav on 14.02.2016.
 */
public class DatasetProvider {
    public enum Direction {
        LEFT,
        RIGHT,
        DEFAULT
    }

    /**
     * get some sub-original of the whole.
     *
     * @param size
     * @param position
     * @param direction
     * @param outputFilePath
     * @return
     */
    public static ScheduleData getDataset(int size, int position, Direction direction,
                                          String inputFilePath, String outputFilePath) {
        return getDataset(size, position, direction, false, inputFilePath, outputFilePath);
    }

    /**
     * get from head.
     *
     * @param size
     * @param direction
     * @param outputFilePath
     * @return
     */
    public static ScheduleData getShuffledHeadDataset(int size, Direction direction, String inputFilePath, String outputFilePath) {
        return getDataset(size, 0, direction, true, inputFilePath, outputFilePath);
    }

    /**
     * Provides original from file.
     *
     * @param file path to file.
     * @return original from file.
     */
    public static ScheduleData getFromFile(String file) {
        try {
            List<String[]> orders = CSVReader.read(file, ",");
            int size = orders.size();

            List<Point2D.Double> lhs = new ArrayList<>(size);
            List<Point2D.Double> rhs = new ArrayList<>(size);
            List<Integer> lhsIds = new ArrayList<>(size);
            List<Integer> rhsIds = new ArrayList<>(size);

            orders.stream().forEach(ss -> {
                Point2D.Double left = new Point2D.Double(Double.parseDouble(ss[1]), Double.parseDouble(ss[2]));
                Point2D.Double right = new Point2D.Double(Double.parseDouble(ss[4]), Double.parseDouble(ss[5]));

                if (ss[6].equals("<")) {
                    rhs.add(left);
                    lhs.add(right);
                    rhsIds.add(Integer.parseInt(ss[0]));
                    lhsIds.add(Integer.parseInt(ss[3]));
                } else {
                    lhs.add(left);
                    rhs.add(right);
                    lhsIds.add(Integer.parseInt(ss[0]));
                    rhsIds.add(Integer.parseInt(ss[3]));
                }
            });

            ScheduleData scheduleData;
            lhs.addAll(rhs);
            lhsIds.addAll(rhsIds);
            scheduleData = new ScheduleData(lhs);
            scheduleData.setIds(lhsIds);

            return scheduleData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This methos provides a original of specified size according to other parameters.
     * If {@code outputFilePath} is specified(is not null),
     * then this original will be alse written to the file.
     *
     * @param size           size of original(size of orders).
     * @param position       position of start
     * @param direction      if LEFT: src is the left column, and des is the right column. RIGHT: vice versa. DEFAULT: as in original-file specified.
     * @param shuffled       if true: all the original will be shuffled beforehand. if false: first {@code size} orders will be taken.
     * @param outputFilePath if not null, the original will be written to this file (if not exists - create this file).
     * @return
     */
    private static ScheduleData getDataset(int size, int position, Direction direction,
                                           boolean shuffled,
                                           String inputFilePath, String outputFilePath) {
        try {
            String resourceName = Config.datasetPath;
            List<String[]> orders;

            try {
                orders = CSVReader.read(App.class.getClassLoader().getResource(resourceName).getFile(), ",");
            } catch (FileNotFoundException e) {
                orders = CSVReader.read("./datasets/" + inputFilePath, ",");
            }

            if (shuffled) {
                Collections.shuffle(orders, new Random());
            }

            orders = orders.subList(position, Math.min(position + size, orders.size()));

            List<Point2D.Double> lhs = new ArrayList<>(size);
            List<Point2D.Double> rhs = new ArrayList<>(size);
            List<Integer> lhsIds = new ArrayList<>(size);
            List<Integer> rhsIds = new ArrayList<>(size);


            orders.stream().forEach(ss -> {
                Point2D.Double left = new Point2D.Double(Double.parseDouble(ss[1]), Double.parseDouble(ss[2]));
                Point2D.Double right = new Point2D.Double(Double.parseDouble(ss[4]), Double.parseDouble(ss[5]));


                if (direction.equals(Direction.DEFAULT) && ss[6].equals("<")) {
                    rhs.add(left);
                    lhs.add(right);
                    rhsIds.add(Integer.parseInt(ss[0]));
                    lhsIds.add(Integer.parseInt(ss[3]));
                } else {
                    lhs.add(left);
                    rhs.add(right);
                    lhsIds.add(Integer.parseInt(ss[0]));
                    rhsIds.add(Integer.parseInt(ss[3]));
                }
            });

            ScheduleData scheduleData;
            if (direction.equals(Direction.LEFT)) {
                rhs.addAll(lhs);
                rhsIds.addAll(lhsIds);
                scheduleData = new ScheduleData(rhs);
                scheduleData.setIds(rhsIds);
            } else {
                lhs.addAll(rhs);
                lhsIds.addAll(rhsIds);
                scheduleData = new ScheduleData(lhs);
                scheduleData.setIds(lhsIds);
            }


            if (outputFilePath != null)
                CSVWriter.write(outputFilePath, orders);

            return scheduleData;

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Point2D.Double> getGeneratePoints(int pairsNumber, DatasetGenerator generator) {
        List<Integer> sizes = new ArrayList<Integer>(1);
        sizes.add(pairsNumber);
        List<Point2D> centers = new ArrayList<>(1);
        centers.add(new Point2D.Double(0.0, 0.0));

        Point2D lu = new Point2D.Double(-50.0, 50.0);
        Point2D rd = new Point2D.Double(50.0, -50.0);
        List<Point2D.Double> points = generator.generate(
                lu, rd,
                sizes, centers);

        return points;
    }

    public ScheduleData getGaussianDistributedDataset(int pairsNumber) {
        return new ScheduleData(getGeneratePoints(pairsNumber, new GausianDatasetGeneratorImpl()));
    }

    public ScheduleData getUniformDistributedDataset(int pairsNumber) {
        return new ScheduleData(getGeneratePoints(pairsNumber, new UniformDatasetGeneratorImpl()));
    }
}
