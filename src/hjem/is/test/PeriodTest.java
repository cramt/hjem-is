package hjem.is.test;

import hjem.is.model.time.ControlledTimeProvider;
import hjem.is.model.time.Period;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PeriodTest {
    @Test
    public void test(){
        ControlledTimeProvider provider = new ControlledTimeProvider();
        Period period = new Period(1, 30);
        period.setProvider(provider);
        provider.setTime(LocalDate.of(1999, 1, 3));
        assertTrue(period.isNow());
    }
}
