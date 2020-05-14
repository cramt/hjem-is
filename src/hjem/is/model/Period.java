package hjem.is.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Period {
    private int start;
    private int end;

    public Period(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isNow() {
        long now = getUnixTimestamp(LocalDate.now());
        long s = getUnixTimestamp(LocalDate.ofYearDay(new Date().getYear(), start));
        long e = getUnixTimestamp(LocalDate.ofYearDay(new Date().getYear(), end));
        return now > s && now < e;
    }

    private long getUnixTimestamp(LocalDate localDate) {
        return localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
