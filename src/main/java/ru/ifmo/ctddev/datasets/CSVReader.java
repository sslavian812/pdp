package ru.ifmo.ctddev.datasets;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes CSV records to file. If file does not exist - it will be created;
 * Created by viacheslav on 14.02.2016.
 */
public class CSVReader {

    /**
     * Reads CSV from file and returns list of records.
     * @param csvFile
     * @return
     */
    public static List<String[]> read(String csvFile, String separator) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            List<String[]> records = new ArrayList<>();

            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] ss = line.split(separator);
                records.add(ss);
            }

//            System.out.println("read file " + csvFile + " successful");
//            System.out.println(records.size() + " records found.");

            return records;
        }

    }
}
