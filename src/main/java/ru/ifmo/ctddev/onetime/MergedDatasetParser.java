package ru.ifmo.ctddev.onetime;

import ru.ifmo.ctddev.features.FeatureMaker;
import ru.ifmo.ctddev.features.Moments;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 12.05.2016.
 */
public class MergedDatasetParser {
    public static void main(String[] args) {
        try {
            String filename = "C:/tmp/g/d10 gaussian.txt";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

            List<List<Double>> counted = new ArrayList<>(25);
            List<String> names = new ArrayList<>(25);
            String line = "";
            boolean flag = false;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {

                if (line.charAt(0) == '#') {
                    flag = true;
                    bufferedReader.readLine();
                }

                String name = line;

                line = bufferedReader.readLine();

                List<Double> allDoubles = Arrays.asList(line.split(", "))
                        .stream().map(d -> Double.parseDouble(d))
                        .collect(Collectors.toList());

                if (flag) {
                    counted.get(i).addAll(allDoubles);
                    ++i;
                } else {
                    counted.add(allDoubles);
                    names.add(name);
                }
                bufferedReader.readLine(); // empty
            }

            Moments momentsMaker = new Moments();

            for (int k = 0; k < 25; ++k) {
                System.out.println(names.get(k));
                System.out.println(String.join(", ",
                        counted.get(k).stream()
                                .map(d -> "" + d)
                                .collect(Collectors.toList())));
                momentsMaker.extractStatisticalFeatures(counted.get(k))
                        .stream().forEach(f -> {
                    System.out.println(f.toCSVString());
                });

                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
