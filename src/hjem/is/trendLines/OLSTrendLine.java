package hjem.is.trendLines;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public abstract class OLSTrendLine implements TrendLine {
    protected RealMatrix coef = null; // will hold prediction coefs once we get values

    private double rSquared;

    protected abstract double[] xVector(double x); // create vector of values from x

    protected abstract boolean logY(); // set true to predict log of y (note: y must be positive)

    @Override
    public void setValues(double[] y, double[] x) {
        if (x.length != y.length) {
            throw new IllegalArgumentException(String.format("The numbers of y and x values must be equal (%d != %d)", y.length, x.length));
        }
        double[][] xData = new double[x.length][];
        for (int i = 0; i < x.length; i++) {
            // the implementation determines how to produce a vector of predictors from a single x
            xData[i] = xVector(x[i]);
        }
        if (logY()) { // in some models we are predicting ln y, so we replace each y with ln y
            y = Arrays.copyOf(y, y.length); // user might not be finished with the array we were given
            for (int i = 0; i < x.length; i++) {
                y[i] = Math.log(y[i]);
            }
        }
        OLSMultipleLinearRegression ols = new OLSMultipleLinearRegression();
        rSquared = ols.calculateRSquared();
        ols.setNoIntercept(true); // let the implementation include a constant in xVector if desired
        ols.newSampleData(y, xData); // provide the data to the model
        coef = MatrixUtils.createColumnRealMatrix(ols.estimateRegressionParameters()); // get our coefs
    }

    @Override
    public double predict(double x) {
        double yhat = coef.preMultiply(xVector(x))[0]; // apply coefs to xVector
        if (logY()) yhat = (Math.exp(yhat)); // if we predicted ln y, we still need to get y
        return yhat;
    }

    @Override
    public double getRSquared() {
        return rSquared;
    }

    public static TrendLine assumeTrend(double[] y, double[] x) {
        ArrayList<TrendLine> trendLines = new ArrayList<>();
        trendLines.add(new ExpTrendLine());
        trendLines.add(new LogTrendLine());
        trendLines.add(new PowerTrendLine());
        trendLines.add(new PolyTrendLine(1));
        trendLines.add(new PolyTrendLine(2));
        for (TrendLine trendLine : trendLines) {
            trendLine.setValues(y, x);
        }
        trendLines.sort((a, b) -> (int) (a.getRSquared() - b.getRSquared()));
        return trendLines.get(0);
    }
}


