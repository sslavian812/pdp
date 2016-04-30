package ru.ifmo.ctddev.datasets;

import ru.ifmo.ctddev.gui.App;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class holds a script for one-time use to converts original.
 * Created by viacheslav on 14.02.2016.
 */
public class OneTimeConverter {

    public static void main(String[] args) {
        try {
            List<String[]> orders = CSVReader.read(App.class.getClassLoader().getResource("original/orders.csv").getFile(), ",");
            List<String[]> airpotrs = CSVReader.read(App.class.getClassLoader().getResource("original/airports.csv").getFile(), ",");

            String header[] = "id1,x1,y1,id2,x2,y2,direction".split(",");

            List<String[]> out = new ArrayList<String[]>();
            out.add(header);

            Map<String, Integer> ids = new HashMap<>();
            Map<Integer, Point2D.Double> coords = new HashMap<>();
            for (String[] ss : airpotrs) {
                if (!ids.containsKey(ss[0])) {
                    ids.put(ss[0], ids.size());
                }
                coords.put(ids.get(ss[0]), Util.convertLatLonToXY(new Point2D.Double(Double.parseDouble(ss[1]), Double.parseDouble(ss[2]))));
            }

            int i = 0;
            for (String ss[] : orders) {
                String[] outRecord = new String[7];
                outRecord[0] = "" + (i); // id of the left point
                Point2D.Double point = Util.convertLatLonToXY(
                        new Point2D.Double(Double.parseDouble(ss[1]), Double.parseDouble(ss[2])));
                outRecord[1] = "" + point.getX();
                outRecord[2] = "" + point.getY();
                outRecord[3] = "" + ids.get(ss[5]); // id of the right point (of airport)
                outRecord[4] = "" + coords.get(ids.get(ss[5])).getX();
                outRecord[5] = "" + coords.get(ids.get(ss[5])).getY();
                outRecord[6] = (ss[7].equals("Out") ? ">" : "<");

                ++i;
                out.add(outRecord);
            }

            CSVWriter.write("C:\\tmp\\taxi8129.csv", out);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
