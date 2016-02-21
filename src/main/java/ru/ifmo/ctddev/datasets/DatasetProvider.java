package ru.ifmo.ctddev.datasets;

import ru.ifmo.ctddev.Config;
import ru.ifmo.ctddev.gui.App;
import ru.ifmo.ctddev.scheduling.ScheduleData;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class provides custom datasets from the whole one.
 * The whole dataset is stored in a file, whose path is written in the {@code application.properties} file.
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
     * get some sub-dataset of the whole.
     *
     * @param size
     * @param position
     * @param direction
     * @param outputFilePath
     * @return
     */
    public static ScheduleData getDataset(int size, int position, Direction direction, String outputFilePath) {
        return getDataset(size, position, direction, false, outputFilePath);
    }

    /**
     * get from head.
     *
     * @param size
     * @param direction
     * @param outputFilePath
     * @return
     */
    public static ScheduleData getShuffledHeadDataset(int size, Direction direction, String outputFilePath) {
        return getDataset(size, 0, direction, true, outputFilePath);
    }

    /**
     * Provides dataset from file.
     *
     * @param file path to file.
     * @return dataset from file.
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
     * This methos provides a dataset of specified size according to other parameters.
     * If {@code outputFilePath} is specified(is not null),
     * then this dataset will be alse written to the file.
     *
     * @param size           size of dataset(size of orders).
     * @param position       position of start
     * @param direction      if LEFT: src is the left column, and des is the right column. RIGHT: vice versa. DEFAULT: as in dataset-file specified.
     * @param shuffled       if true: all the dataset will be shuffled beforehand. if false: first {@code size} orders will be taken.
     * @param outputFilePath if not null, the dataset will be written to this file (if not exists - create this file).
     * @return
     */
    private static ScheduleData getDataset(int size, int position, Direction direction, boolean shuffled, String outputFilePath) {
        try {
            String resourceName = Config.datasetPath;
            List<String[]> orders = CSVReader.read(App.class.getClassLoader().getResource(resourceName).getFile(), ",");

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
}
