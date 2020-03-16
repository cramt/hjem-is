package hjem.is.controller.regression;

import java.util.HashMap;

public abstract class AbstractFit implements Fittable {
    public abstract Model fit(Observation[] inputVectors, String dependent, HashMap<String, RegressionalFunction> mutations);

    // Contains an array of standardised Observation objects, an array of feature means and an array of feature standard deviations.
    // Stores the values used to standardise data so that data and fitted parameters can be destandardised.
    protected static class Standardisation {
        public Observation[] observations;
        public double[] xbars;
        public double[] sigmas;

        public Standardisation(Observation[] observations, double[] xbars, double[] sigmas) {
            this.observations = observations;
            this.xbars = xbars;
            this.sigmas = sigmas;
        }
    }

    // Standardises data such that each feature has mean 0 and standard deviation 1.
    // Speeds up convergence of gradient descent.
    protected Standardisation standardise(Observation[] inputVectors) {
        // Arrays containing the means and standard deviations of each feature.
        double[] xbars = new double[inputVectors[0].size()];
        double[] sigmas = new double[inputVectors[0].size()];

        // Iterates over the input data and adds the value of each feature to the mean.
        for (int i = 0; i < inputVectors.length; i++) {
            int j = 0;
            for (String feature : inputVectors[i].getFeatures()) {
                xbars[j] += inputVectors[i].getFeature(feature);
                j++;
            }
        }

        // Divides each value by the number of observations to yield the mean.
        for (int i = 0; i < xbars.length; i++)
            xbars[i] = xbars[i] / inputVectors.length;

        // Iterates over the input data and adds the squared difference between that value and the mean to the standard deviation.
        for (int i = 0; i < inputVectors.length; i++) {
            int j = 0;
            for (String feature : inputVectors[i].getFeatures()) {
                sigmas[j] += Math.pow(inputVectors[i].getFeature(feature) - xbars[j], 2);
                j++;
            }
        }

        // Square roots and divides each value by the number of observations to yield the standard deviation.
        for (int i = 0; i < sigmas.length; i++)
            sigmas[i] = Math.sqrt(sigmas[i] / inputVectors.length);

        // Iterates over the input data and standardises each value based on feature means and standard deviations.
        for (int i = 0; i < inputVectors.length; i++) {
            int j = 0;
            for (String feature : inputVectors[i].getFeatures()) {
                inputVectors[i].putFeature(feature, (inputVectors[i].getFeature(feature) - xbars[j]) / sigmas[j]);
                j++;
            }
        }

        Standardisation output = new Standardisation(inputVectors, xbars, sigmas);

        return output;
    }

    // Takes a Standardisation object and destandardises data and fitted parameters based on means and standard deviations.
    protected void deStandardise(Standardisation standard, Observation[] inputVectors, double[] thetas) {
        // Destandardises the intercept and parameters.
        for (int i = 1; i < thetas.length; i++) {
            thetas[0] -= thetas[i] * (standard.xbars[i - 1] / standard.sigmas[i - 1]);
            thetas[i] = (thetas[i] * standard.sigmas[standard.sigmas.length - 1]) / standard.sigmas[i - 1];
        }
        thetas[0] *= standard.sigmas[standard.sigmas.length - 1];
        thetas[0] += standard.xbars[standard.xbars.length - 1];

        // Destandardises the input data.
        for (int i = 0; i < inputVectors.length; i++) {
            int j = 0;
            for (String feature : inputVectors[i].getFeatures()) {
                inputVectors[i].putFeature(feature, ((inputVectors[i].getFeature(feature) * standard.sigmas[j]) + standard.xbars[j]));
                j++;
            }
        }
    }

    // Calculates the R-squared of a fitted model based on the model and the input used to fit it.
    protected double calculateRSquared(Observation[] inputVectors, Model model) {
        // Calculates the mean value of y.
        double ybar = 0;
        for (int i = 0; i < inputVectors.length; i++) {
            ybar += inputVectors[i].getFeature(model.dependent);
        }
        ybar /= inputVectors.length;

        // Calculates residual and total sum of squares for the model.
        double rss = 0;
        double tss = 0;
        for (int i = 0; i < inputVectors.length; i++) {
            rss += Math.pow((inputVectors[i].getFeature(model.dependent) - model.predict(inputVectors[i])), 2);
            tss += Math.pow((inputVectors[i].getFeature(model.dependent) - ybar), 2);
        }

        // Returns the R-squared value.
        return (1 - rss / tss);
    }

    public final RegressionalFunction[] ALL_REGRESSIONS = new RegressionalFunction[]{
            value -> value,
            value -> value * value,
            value -> Math.pow(Math.E, value),
            Math::log
    };
}
