package ru.ifmo.ctddev.datasets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


/**
 * Writes CSV records to file. If file does not exist - it will be created;
 * Created by viacheslav on 14.02.2016.
 */
public class CSVWriter {

    public static void write(String filename, List<String[]> records) {
        BufferedWriter bw = null;
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            bw = new BufferedWriter(new FileWriter(filename));

            for (String[] ss : records) {
                bw.write(String.join(",", ss));
                bw.newLine();
            }

            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
