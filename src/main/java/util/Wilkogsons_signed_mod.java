package util;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Wilkogsons_signed_mod {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("C:/tmp/s/1+N,BM [1.0,0.0,0.0,0.0,0.0] vs K+KN"));
//		PrintWriter pw = new PrintWriter("C:/tmp/s/res.txt");
		FileWriter pw = new FileWriter("C:/tmp/s/res.txt", true);

		NumberFormat formatter = new DecimalFormat("#0.00");

		while (br.ready()) {
			String datasetName = br.readLine();
			System.err.println(datasetName);
			StringTokenizer line1 = new StringTokenizer(br.readLine());
			StringTokenizer line2 = new StringTokenizer(br.readLine());
			br.readLine();

			int n = line1.countTokens();

			double[] diff = new double[n];

			for (int i = 0; i < n; i++) {
				double res1 = Double.parseDouble(line1.nextToken());
				double res2 = Double.parseDouble(line2.nextToken());
				diff[i] = res1 - res2;
			}

			Arrays.sort(diff);

			int j=0;
			int w=0;
			
			for (int i = 0; i < n; i++) {
				if (diff[0]>0){
					w+=(i+1);					
				}else if (diff[0]<0){
					w-=(i+1);
				}else if (j==0){
					w+=(i+1);
					j=1;
				}else if (j==1){
					w-=(i+1);
					j=0;
				}
			}
			
			w = Math.abs(w);
			double sigma = Math.sqrt(n*(n+1)*(2*n+1)/6);
			double z = w/sigma;
			
			pw.write(datasetName+" wst= " + formatter.format(z));
			pw.write("\n");
			pw.flush();
			pw.close();

		}

		br.close();
		pw.close();
	}
}