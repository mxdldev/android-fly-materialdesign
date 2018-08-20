package com.yesway.calendardemo;

import com.yesway.calendarview.month.ScrollerMonthController;
import com.yesway.calendarview.month.ScrollerMonthView;
import com.yesway.calendarview.month.ScrollerMonthAdapter;
import com.yesway.calendarview.month.ScrollerMonthViewClickListener;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class ScrollerMonthCalendarActivity extends AppCompatActivity {

    private ScrollerMonthView dayPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_month_calendar);

        dayPickerView = (ScrollerMonthView) findViewById(R.id.pickerView);
        dayPickerView.setController(new ScrollerMonthController(){
            @Override
            public int getMaxYear()
            {
                return 2017;
            }

            @Override
            public int getMinYear() {
                return 2015;
            }

            @Override
            public int getCurrentYear() {
                return 2016;
            }

        });
        dayPickerView.setViewClickListener(new ScrollerMonthViewClickListener() {
            @Override
            public void onDayOfMonthSelected(int year, int month, int day) {

            }

            @Override
            public void onDateRangeSelected(ScrollerMonthAdapter.SelectedDays<ScrollerMonthAdapter.CalendarDay> selectedDays) {

            }
        });

    }

}
