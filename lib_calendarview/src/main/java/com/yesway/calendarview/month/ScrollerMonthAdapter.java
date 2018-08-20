/***********************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Robin Chutaux
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ***********************************************************************************/
package com.yesway.calendarview.month;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.yesway.calendarview.MonthView;
import com.yesway.calendarview.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScrollerMonthAdapter extends RecyclerView.Adapter<ScrollerMonthAdapter.ViewHolder> {
  public static final String TAG = ScrollerMonthAdapter.class.getSimpleName();
  protected static final int MONTHS_IN_YEAR = 12;
  private final TypedArray typedArray;
  private final Context mContext;
  private final ScrollerMonthController mController;
  private ScrollerMonthViewClickListener mListener;
  private final Calendar calendar;
  private final SelectedDays<CalendarDay> selectedDays;
  private final Integer firstMonth = 0;// 起始月边界
  private final Integer lastMonth = 11;// 结束月边界
  private int mMaxYear;
  private int mMinYear;


  public ScrollerMonthAdapter(Context context, ScrollerMonthController datePickerController,
                              TypedArray typedArray) {
    this.typedArray = typedArray;
    calendar = Calendar.getInstance();
    // firstMonth = typedArray.getInt(R.styleable.DayPickerView_firstMonth,
    // calendar.get(Calendar.MONTH));
    // lastMonth = typedArray.getInt(R.styleable.DayPickerView_lastMonth,
    // (calendar.get(Calendar.MONTH) - 1) % MONTHS_IN_YEAR);
    selectedDays = new SelectedDays<>();
    mContext = context;
    mController = datePickerController;
    init();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    // final SimpleMonthView simpleMonthView = new SimpleMonthView(mContext, typedArray);
    final LinearLayout monthView =
        (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.calendar_monthview_scroll, null);
    // MonthView monthView = new MonthView(typedArray,mContext);
    return new ViewHolder(monthView);
  }

  @Override
  public void onBindViewHolder(ViewHolder viewHolder, int position) {
    final MonthView monthView = viewHolder.simpleMonthView;
    final HashMap<String, Integer> drawingParams = new HashMap<String, Integer>();
    int month;
    int year;
    month = (firstMonth + (position % MONTHS_IN_YEAR)) % MONTHS_IN_YEAR;
    year = position / MONTHS_IN_YEAR + mMinYear
        + ((firstMonth + (position % MONTHS_IN_YEAR)) / MONTHS_IN_YEAR);
    monthView.reuse();
    com.yesway.calendarview.CalendarDay calendarDay = new com.yesway.calendarview.CalendarDay();
    calendarDay.setDay(year, month + 1, 1);
    monthView.setToday(calendarDay);
    monthView.setYearAndMonth(calendarDay.getCalendarMonth());

    viewHolder.txtMonthViewTitle.setText((month+1)+"月");
    // monthView.showMonthTitle(true);
    // monthView.showWeekLabel(true);
    // monthView.setDecors(calendarDay);
    monthView.invalidate();
  }

  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getItemCount() {
    int itemCount = (((mMaxYear - mMinYear) + 1) * MONTHS_IN_YEAR);

    if (firstMonth != -1) itemCount -= firstMonth;

    if (lastMonth != -1) itemCount -= (MONTHS_IN_YEAR - lastMonth) - 1;

    return itemCount;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public MonthView simpleMonthView;
    TextView txtMonthViewTitle;
    TextView txtMonthViewDistance;
    TextView txtMonthViewPrice;
    TextView txtMonthViewOil;

    public ViewHolder(View itemView) {
      super(itemView);
      simpleMonthView = (MonthView) itemView.findViewById(R.id.monthview_scroll);

      txtMonthViewTitle = (TextView) itemView.findViewById(R.id.txt_monthview_title);
      txtMonthViewDistance = (TextView) itemView.findViewById(R.id.txt_monthview_distance);
      txtMonthViewPrice = (TextView) itemView.findViewById(R.id.txt_monthview_price);
      txtMonthViewOil = (TextView) itemView.findViewById(R.id.txt_monthview_oil);

      simpleMonthView.setLayoutParams(
          new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
      simpleMonthView.setClickable(true);
      simpleMonthView.setOnSelectionChangeListener(new MonthView.OnSelectionChangeListener() {
        @Override
        public void onSelectionChanged(MonthView monthView,
                                       @Nullable com.yesway.calendarview.CalendarDay now,
                                       @Nullable com.yesway.calendarview.CalendarDay old, boolean byUser) {

          //Intent intent = new Intent(mContext,)
        }
      });


    }
  }

  protected void init() {
    if (typedArray.getBoolean(R.styleable.ScrollerMonthView_currentDaySelected, false)) {
      onDayTapped(new CalendarDay(System.currentTimeMillis()));
    }

    mMinYear = mController.getMinYear();
    mMaxYear = mController.getMaxYear();

    if (mMaxYear < mMinYear) {
      int temp = mMaxYear;
      mMaxYear = mMinYear;
      mMinYear = temp;
    }
  }

  public void onDayClick(MonthView simpleMonthView, CalendarDay calendarDay) {
    if (calendarDay != null) {
      onDayTapped(calendarDay);
    }
  }

  protected void onDayTapped(CalendarDay calendarDay) {
    if (mListener != null) {
      mListener.onDayOfMonthSelected(calendarDay.year, calendarDay.month, calendarDay.day);
    }
    setSelectedDay(calendarDay);
  }

  public void setSelectedDay(CalendarDay calendarDay) {
    if (mListener == null) {
      return;
    }
    if (selectedDays.getFirst() != null && selectedDays.getLast() == null) {
      selectedDays.setLast(calendarDay);

      if (selectedDays.getFirst().month < calendarDay.month) {
        for (int i = 0; i < selectedDays.getFirst().month - calendarDay.month - 1; ++i)
          mListener.onDayOfMonthSelected(selectedDays.getFirst().year,
              selectedDays.getFirst().month + i, selectedDays.getFirst().day);
      }

      mListener.onDateRangeSelected(selectedDays);
    } else if (selectedDays.getLast() != null) {
      selectedDays.setFirst(calendarDay);
      selectedDays.setLast(null);
    } else
      selectedDays.setFirst(calendarDay);

    notifyDataSetChanged();
  }

  public int getFirstMonth() {
    return firstMonth;
  }

  public static class CalendarDay implements Serializable {
    private static final long serialVersionUID = -5456695978688356202L;
    private Calendar calendar;

    int day;
    int month;
    int year;

    public CalendarDay() {
      setTime(System.currentTimeMillis());
    }

    public CalendarDay(int year, int month, int day) {
      setDay(year, month, day);
    }

    public CalendarDay(long timeInMillis) {
      setTime(timeInMillis);
    }

    public CalendarDay(Calendar calendar) {
      year = calendar.get(Calendar.YEAR);
      month = calendar.get(Calendar.MONTH);
      day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void setTime(long timeInMillis) {
      if (calendar == null) {
        calendar = Calendar.getInstance();
      }
      calendar.setTimeInMillis(timeInMillis);
      month = this.calendar.get(Calendar.MONTH);
      year = this.calendar.get(Calendar.YEAR);
      day = this.calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void set(CalendarDay calendarDay) {
      year = calendarDay.year;
      month = calendarDay.month;
      day = calendarDay.day;
    }

    public void setDay(int year, int month, int day) {
      this.year = year;
      this.month = month;
      this.day = day;
    }

    public Date getDate() {
      if (calendar == null) {
        calendar = Calendar.getInstance();
      }
      calendar.set(year, month, day);
      return calendar.getTime();
    }

    @Override
    public String toString() {
      final StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("{ year: ");
      stringBuilder.append(year);
      stringBuilder.append(", month: ");
      stringBuilder.append(month);
      stringBuilder.append(", day: ");
      stringBuilder.append(day);
      stringBuilder.append(" }");

      return stringBuilder.toString();
    }
  }

  public SelectedDays<CalendarDay> getSelectedDays() {
    return selectedDays;
  }

  public static class SelectedDays<K> implements Serializable {
    private static final long serialVersionUID = 3942549765282708376L;
    private K first;
    private K last;

    public K getFirst() {
      return first;
    }

    public void setFirst(K first) {
      this.first = first;
    }

    public K getLast() {
      return last;
    }

    public void setLast(K last) {
      this.last = last;
    }
  }

  public void setListener(ScrollerMonthViewClickListener listener) {
    mListener = listener;
  }
}
