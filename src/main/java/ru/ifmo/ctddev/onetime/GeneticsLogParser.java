package ru.ifmo.ctddev.onetime;

import weka.core.pmml.Array;

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
public class GeneticsLogParser {
    public static void main(String[] args) {
        try {
            String filename = "C:/tmp/g/genetics uniform LONG 2.txt";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.charAt(0) != '=')
                    break;
                bufferedReader.readLine();

                String name = bufferedReader.readLine();
                bufferedReader.readLine();

                List<Double> allDoubles = new ArrayList<>();
                for (int i = 0; i < 5; ++i) {
                    bufferedReader.readLine(); // empty.
                    bufferedReader.readLine(); // dataset
                    line = bufferedReader.readLine(); // ratios
                    allDoubles.addAll(
                            Arrays.asList(line.substring(12, line.length() - 1).split(", "))
                                    .stream().map(d -> Double.parseDouble(d))
                                    .collect(Collectors.toList()));
                    bufferedReader.readLine(); // average
                }
                bufferedReader.readLine(); // empty

                bufferedReader.readLine(); // average

                System.out.println(name);
                System.out.println(String.join(", ",
                        allDoubles.stream()
                                .map(d -> "" + d)
                                .collect(Collectors.toList())));
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
