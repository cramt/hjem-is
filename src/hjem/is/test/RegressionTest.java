package hjem.is.test;

import hjem.is.controller.regression.*;
import hjem.is.utilities.Parse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class RegressionTest {
    public static Observation[] testData;

    @BeforeAll
    public static void loadData() throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader("res/1000 Sales Records.csv"));
        String[] header = csvReader.readLine().split(",");
        header = Arrays.copyOfRange(header, 8, header.length);
        String row;
        List<List<Double>> data = new ArrayList<>();
        while ((row = csvReader.readLine()) != null) {
            String[] str = row.split(",");
            data.add(Arrays.stream(Arrays.copyOfRange(str, 8, str.length)).map(Parse::integer64).collect(Collectors.toList()));
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
    public void regressionTest() {
        String dependant = testData[0].getFeatures().stream().findFirst().get();
        Model model = new ModelFinder(new GradientDescentFit(0.0001), testData, dependant).run();
        double predicted = model.predict(testData[500]);
        double real = testData[500].getFeature(dependant);
        System.out.println(predicted + " - " + real);
    }
}
