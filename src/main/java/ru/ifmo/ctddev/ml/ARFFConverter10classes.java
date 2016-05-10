package ru.ifmo.ctddev.ml;

import ru.ifmo.ctddev.datasets.CSVReader;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 03.05.2016.
 */
public class ARFFConverter10classes {
    public static void main(String[] args) throws IOException {
        String targetDir = "src/main/resources/ml";
        String features = "/features";
        String answers = "/answers";
        String train = "/train";
        String dARFF = ".arff";

        File dir = new File(targetDir + features);
        File[] directoryListing = dir.listFiles();
        if (directoryListing == null) {
            System.err.println("not a directory to list files");
            return;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(
                targetDir + train + dARFF, false));

        writer.write("%features,targetClass");
        writer.newLine();
        writer.write("@RELATION pdp");
        writer.newLine();

        boolean firstTime = true;

        Map<String, String> strategyToClass = StrategyProvider.getStrategiesMapToClasses();
        for (File file : directoryListing) {
            List<String[]> featuresCSV = CSVReader.read(file.getAbsolutePath(), ",");
            List<String[]> targetCSV = CSVReader.read(targetDir + answers + "/" + file.getName(), ";");

            if (firstTime) {
                featuresCSV.stream().forEach(f -> {
                    try {
                        writer.write("@ATTRIBUTE " + f[0] + " NUMERIC");
                        writer.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                writer.write("@ATTRIBUTE class {1,2,3,4,5,6,7,8,9,10}");
                writer.newLine();
                writer.write("@DATA");
                writer.newLine();
                firstTime = false;
            }

            List<String> featuredDouble = featuresCSV.stream()
                    .map(f -> f[1]).collect(Collectors.toList());

            String bestStrategy = targetCSV.get(0)[0];
            Double bestResult = Double.parseDouble(targetCSV.get(0)[1]);

            for (int i = 1; i < targetCSV.size(); ++i) {
                if (Double.parseDouble(targetCSV.get(i)[1]) > bestResult) {
                    bestResult = Double.parseDouble(targetCSV.get(i)[1]);
                    bestStrategy = targetCSV.get(i)[0];
                }
            }
            featuredDouble.add(strategyToClass.get(bestStrategy));

            writer.write(String.join(",", featuredDouble));
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }
}
