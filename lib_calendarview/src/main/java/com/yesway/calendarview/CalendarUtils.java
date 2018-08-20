package com.yesway.calendarview;

import java.util.Calendar;


public class CalendarUtils {
    public static int getDaysInMonth(CalendarMonth calendarMonth) {
        return getDaysInMonth(calendarMonth.getMonth() - 1, calendarMonth.getYear());
    }

    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                return (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    public static CalendarDay offsetDay(CalendarDay now, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now.getDate());
        calendar.add(Calendar.DAY_OF_MONTH, offset);
        return new CalendarDay(calendar);
    }
}
