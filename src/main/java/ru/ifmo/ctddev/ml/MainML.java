package ru.ifmo.ctddev.ml;

import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by viacheslav on 03.05.2016.
 */
public class MainML {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/resources/tmp/data.arff"));
            Instances data = new Instances(reader);
            reader.close();
            data.setClassIndex(data.numAttributes() - 1);



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
