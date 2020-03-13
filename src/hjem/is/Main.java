package hjem.is;

import hjem.is.trendLines.OLSTrendLine;
import hjem.is.trendLines.TrendLine;

public class Main {
    public static void main(String[] args) {
        double[] x = new double[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
        double[] y = new double[]{0, 1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256, 289, 324, 361};
        TrendLine line = OLSTrendLine.assumeTrend(y, x);
        System.out.println(line.toString());
        System.out.println(line.predict(5.5));
    }
}
