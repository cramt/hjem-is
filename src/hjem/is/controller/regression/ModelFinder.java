package hjem.is.controller.regression;

import hjem.is.utilities.Combination;
import hjem.is.utilities.Permutations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ModelFinder {
    private Fittable fittable;
    private Observation[] observations;
    private String dependant;
    private List<List<RegressionalFunction>> funcs;

    public ModelFinder(Fittable fittable, Observation[] observations, String dependant, RegressionalFunction[] functions) {
        this.fittable = fittable;
        this.observations = observations;
        this.dependant = dependant;
        this.funcs = Combination.repeated(Arrays.asList(functions), observations[0].getFeatures().size() - 1);
    }

    public ModelFinder(Fittable fittable, Observation[] observations, String dependant) {
        this(fittable, observations, dependant, new RegressionalFunction[]{x -> Math.pow(x, 3), x -> Math.pow(Math.E, x), x -> x, Math::log});
    }

    public Model run() {
        ArrayList<Model> models = new ArrayList<>();
        for (List<RegressionalFunction> funcs : funcs) {
            HashMap<String, RegressionalFunction> mutations = new HashMap<>();
            int i = 0;
            for (String feature : observations[0].getFeatures()) {
                if (feature.equals(dependant)) {
                    continue;
                }
                mutations.put(feature, funcs.get(i++));
            }
            models.add(fittable.fit(observations, dependant, mutations));
        }
        ArrayList<Model> notNaN = new ArrayList<>();
        for (Model model : models) {
            if (!Double.isNaN(model.rSquared)) {
                notNaN.add(model);
            }
        }
        notNaN.sort((a, b) -> (int) ((b.rSquared - a.rSquared) * 10000));
        return notNaN.get(0);
    }
}
