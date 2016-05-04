package ru.ifmo.ctddev.ml;

import weka.classifiers.trees.J48;
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
                    new FileReader("src/main/resources/ml/train.arff"));
            Instances data = new Instances(reader);
            reader.close();
            data.setClassIndex(data.numAttributes() - 1);

            String[] options = {"-U"};
            J48 tree = new J48();         // new instance of tree
            tree.setOptions(options);     // set the options
            tree.buildClassifier(data);   // build classifier


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
