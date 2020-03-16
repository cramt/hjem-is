package hjem.is.controller.regression;

import java.util.HashMap;
import java.util.LinkedHashMap;

// Represents a fitted multiple linear regression model. 
public class Model {
    public LinkedHashMap<String, Double> parameters;
    public String dependent;
    public double rSquared;
    private HashMap<String, RegressionalFunction> mutations;

    public Model(LinkedHashMap<String, Double> parameters, String dependent, HashMap<String, RegressionalFunction> mutations) {
        this.parameters = parameters;
        this.dependent = dependent;
        this.mutations = mutations;
    }

    // Takes an Observation matching the dimensions of the input data and calculates a value for the dependent variable. 
    public double predict(Observation input) {
        double yhat = parameters.get("Intercept");
        for (String feature : parameters.keySet())
            if (!feature.equals("Intercept")) {
                yhat += parameters.get(feature) * mutations.get(feature).function(input.getFeature(feature));
            }

        return yhat;
    }

    public String toString() {
        String output = "Multiple linear regression predicting " + dependent + " using " + (parameters.size() - 1) + " features.\n"
                + "R-Squared: " + rSquared + "\n\nFeature\t\t\t\tParameter\n-------------------------------------------------------------\n";

        for (String feature : parameters.keySet()) {
            String formattedName = feature;
            while (formattedName.length() < 16) {
                formattedName = formattedName + " ";
            } // formatting
            output = output + formattedName + "\t\t" + parameters.get(feature) + "\n";
        }

        return output;
    }
}
