package hjem.is.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Period {
    private int start;
    private int end;
    private TimeProvider provider;

    public Period(int start, int end) {
        this.start = start;
        this.end = end;
        provider = new StandardTimeProvider();
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        if (end < start) {
            return end + amountOfDaysInYear();
        }
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isNow() {
        long now = getUnixTimestamp(provider.getTime());
        long s = getUnixTimestamp(LocalDate.ofYearDay(new Date().getYear(), start));
        long e = getUnixTimestamp(LocalDate.ofYearDay(new Date().getYear(), end));
        return now > s && now < e;
    }

    public int size() {
        return getStart() - getEnd();
    }

    private long getUnixTimestamp(LocalDate localDate) {
        return localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private int amountOfDaysInYear() {
        return isLeapYear() ? 366 : 365;
    }

    private boolean isLeapYear() {
        boolean leap = false;
        int year = new Date().getYear();

        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0)
                    leap = true;
                else
                    leap = false;
            } else
                leap = true;
        } else
            leap = false;
        return leap;
    }

    public TimeProvider getProvider() {
        return provider;
    }

    public void setProvider(TimeProvider provider) {
        this.provider = provider;
    }
}
