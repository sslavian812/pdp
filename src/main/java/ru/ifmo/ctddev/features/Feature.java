package ru.ifmo.ctddev.features;

/**
 * Created by viacheslav on 14.02.2016.
 */
public class Feature {
    double value;
    String name;
    String description;

    public Feature(String name, double value, String description) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Feature{\n" +
                "    name='" + name + '\'' +
                ",\n    value=" + value +
                ",\n    description='" + description + '\'' +
                "\n}";
    }

    /**
     * provides comma separated name,value,description as a line
     *
     * @return
     */
    public String toCSVString() {
        return name + "," + value + "," + description;
    }

    public static String getHeadOfCSV() {
        return "name,value,description";
    }

}
