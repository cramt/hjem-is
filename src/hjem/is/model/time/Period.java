package hjem.is.model.time;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Period {
    private int start;
    private int end;
    private ITimeProvider provider;

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
        int now = provider.getTime().getDayOfYear();
        int s = getStart();
        int e = getEnd();
        return (now > s && now < e) || (now > s - amountOfDaysInYear() && now < e - amountOfDaysInYear());
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

    public ITimeProvider getProvider() {
        return provider;
    }

    public void setProvider(ITimeProvider provider) {
        this.provider = provider;
    }
}
