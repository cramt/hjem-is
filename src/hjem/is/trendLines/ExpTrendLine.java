package hjem.is.trendLines;

public class ExpTrendLine extends OLSTrendLine {
    @Override
    protected double[] xVector(double x) {
        return new double[]{1, Math.pow(Math.E, x)};
    }
}
