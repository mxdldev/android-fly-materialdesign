package com.yesway.calendarview;

import java.util.Calendar;

import android.support.annotation.NonNull;

public class CalendarMonth implements Comparable<CalendarMonth> {
    int month;
    int year;

    public CalendarMonth() {
        this(Calendar.getInstance());
    }

    public CalendarMonth(int year, int month) {
        setMonth(year, month);
    }

    public CalendarMonth(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        setMonth(year, month);
    }

    public void setMonth(int year, int month) {
        this.year = year;
        this.month = month;
        if(year < 1900) {
            throw new IllegalArgumentException("year can not small than 1900");
        }
        if(month < 1 || month > 12) {
            throw new IllegalArgumentException("month " + month + "doesn't exist");
        }
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public CalendarMonth previous() {
        int preY = year;
        int preM = month - 1;
        if(preM == 0) {
            preY = year - 1;
            preM = 12;
        }
        return new CalendarMonth(preY, preM);
    }

    public CalendarMonth next() {
        int nextY = year;
        int nextM = month + 1;
        if(nextM == 13) {
            nextY = year + 1;
            nextM = 1;
        }
        return new CalendarMonth(nextY, nextM);
    }

    @Override
    public int compareTo(@NonNull CalendarMonth another) {
        if(another.getYear() > year || (another.getYear() == year && another.getMonth() > month)) {
            return -1;
        } else if(another.getYear() == year && another.getMonth() == month) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof CalendarMonth) {
            if(this == o)
                return true;
            CalendarMonth another = (CalendarMonth) o;
            return another.getYear() == year && another.getMonth() == month;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return year * 100 + month;
    }

    @Override
    public String toString() {
        return "CalendarMonth: { " + year + "-" + month + " }";
    }
}
