package hjem.is.controller.regression;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class NormalEquationFit extends AbstractFit {
    // Fits the model in closed form using the normal equation method.
    @Override
    public Model fit(Observation[] inputVectors, String dependent, HashMap<String, RegressionalFunction> mutations) {
        // Builds the design matrix of features from the array of Observation objects, as well as its transpose. 
        double[][] design = new double[inputVectors.length][inputVectors[0].size()];
        double[][] designT = new double[inputVectors[0].size()][inputVectors.length]; // Transpose
        for (int i = 0; i < inputVectors.length; i++) {
            design[i][0] = 1; // Intercept
            designT[0][i] = 1;
            int j = 1;
            for (String feature : inputVectors[i].getFeatures()) {
                if (!feature.equals(dependent)) {
                    design[i][j] = inputVectors[i].getFeature(feature);
                    designT[j][i] = inputVectors[i].getFeature(feature);
                    j++;
                }
            }
        }
        RealMatrix X = new Array2DRowRealMatrix(design);
        RealMatrix XPrime = new Array2DRowRealMatrix(designT);

        // Builds the vector of y values. 
        double[] yArray = new double[inputVectors.length];
        for (int i = 0; i < inputVectors.length; i++)
            yArray[i] = inputVectors[i].getFeature(dependent);
        RealMatrix y = new Array2DRowRealMatrix(yArray);

        // Solves for the parameter vector: theta = (X'X)-1 X'y
        RealMatrix theta = new LUDecomposition(XPrime.multiply(X)).getSolver().getInverse().multiply(XPrime).multiply(y);

        // Creates a hashmap of the name of each feature and its associated fitted parameter. 
        LinkedHashMap<String, Double> parameters = new LinkedHashMap<String, Double>();
        double[] thetas = theta.getColumn(0);
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
}
