package ru.ifmo.ctddev.datasets;

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
    enum Direction {
        LEFT,
        RIGHT,
        DEFAULT
    }


    /**
     * This methos provides a dataset of specified size according to other parameters.
     * If {@code outputFilePath} is specified(is not null),
     * then this dataset will be alse written to the file.
     *
     * @param size           size of dataset(size of orders).
     * @param direction      if LEFT: src is the left column, and des is the right column. RIGHT: vice versa. DEFAULT: as in dataset-file specified.
     * @param shuffled       if true: all the dataset will be shuffled beforehand. if false: first {@code size} orders will be taken.
     * @param outputFilePath if not null, the dataset will be written to this file (if not exists - create this file).
     * @return
     */
    public static ScheduleData getDataset(int size, Direction direction, boolean shuffled, String outputFilePath) {
        try {
            String resourceName = "";
            List<String[]> orders = CSVReader.read(App.class.getClassLoader().getResource(resourceName).getFile(), ",");

            if (shuffled) {
                Collections.shuffle(orders, new Random());
            }

            orders = orders.subList(0, size);

            List<Point2D.Double> lhs = new ArrayList<>(size);
            List<Point2D.Double> rhs = new ArrayList<>(size);

            orders.stream().forEach(ss -> {
                Point2D.Double left = new Point2D.Double(Double.parseDouble(ss[1]), Double.parseDouble(ss[2]));
                Point2D.Double right = new Point2D.Double(Double.parseDouble(ss[4]), Double.parseDouble(ss[5]));

                if (direction.equals(Direction.DEFAULT)) {
                    if (ss[6].equals(">")) {
                        lhs.add(left);
                        rhs.add(right);
                    } else {
                        rhs.add(left);
                        lhs.add(right);
                    }
                } else {
                    lhs.add(left);
                    rhs.add(right);
                }
            });

            ScheduleData scheduleData;
            if (direction.equals(Direction.LEFT)) {
                rhs.addAll(lhs);
                scheduleData = new ScheduleData(rhs);
            } else {
                lhs.addAll(rhs);
                scheduleData = new ScheduleData(lhs);
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
