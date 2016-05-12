package ru.ifmo.ctddev.onetime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 12.05.2016.
 */
public class WolframParser {
    public static void main(String[] args) {
        try {
            String filename = "C:/tmp/w/gaussian.txt";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                int xsStart = line.indexOf("xs = {");
                System.out.println(line.substring(0, xsStart));

                String fitCallsString = line.substring(xsStart + 6, line.length() - 2);
                line = bufferedReader.readLine();
                String ratiosString = line.substring(6, line.length() - 2);

                List<Double> doubleList = Arrays.stream(ratiosString.split(", ")).map(d -> Double.parseDouble(d))
                        .collect(Collectors.toList());

                int maxpos = 0;
                for (int i = 0; i < doubleList.size(); ++i) {
                    if (doubleList.get(i) > doubleList.get(maxpos)) {
                        maxpos = i;
                    }
                }

                System.out.println("best ratio:" + doubleList.get(maxpos)
                        + ". at fit call:" + fitCallsString.split(", ")[maxpos]);
                System.out.println();

                bufferedReader.readLine();
                bufferedReader.readLine();
                bufferedReader.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
