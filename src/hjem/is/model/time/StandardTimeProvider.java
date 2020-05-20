package hjem.is.model.time;

import java.time.LocalDate;

public class StandardTimeProvider implements ITimeProvider {

    @Override
    public LocalDate getTime() {
        return LocalDate.now();
    }

}
