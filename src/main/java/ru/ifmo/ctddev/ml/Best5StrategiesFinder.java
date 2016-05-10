package ru.ifmo.ctddev.ml;

import util.Score;
import ru.ifmo.ctddev.datasets.CSVReader;
import ru.ifmo.ctddev.scheduling.strategies.StrategyProvider;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by viacheslav on 10.05.2016.
 */
public class Best5StrategiesFinder {


    public static void main(String[] args) {

        try {
            String targetDir = "src/main/resources/ml";
            String answers = "/answers";

            Map<String, Integer> strategiesToFrequency = new HashMap<>();

            StrategyProvider.provideAllStrategies().stream()
                    .forEach(s -> strategiesToFrequency.put(s.getDisplayName(), 0));

            File dir = new File(targetDir + answers);
            File[] directoryListing = dir.listFiles();

            if (directoryListing == null) {
                System.err.println("not a directory to list files");
                return;
            }

            List<Score> scores = new ArrayList<>();
            for (File file : directoryListing) {
                System.out.println("file: " + file.getName() + " - OK");
                List<String[]> answersCSV = CSVReader.read(file.getAbsolutePath(), ";");

                scores.clear();
                for (String[] ss : answersCSV) {
                    scores.add(new Score(ss[0], Double.parseDouble(ss[1])));
                }

                Collections.sort(scores);
                Collections.reverse(scores);

                for (int i = 0; i < 5; ++i) {
                    strategiesToFrequency.put(scores.get(i).name,
                            strategiesToFrequency.get(scores.get(i).name) + 1);
                }
            }


            scores.clear();

            System.out.println("for each strategy number of being int top 5");
            for (Map.Entry<String, Integer> entry : strategiesToFrequency.entrySet()) {
                System.out.println(entry.getKey() + ";" + entry.getValue());
                scores.add(new Score(entry.getKey(), entry.getValue()));
            }


            System.out.println();
            System.out.println("================");
            System.out.println("best 5:");
            System.out.println("strategy name;reached score;class label");

            Collections.sort(scores);
            Collections.reverse(scores);
            for (int i = 0; i < 5; ++i) {
                System.out.println(scores.get(i).name + ";" + scores.get(i).score + ";" + (i + 1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
