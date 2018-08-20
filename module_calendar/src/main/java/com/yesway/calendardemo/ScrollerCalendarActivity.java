package com.yesway.calendardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yesway.calendarview.CalendarMonth;
import com.yesway.calendarview.YearView;
import com.yesway.calendarview.year.ScrollerYearView;
import com.yesway.calendarview.year.ScrollerYearViewClickListener;

public class ScrollerCalendarActivity extends AppCompatActivity implements ScrollerYearViewClickListener {
    private ScrollerYearView monthPickerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_scroller_calendar);
        monthPickerView = (ScrollerYearView) findViewById(R.id.pickerView);
        monthPickerView.setYearViewClickListener(this);
    }


    @Override
    public void onMonthOfYearSelected(YearView yearView, CalendarMonth calendarMonth) {
        Toast.makeText(getApplicationContext(), calendarMonth.getYear()+"-"+calendarMonth.getMonth(), Toast.LENGTH_SHORT).show();
    }
}
