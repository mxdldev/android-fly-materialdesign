package com.yesway.calendarview.month;

public interface ScrollerMonthViewClickListener {

    void onDayOfMonthSelected(int year, int month, int day);

    void onDateRangeSelected(final ScrollerMonthAdapter.SelectedDays<ScrollerMonthAdapter.CalendarDay> selectedDays);

}
