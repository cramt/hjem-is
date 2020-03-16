package hjem.is.controller.regression;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class GradientDescentFit extends AbstractFit {
    private double alpha;

    public GradientDescentFit(double alpha) {
        this.alpha = alpha;
    }

    // Fits the model using batch gradient descent with a learning rate specified by the alpha parameter.
    // Cost function is convex so global minimum will always be reached.
    @Override
    public Model fit(Observation[] inputVectors, String dependent, HashMap<String, RegressionalFunction> mutations) {
        // Standardises the input data to have mean 0 and standard deviation 1.
        Standardisation stanData = standardise(inputVectors);

        // Converts the array of Observation objects to a matrix (2-d array) with an extra first column of 1s for the intercept.
        double[][] train = new double[inputVectors[0].size() + 1][inputVectors.length];
        for (int i = 0; i < inputVectors.length; i++) {
            train[0][i] = 1; // Intercept
            train[train.length - 1][i] = inputVectors[i].getFeature(dependent); // Dependent variable in the last column
            int j = 1;
            for (String feature : inputVectors[i].getFeatures()) {
                if (!feature.equals(dependent)) {
                    train[j][i] = mutations.get(feature).function(inputVectors[i].getFeature(feature));
                    j++;
                }
            }
        }

        // One parameter theta per independent variable and one for the intercept.
        double[] thetas = new double[inputVectors[0].size()];
        double[] temps = new double[thetas.length]; // Parallel array for temp values.
        double delta; // Absolute change in parameters; used to measure convergence

        do {
            delta = 0;

            // For each parameter theta:
            for (int i = 0; i < thetas.length; i++) {
                // Updates the temp value for that parameter.
                temps[i] = thetas[i] - (alpha * ((double) 1 / train.length) * evaluateCost(thetas, train, i));

                // Calculates the absolute change in that parameter.
                delta += Math.abs(thetas[i] - temps[i]);
            }

            // Updates each theta to its temp value.
            for (int i = 0; i < thetas.length; i++)
                thetas[i] = temps[i];
        } while (delta > 1E-7); // Threshold at which we conclude that convergence has occurred

        // Destandardises the data and fitted parameters.
        deStandardise(stanData, inputVectors, thetas);

        // Creates a hashmap of the name of each feature and its associated parameter.
        LinkedHashMap<String, Double> parameters = new LinkedHashMap<String, Double>();
        parameters.put("Intercept", thetas[0]);
        int j = 1;
        for (String feature : inputVectors[1].getFeatures()) {
            if (!feature.equals(dependent)) { // Ignores the dependent variable
                parameters.put(feature, thetas[j]);
                j++;
            }
        }

        // Constructs the Model object.
        Model outputModel = new Model(parameters, dependent, mutations);

        // Calculates r-squared and sets it on the model.
        outputModel.rSquared = calculateRSquared(inputVectors, outputModel);

        return outputModel;
    }

    // Evaluates the cost function (squared error) for a given level of parameters.
    // Used in the update step of gradient descent.
    private double evaluateCost(double[] thetas, double[][] data, int featureIndex) {
        double result = 0;

        // For every row in the dataset:
        for (int i = 0; i < data[0].length; i++) {
            double error = 0;

            // Calculates the value predicted by the parameters.
            for (int j = 0; j < data.length - 1; j++)
                error += data[j][i] * thetas[j];

            // Subtracts the actual value to get the error and scales by the value in that row for the feature being updated.
            error -= data[data.length - 1][i];
            error *= data[featureIndex][i];

            // Adds the result to the sum.
            result += error;
        }

        return result;
    }
}
