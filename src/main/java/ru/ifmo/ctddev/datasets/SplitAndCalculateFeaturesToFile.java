package ru.ifmo.ctddev.datasets;

import ru.ifmo.ctddev.features.Feature;
import ru.ifmo.ctddev.features.FeatureMaker;
import ru.ifmo.ctddev.scheduling.ScheduleData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslav on 01.05.2016.
 */
public class SplitAndCalculateFeaturesToFile {


    private static FeatureMaker featureMaker = new FeatureMaker();


    public static void main(String[] args) {

        dealWithFile("uniform8000.csv");
        dealWithFile("gaussian8000.csv");
        dealWithFile("taxi8000.csv");
    }

    private static void dealWithFile(String file) {
        try {
            int start = 0;
            int size = 50;

            while (start + size <= 8000) {
                String newFileName = "./src/main/resources/ml/" + file + "_" + start + "_" + (start + size);
                ScheduleData scheduleData = DatasetProvider.getDataset(
                        size, start, DatasetProvider.Direction.RIGHT,
                        file, newFileName + ".csv");

                BufferedWriter writer = new BufferedWriter(new FileWriter(
                        newFileName + "_features.csv", false));

                writer.write(Feature.getHeadOfCSV());
                writer.newLine();

                featureMaker.getFeatures(scheduleData).stream().map(f -> f.toCSVString())
                        .forEach(f -> {
                            try {
                                writer.write(f);
                                writer.newLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                writer.flush();
                writer.close();

                start += size;
                System.out.println(newFileName + " - OK");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
