package ru.ifmo.ctddev;

import ru.ifmo.ctddev.gui.App;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by viacheslav on 14.02.2016.
 */
public class Config {

    public static boolean enableConstraintPairing  = false;
    public static boolean enableConstraintCapacity = false;
    public static boolean showTrace                = false;
    public static boolean showInfo                 = false;
    public static boolean distanceCartesian        = false;
    public static boolean distanceMatrix           = false;
    public static int     maxCapacity              = Integer.MAX_VALUE;
    public static String  datasetPath              = "";
    public static int leastSeize                   = 33;

    static {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            String filename = "application.properties";
            input = App.class.getClassLoader().getResourceAsStream(filename);
            if(input==null){
                System.out.println("Sorry, unable to find " + filename);
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            enableConstraintPairing  = Boolean.parseBoolean(prop.getProperty("enable.constraint.pairing"));
            enableConstraintCapacity = Boolean.parseBoolean(prop.getProperty("enable.constraint.capacity"));
            showTrace                = Boolean.parseBoolean(prop.getProperty("show.trace"));
            showInfo                 = Boolean.parseBoolean(prop.getProperty("show.info"));
            distanceCartesian = Boolean.parseBoolean(prop.getProperty("distance.cartesian"));
            distanceMatrix           = Boolean.parseBoolean(prop.getProperty("distance.matrix"));
            maxCapacity              = Integer.parseInt(prop.getProperty("max.capacity"));
            datasetPath              = prop.getProperty("dataset.path");
            leastSeize               = Integer.parseInt(prop.getProperty("least.size"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
