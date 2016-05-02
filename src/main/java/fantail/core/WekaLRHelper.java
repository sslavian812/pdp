/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fantail.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Quan Sun quan.sun.nz@gmail.com
 */
public class WekaLRHelper {

    public static Instances covertArff2Xarff(Instances data) throws Exception{
        String userDIR = System.getProperty("user.dir");
        String randFileName = Long.toString(System.nanoTime()).substring(10) + ".LRT.temp.xarff";
        String path_separator = System.getProperty("file.separator");
        String xarffPath = userDIR + path_separator + randFileName;
        //System.out.println(m_xarffPath);
        int numObjects = Tools.getNumberTargets(data);

        StringBuilder sb = new StringBuilder();
        sb.append("@relation arff2xarff").append(System.getProperty("line.separator"));
        for (int i = 0; i < data.numAttributes() - 1; i++) {
            sb.append("@attribute ");
            sb.append(data.attribute(i).name());
            sb.append(" numeric").append(System.getProperty("line.separator"));
        }
        sb.append("@attribute L RANKING {");
        for (int i = 0; i < numObjects; i++) {
            String spr = ",";
            if (i == numObjects - 1) {
                spr = "";
            }
            String targetName = "T" + (i);
            sb.append(targetName).append(spr);
        }
        sb.append("}").append(System.getProperty("line.separator"));
        sb.append("@data ").append(System.getProperty("line.separator"));

        for (int i = 0; i < data.numInstances(); i++) {
            Instance inst = data.instance(i);
            for (int j = 0; j < data.numAttributes() - 1; j++) {
                sb.append(inst.value(j)).append(",");
            }
            for (int x = 1; x <= numObjects; x++) {
                int rank = x;

                String[] names = Tools.getTargetNames(inst);
                String algo = getName(rank, Tools.getTargetVector(inst), names);

                String sprr = ">";
                if (x == names.length) {
                    sprr = "";
                }
                sb.append(algo).append(sprr);
            }
            sb.append(System.getProperty("line.separator"));
        }

        File file = new File(xarffPath);
        try (Writer output = new BufferedWriter(new FileWriter(file))) {
            output.write(sb.toString());
        }
        weka.core.converters.XArffLoader xarffLoader = new weka.core.converters.XArffLoader();
        xarffLoader.setSource(new File(xarffPath));
        Instances xarffData = xarffLoader.getDataSet();
        //
        File tmpxarffFile = new File(xarffPath);
        if (tmpxarffFile.exists()) {
            tmpxarffFile.delete();
        }
        return xarffData;
    }
    
    private static String getName(int rank, double[] rankings, String[] names) {
        String n = "NaN";
        for (int i = 0; i < rankings.length; i++) {
            if (rank == rankings[i]) {
                return names[i];
            }
        }
        return n;
    }
}
