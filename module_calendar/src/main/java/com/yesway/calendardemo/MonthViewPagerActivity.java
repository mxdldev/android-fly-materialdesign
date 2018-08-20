package com.yesway.calendardemo;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yesway.calendardemo.control.GetDecorsTask;
import com.yesway.calendardemo.model.DayEvent;
import com.yesway.calendarview.CalendarDay;
import com.yesway.calendarview.CalendarMonth;
import com.yesway.calendarview.DayDecor;
import com.yesway.calendarview.MonthView;
import com.yesway.calendarview.MonthViewPager;

import java.util.List;

public class MonthViewPagerActivity extends AppCompatActivity {
  private MonthViewPager monthViewPager;
  private TextView textView;
  private ProgressDialog progressDialog;
  private List<DayEvent> yearEvents;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_month_view_pager);
    // find view
    monthViewPager = (MonthViewPager) findViewById(R.id.mvp);
    monthViewPager.setMonthMode();

    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_dayview);
    viewPager.setAdapter(new PagerAdapter() {
      @Override
      public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
      }

      // 有多少个切换页
      @Override
      public int getCount() {
        return 1;
      }

      // 对超出范围的资源进行销毁
      @Override
      public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
        // container.removeView(views.get(position));
      }

      // 对显示的资源进行初始化
      @Override
      public Object instantiateItem(ViewGroup container, int position) {
        // return super.instantiateItem(container, position);
        View view = LayoutInflater.from(MonthViewPagerActivity.this)
            .inflate(R.layout.calendar_dayview_include, null);
        container.addView(view);
        return view;
      }
    });
    textView = (TextView) findViewById(R.id.tv);

    init();
    // request all events(daemon)
    getEvents();
  }

  private void init() {
    monthViewPager.setCurrentMonth(new CalendarMonth(2016, 3));
    monthViewPager.setToday(new CalendarDay(2016, 3, 12));
    monthViewPager.setMonthRange(new CalendarMonth(2015, 12), new CalendarMonth(2017, 2));
    monthViewPager.addOnMonthChangeListener(new MonthViewPager.OnMonthChangeListener() {
      @Override
      public void onMonthChanged(MonthViewPager monthViewPager, MonthView previous,
          MonthView current, MonthView next, CalendarMonth currentMonth, CalendarMonth old) {
        Log.d("onMonthChanged",
            "old=" + old.toString() + ";current=" + currentMonth.toString() + ";left="
                + (previous == null ? "null" : previous.getCurrentMonth().toString()) + ";right="
                + (next == null ? "null" : next.getCurrentMonth().toString()));
      }
    });
    monthViewPager.setOnSelectionChangeListener(new MonthView.OnSelectionChangeListener() {
      @Override
      public void onSelectionChanged(MonthView monthView, CalendarDay now, CalendarDay old,
          boolean byUser) {
        for (DayEvent event : yearEvents) {
          if (event.isThisDay(now)) {
            textView.setText(String.format("Today is \n%s\nToday have %d events", now.toString(),
                event.getEventDetails().length));
            return;
          }
        }
        textView.setText(R.string.no_event);
      }
    });
    monthViewPager.setOnMonthTitleClickListener(new MonthView.OnMonthTitleClickListener() {
      @Override
      public void onMonthClick(MonthView monthView, CalendarMonth calendarMonth) {
        monthViewPager.setCurrentMonth(new CalendarMonth(2017, 2));
      }
    });
    monthViewPager.setOnDragListener(new MonthViewPager.OnDragListener() {
      @Override
      public void onDrag(MonthView middle, int left, int dx) {
        Log.d("OnDragListener", "left==" + left + ";dx==" + dx);
      }
    });
  }

  private void getEvents() {
    new GetDecorsTask(new GetDecorsTask.DecorResult() {
      @Override
      public void onStart() {
        progressDialog =
            ProgressDialog.show(MonthViewPagerActivity.this, null, "loading...", false, false);
      }

      @Override
      public void onResult(List<DayEvent> events) {
        yearEvents = events;

        DayDecor dayDecor = new DayDecor();
        for (DayEvent event : yearEvents) {
          CalendarDay calendarDay =
              new CalendarDay(event.getYear(), event.getMonth(), event.getDay());
          dayDecor.putOne(calendarDay, event.getType().getColor());
        }
        monthViewPager.setDecors(dayDecor);

        progressDialog.dismiss();
      }
    }).execute(2016);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    //getMenuInflater().inflate(R.menu.mvp_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.item1) {
      DayDecor.Style style = new DayDecor.Style();
      style.setBold(true);
      style.setTextSize(getResources().getDimensionPixelSize(R.dimen.big_text));
      style.setPureColorBg(Color.BLACK);
      monthViewPager.setSelectionStyle(style);
    } else if (item.getItemId() == R.id.item2) {
      monthViewPager
          .setSelection(new CalendarDay(monthViewPager.getCurrentChild().getCurrentMonth(), 1));
    } else if (item.getItemId() == R.id.item3) {
      monthViewPager.setCurrentMonth(new CalendarMonth(2016, 1));
    }
    // switch (item.getItemId()) {
    // case R.id.item1:
    // DayDecor.Style style = new DayDecor.Style();
    // style.setBold(true);
    // style.setTextSize(getResources().getDimensionPixelSize(R.dimen.big_text));
    // style.setPureColorBg(Color.BLACK);
    // monthViewPager.setSelectionStyle(style);
    // break;
    // case R.id.item2:
    // monthViewPager.setSelection(new
    // CalendarDay(monthViewPager.getCurrentChild().getCurrentMonth(), 1));
    // break;
    // case R.id.item3:
    // monthViewPager.setCurrentMonth(new CalendarMonth(2016, 1));
    // break;
    // }
    return super.onOptionsItemSelected(item);
  }
}
