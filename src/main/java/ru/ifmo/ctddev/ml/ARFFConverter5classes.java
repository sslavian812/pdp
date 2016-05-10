package ru.ifmo.ctddev.ml;

import ru.ifmo.ctddev.datasets.CSVReader;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;
import util.Score;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 10.05.2016.
 */
public class ARFFConverter5classes {

    public static void main(String[] args) throws IOException {
        String targetDir = "src/main/resources/ml";
        String features = "/features";
        String answers = "/answers";
        String train = "/train5";
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

        Map<String, String> strategyToClass = StrategyProvider.getTop5();

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

                writer.write("@ATTRIBUTE class {1,2,3,4,5}");
                writer.newLine();
                writer.write("@DATA");
                writer.newLine();
                firstTime = false;
            }

            List<String> featuresDouble = featuresCSV.stream()
                    .map(f -> f[1]).collect(Collectors.toList());


            for (Map.Entry<String, String> e : strategyToClass.entrySet()) {

            }

            List<Score> scores = targetCSV.stream().filter(ss -> {return strategyToClass.containsKey(ss[0]);})
                    .map(ss -> new Score(ss[0], Double.parseDouble(ss[1])))
                    .sorted().collect(Collectors.toList());

            Collections.reverse(scores);
            scores.get(0);


//            for (String[] ss : targetCSV) {
//                if(strategyToClass.containsKey(ss[0]))
//                    scores.add(new Score(ss[0], Double.parseDouble(ss[1])));
//            }


            featuresDouble.add(strategyToClass.get(scores.get(0).name));

            writer.write(String.join(",", featuresDouble));
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }
}
