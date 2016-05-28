package ru.ifmo.ctddev.onetime;

import com.sun.org.apache.xpath.internal.SourceTree;
import util.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 25.05.2016.
 */
public class RecalcLogParser {
    public static void main(String[] args) {
        try {
            String filename = "C:/tmp/cur/taxi.txt";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

            String line = "";

            List<List<String>> data = new ArrayList<>(5);
            for (int i = 0; i < 5; ++i) {
                data.add(new ArrayList<>());
            }

            bufferedReader.readLine(); // file header
            while (true) {

                for (int g = 0; g < 5; ++g) { // genetics
                    line = bufferedReader.readLine(); // ======== or stop
                    if (line.equals("stop"))
                        break;

                    line = bufferedReader.readLine(); // empty
                    String name = bufferedReader.readLine();
                    line = bufferedReader.readLine(); // dataset

                    List<Double> allDoubles = new ArrayList<>();

                    for (int i = 0; i < 10; ++i) {
                        line = bufferedReader.readLine(); // total or empty.
                        line = bufferedReader.readLine(); // dataset
                        line = bufferedReader.readLine(); // ratios
                        line = bufferedReader.readLine(); // average
                        String dispersion = bufferedReader.readLine().split(",")[1];
                        allDoubles.add(Double.parseDouble(dispersion));
                    }
                    bufferedReader.readLine(); // empty

                    String average = bufferedReader.readLine(); // average
                    average = average.substring(average.indexOf("0."));


                    data.get(g).add(name + "#" + average);

                    System.out.println(name + " - dispersion - " + Util.calcAverage(allDoubles));
                }
                if (line.equals("stop"))
                    break;
            }

            System.out.println();
            System.out.println();

            int k = 0;
            for (int i = 0; i < 5; ++i) {

                System.out.println();
                System.out.println("---------G=" + i);
                System.out.println();
                k = 0;
                for (String s : data.get(i)) {
                    if (k == 10)
                        System.out.println();
                    System.out.println(s.split("#")[0]);
                    ++k;
                }
                System.out.println();
                k = 0;
                for (String s : data.get(i)) {
                    if (k == 10)
                        System.out.println();
                    System.out.println(new DecimalFormat("#0.000").format(Double.parseDouble(s.split("#")[1])));
                    ++k;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
