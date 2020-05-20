package hjem.is.model.time;

import java.time.LocalDate;

public class ControlledTimeProvider implements ITimeProvider {
    LocalDate time;

    @Override
    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }
}
