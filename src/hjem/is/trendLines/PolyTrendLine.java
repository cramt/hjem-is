package hjem.is.trendLines;

public class PolyTrendLine extends OLSTrendLine {
    final int degree;

    public PolyTrendLine(int degree) {
        if (degree < 0) throw new IllegalArgumentException("The degree of the polynomial must not be negative");
        this.degree = degree;
    }

    protected double[] xVector(double x) {
        return new double[]{1, Math.pow(x, degree)};
    }
}

