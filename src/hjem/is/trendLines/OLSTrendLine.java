package hjem.is.trendLines;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.util.ArrayList;

public abstract class OLSTrendLine implements TrendLine {
    protected RealMatrix coef = null; // will hold prediction coefs once we get values

    private double rSquared;

    protected abstract double[] xVector(double x); // create vector of values from x

    @Override
    public void setValues(double[] y, double[] x) {
        if (x.length != y.length) {
            throw new IllegalArgumentException(String.format("The numbers of y and x values must be equal (%d != %d)", y.length, x.length));
        }
        double[][] xData = new double[x.length][];
        for (int i = 0; i < x.length; i++) {
            // the implementation determines how to produce a vector of predictors from a single x
            xData[i]  = xVector(x[i]);
        }
        OLSMultipleLinearRegression ols = new OLSMultipleLinearRegression();
        ols.setNoIntercept(true); // let the implementation include a constant in xVector if desired
        ols.newSampleData(y, xData); // provide the data to the model
        double[] params = ols.estimateRegressionParameters();
        rSquared = ols.calculateRSquared();
        coef = MatrixUtils.createColumnRealMatrix(params); // get our coefs
    }

    @Override
    public double predict(double x) {
        return coef.preMultiply(xVector(x))[0];
    }

    @Override
    public double getRSquared() {
        return rSquared;
    }

    public static TrendLine assumeTrend(double[] y, double[] x) {
        ArrayList<TrendLine> trendLines = new ArrayList<>();
        trendLines.add(new ExpTrendLine());
        trendLines.add(new LogTrendLine());
        trendLines.add(new PolyTrendLine(1));
        trendLines.add(new PolyTrendLine(2));
        for (TrendLine trendLine : trendLines) {
            trendLine.setValues(y, x);
        }
        ArrayList<TrendLine> viable = new ArrayList<>();
        for (TrendLine trendLine : trendLines) {
            if (!Double.isNaN(trendLine.getRSquared())) {
                viable.add(trendLine);
            }
        }
        viable.sort((a, b) -> (int) ((b.getRSquared() - a.getRSquared()) * 10000));
        return viable.get(0);
    }
}


