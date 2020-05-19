package hjem.is.model.time;

import java.time.LocalDate;

public class StandardTimeProvider implements TimeProvider {

    @Override
    public LocalDate getTime() {
        return LocalDate.now();
    }

}
