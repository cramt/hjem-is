package hjem.is;

import hjem.is.trendLines.OLSTrendLine;
import hjem.is.trendLines.TrendLine;

public class Main {
    public static void main(String[] args) {
        double[] x = new double[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
        double[] y = new double[]{1, 1.2, 1.44, 1.7279999999999998, 2.0736, 2.4883199999999994, 2.9859839999999993, 3.583180799999999, 4.2998169599999985, 5.1597803519999985, 6.191736422399997, 7.430083706879997, 8.916100448255996, 10.699320537907195, 12.839184645488633, 15.40702157458636, 18.48842588950363, 22.186111067404354, 26.623333280885227, 31.94799993706227};
        TrendLine line = OLSTrendLine.assumeTrend(y, x);
        System.out.println(line.predict(5.5));
    }
}
