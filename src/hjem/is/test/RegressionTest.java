package hjem.is.test;

import hjem.is.controller.regression.*;
import hjem.is.utilities.Parse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RegressionTest {
    public static Observation[] testData;

    @BeforeAll
    public static void loadData() throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader("res/concrete.csv"));
        String[] header = csvReader.readLine().split(",");
        String row;
        List<List<Double>> data = new ArrayList<>();
        while ((row = csvReader.readLine()) != null) {
            String[] str = row.split(",");
            data.add(Arrays.stream(str).map(Parse::integer64).collect(Collectors.toList()));
        }
        Observation[] observations = new Observation[data.size()];
        for (int i = 0; i < data.size(); i++) {
            Observation o = new Observation();
            for (int j = 0; j < data.get(i).size(); j++) {
                o.putFeature(header[j], data.get(i).get(j));
            }
            observations[i] = o;
        }
        testData = observations;
    }

    @Test
    public void gradientDecentTest() {
        String dependant = "compressivestrength";
        Model model = new ModelFinder(new GradientDescentFit(0.0001), testData, dependant).run();
        double predicted = model.predict(testData[500]);
        double real = testData[30].getFeature(dependant);
        System.out.println("gradient descent r^2 = " + model.rSquared);
        System.out.println("gradient descent precision = " + Math.abs(real - predicted));
        MyAsserts.assertApproxEquals(real, predicted, 20);
    }

    @Test
    public void normalEquationTest() {
        String dependant = "compressivestrength";
        Model model = new ModelFinder(new NormalEquationFit(), testData, dependant).run();
        double predicted = model.predict(testData[500]);
        double real = testData[30].getFeature(dependant);
        System.out.println("normal equation r^2 = " + model.rSquared);
        System.out.println("normal equation precision = " + Math.abs(real - predicted));
        MyAsserts.assertApproxEquals(real, predicted, 20);
    }
}
