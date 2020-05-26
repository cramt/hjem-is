package hjem.is.test;

import static org.junit.jupiter.api.Assertions.*;

public class MyAsserts {
    public static void assertApproxEquals(double a, double b, double diff) {
        assertTrue(Math.abs(a - b) < diff, a + " is not approximately equal to " + b);
    }
}
