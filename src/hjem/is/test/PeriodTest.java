package hjem.is.test;

import hjem.is.model.time.ControlledTimeProvider;
import hjem.is.model.time.Period;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PeriodTest {
    @Test
    public void simple1999() {
        ControlledTimeProvider provider = new ControlledTimeProvider();
        Period period = new Period(1, 30);
        period.setProvider(provider);
        provider.setTime(LocalDate.of(1999, 1, 3));
        assertTrue(period.isNow());
    }
    @Test
    public void simple2050(){
        ControlledTimeProvider provider = new ControlledTimeProvider();
        Period period = new Period(1, 30);
        period.setProvider(provider);
        provider.setTime(LocalDate.of(2050, 1, 3));
        assertTrue(period.isNow());
    }
    @Test
    public void negative1999(){
        ControlledTimeProvider provider = new ControlledTimeProvider();
        Period period = new Period(30, 60);
        period.setProvider(provider);
        provider.setTime(LocalDate.of(1999, 1, 3));
        assertFalse(period.isNow());
    }
    @Test
    public void newYear(){
        ControlledTimeProvider provider = new ControlledTimeProvider();
        Period period = new Period(355, 3);
        period.setProvider(provider);
        provider.setTime(LocalDate.of(2050, 12, 31));
        assertTrue(period.isNow());
        provider.setTime(LocalDate.of(2050, 1, 1));
        assertTrue(period.isNow());
    }
}
