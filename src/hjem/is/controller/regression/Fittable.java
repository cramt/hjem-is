package hjem.is.controller.regression;

import java.util.HashMap;

public interface Fittable {
    Model fit(Observation[] inputVectors, String dependent, HashMap<String, RegressionalFunction> mutations);
}
