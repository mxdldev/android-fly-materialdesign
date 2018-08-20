package com.yesway.calendarview;

import java.util.Calendar;
import java.util.Date;

import android.support.annotation.NonNull;

public class CalendarDay implements Comparable<CalendarDay> {
    private CalendarMonth calendarMonth;
    int month;
    int year;
    int day;

    public CalendarDay() {
        this(Calendar.getInstance());
    }

    public CalendarDay(CalendarMonth calendarMonth, int day) {
        setDay(calendarMonth.getYear(), calendarMonth.getMonth(), day);
    }

    public CalendarDay(int year, int month, int day) {
        setDay(year, month, day);
    }

    public CalendarDay(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        setDay(year, month, day);
    }

    public void setDay(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        calendarMonth = new CalendarMonth(year, month);

        if(year < 1900) {
            throw new IllegalArgumentException("year can not smaller than 1900: " + year);
        }
        int maxDays = CalendarUtils.getDaysInMonth(month - 1, year);
        if(day > maxDays) {
            throw new IllegalArgumentException("date {" + year + month + day + "} doesn't exist");
        }
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public CalendarMonth getCalendarMonth() {
        return calendarMonth;
    }

    public int getYear() {
        return year;
    }

    public Date getDate() {
        return new Date(year - 1900, month - 1, day);
    }

    @Override
    public int compareTo(@NonNull CalendarDay another) {
        if(another == null){
            return -1;
        }
        if(another.getYear() > year || (another.getYear() == year && another.getMonth() > month)
                || (another.getYear() == year && another.getMonth() == month && another.getDay() > day)) {
            return -1;
        } else if(another.getYear() == year && another.getMonth() == month && another.getDay() == day) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof CalendarDay) {
            if(this == o)
                return true;
            CalendarDay another = (CalendarDay) o;
            return another.getYear() == year && another.getMonth() == month && another.getDay() == day;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return year * 10000 + month * 100 + day;
    }

    @Override
    public String toString() {
        return "CalendarDay: { " + year + "-" + month + "-" + day + " }";
    }
}
