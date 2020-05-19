package hjem.is.model;

import java.time.LocalDate;

public class ControlledTimeProvider implements TimeProvider {
    LocalDate time;

    @Override
    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }
}
