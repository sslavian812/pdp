package ru.ifmo.ctddev.onetime;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static util.Util.calcAverage;

/**
 * Created by viacheslav on 12.05.2016.
 */
public class LogParser10per10datasets {
    public static void main(String[] args) {
        try {
            String filename = "C:/tmp/g/d10 uniform.txt";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

            List<List<Double>> counted = new ArrayList<>(25);
            List<String> names = new ArrayList<>(25);

            for (int k = 0; k < 25; ++k) {
                counted.add(new ArrayList<>());
            }
            String line = "";
            boolean flag = false;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {

                if (line.charAt(0) == '#') {
                    flag = true;
                    i =0;
                    bufferedReader.readLine();
                }

                String name = line;

                line = bufferedReader.readLine();

                for (int d = 0; d < 5; ++d) {
                    double average = calcAverage(Arrays.asList(line.split(", ")).subList(10 * d, 10 * (d + 1))
                            .stream().map(doub -> Double.parseDouble(doub))
                            .collect(Collectors.toList()));

                    counted.get(i).add(average);
                }

                if (!flag) {
                    names.add(name);
                }
                ++i;

                bufferedReader.readLine(); // empty
            }


            for (int k = 0; k < 25; ++k) {
                System.out.println(names.get(k));
                System.out.println(String.join(", ",
                        counted.get(k).stream()
                                .map(d -> "" + d)
                                .collect(Collectors.toList())));
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
