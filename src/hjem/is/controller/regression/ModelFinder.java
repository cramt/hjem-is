package hjem.is.controller.regression;

import hjem.is.utilities.Combination;
import hjem.is.utilities.Permutations;
import org.apache.poi.ss.formula.functions.Mode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

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
        //this(fittable, observations, dependant, new RegressionalFunction[]{x -> Math.pow(x, 3), x -> Math.pow(Math.E, x), x -> x, Math::log});
        this(fittable, observations, dependant, new RegressionalFunction[]{x -> x});
    }

    public Model run() {
        ArrayList<FutureTask<Model>> modelsFuture = new ArrayList<>();
        for (List<RegressionalFunction> funcs : funcs) {
            HashMap<String, RegressionalFunction> mutations = new HashMap<>();
            int i = 0;
            for (String feature : observations[0].getFeatures()) {
                if (feature.equals(dependant)) {
                    continue;
                }
                mutations.put(feature, funcs.get(i++));
            }
            FutureTask<Model> task = new FutureTask<Model>(() -> fittable.fit(observations, dependant, mutations));
            task.run();
            modelsFuture.add(task);
        }
        List<Model> models = modelsFuture.stream().map(x -> {
            try {
                return x.get();
            } catch (InterruptedException | ExecutionException ignored) {
                return null;
            }
        }).collect(Collectors.toList());
        ArrayList<Model> notNaN = new ArrayList<>();
        for (Model model : models) {
            if (!Double.isNaN(model.rSquared)) {
                notNaN.add(model);
            }
        }
        Model largest = notNaN.get(0);
        for (Model model : notNaN) {
            if (model.rSquared > largest.rSquared) {
                largest = model;
            }
        }
        return largest;
    }
}
