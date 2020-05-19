package hjem.is.model;

import java.time.LocalDate;

public class StandardTimeProvider implements TimeProvider {

    @Override
    public LocalDate getTime() {
        return LocalDate.now();
    }

}
