package com.yesway.calendardemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.yesway.calendarview.CalendarDay;
import com.yesway.calendarview.CalendarMonth;
import com.yesway.calendarview.DayDecor;
import com.yesway.calendarview.YearView;

public class YearViewActivity extends AppCompatActivity {

    private YearView yearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_view);

        //find view
        yearView = (YearView) findViewById(R.id.yv);

        init();
    }

    private void init() {
        // set current showing year
        yearView.setYear(2017);
        // set today
        yearView.setToday(new CalendarDay(2017, 2, 12));
        // add decorators
        DayDecor dayDecor = new DayDecor();
        dayDecor.putOne(new CalendarDay(2017, 1, 1), Color.BLACK);
        dayDecor.putOne(new CalendarDay(2017, 4, 1), Color.RED);
        dayDecor.putOne(new CalendarDay(2017, 3, 3), Color.GRAY);
        dayDecor.putOne(new CalendarDay(2017, 5, 19), Color.GRAY);
        dayDecor.putOne(new CalendarDay(2017, 12, 25), Color.BLUE);
        dayDecor.putOne(new CalendarDay(2017, 12, 24), Color.MAGENTA);
        yearView.setDecors(dayDecor);
        // add month click listener
        yearView.setOnMonthClickListener(new YearView.OnMonthClickListener() {
            @Override
            public void onMonthClick(YearView yearView, CalendarMonth calendarMonth) {
                Toast.makeText(YearViewActivity.this, calendarMonth.toString() + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
