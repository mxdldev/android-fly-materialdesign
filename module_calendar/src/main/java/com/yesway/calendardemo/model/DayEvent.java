package com.yesway.calendardemo.model;

import com.yesway.calendarview.CalendarDay;

/**
 * day event bean
 *
 * @author wl
 * @since 2016/08/26 12:06
 */
public class DayEvent {
    private int year;
    private int month;
    private int day;
    private EventType type;
    private String[] eventDetails;

    public boolean isThisDay(CalendarDay calendarDay) {
        return calendarDay != null && year == calendarDay.getYear() && month == calendarDay.getMonth() && day == calendarDay.getDay();
    }



    public DayEvent(int year, int month, int day, EventType type, String[] eventDetails) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.type = type;
        this.eventDetails = eventDetails;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String[] getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String[] eventDetails) {
        this.eventDetails = eventDetails;
    }
}
