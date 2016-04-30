package ru.ifmo.ctddev.datasets;

import ru.ifmo.ctddev.generate.DatasetGenerator;
import ru.ifmo.ctddev.generate.GausianDatasetGeneratorImpl;
import ru.ifmo.ctddev.generate.UniformDatasetGeneratorImpl;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This class generates uniform and gaussian datasets to files.
 * Created by viacheslav on 30.04.2016.
 */
public class GenerateDatasetToFile {
    public static final String header = "id1,x1,y1,id2,x2,y2,direction";

    public static void main(String[] args) {


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                generateAndWirte("uniform8000.csv", new UniformDatasetGeneratorImpl());
            }
        });
        t.start();

        generateAndWirte("gaussian8000.csv", new GausianDatasetGeneratorImpl());
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void generateAndWirte(String fileName, DatasetGenerator generator) {
        DatasetProvider datasetProvider = new DatasetProvider();
        List<Point2D.Double> points = datasetProvider.getGeneratePoints(8000, generator);

        List<String[]> records = new ArrayList<String[]>(8001);
        records.add(header.split(","));
        for (int i = 0; i < points.size(); i += 2) {
            records.add(new String[]{
                    "" + i / 2,
                    "" + points.get(i).getX(), "" + points.get(i).getY(),
                    "" + i / 2,
                    "" + points.get(i + 1).getX(), "" + points.get(i + 1).getY(),
                    ">"   // direction. for generated datasets it's not used
            });
        }

        CSVWriter.write("C:/tmp/" + fileName, records);
    }
}
