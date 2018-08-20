package com.yesway.calendarview.year;

import java.util.Calendar;
import java.util.HashMap;

import com.yesway.calendarview.CalendarMonth;
import com.yesway.calendarview.YearView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;


public class ScrollerYearAdapter extends RecyclerView.Adapter<ScrollerYearAdapter.ViewHolder>{
    protected static final int MONTHS_IN_YEAR = 12;
    private final TypedArray typedArray;
	private final Context context;
	private ScrollerYearViewClickListener mScrollerYearViewClickListener;
    private final Calendar calendar;
    protected CalendarMonth selectedMonth;

    private YearView currentYearView;
    private final int yearRange;

	public ScrollerYearAdapter(Context context, ScrollerYearViewClickListener scrollerYearViewClickListener, TypedArray typedArray) {
		yearRange = 200;
		this.typedArray = typedArray;
        this.context = context;
        calendar = Calendar.getInstance();
        selectedMonth = new CalendarMonth();
		mScrollerYearViewClickListener = scrollerYearViewClickListener;
	}
	
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final YearView yearView = new YearView(context, typedArray);
        return new ViewHolder(yearView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        final YearView yearView = viewHolder.yearView;
        final HashMap<String, Integer> drawingParams = new HashMap<String, Integer>();
        int currentYear;
        currentYear = 2017 + (position - yearRange / 2);
        yearView.reuse();
        drawingParams.put(YearView.VIEW_PARAMS_YEAR_CURRENT, currentYear);
        drawingParams.put(YearView.VIEW_PARAMS_WEEK_START, calendar.getFirstDayOfWeek());
        yearView.setYearParams(drawingParams);
        yearView.invalidate();
        
        currentYearView = yearView;
    }

    public long getItemId(int position) {
		return position;
	}
    
    public YearView getYearView(){
    	return currentYearView;
    }

    @Override
    public int getItemCount()
    {
        return yearRange;
    }

	protected void onMonthTapped(CalendarMonth calendarMonth) {
		setSelectedMonth(calendarMonth);
		notifyDataSetChanged();
	}

	public void setSelectedMonth(CalendarMonth calendarMonth) {
		selectedMonth = calendarMonth;
		notifyDataSetChanged();
	}
	
	public CalendarMonth getSelectedMonths() {
		return selectedMonth;
	}
	
	public int getYearRange(){
		return yearRange;
	}

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        final YearView yearView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            yearView = (YearView) itemView;
            yearView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            yearView.setClickable(true);
            yearView.setOnMonthClickListener(new YearView.OnMonthClickListener(){

                @Override
                public void onMonthClick(YearView yearView, com.yesway.calendarview.CalendarMonth calendarMonth) {
                    selectedMonth = calendarMonth;
                    if(mScrollerYearViewClickListener != null){
                        mScrollerYearViewClickListener.onMonthOfYearSelected(yearView,calendarMonth);
                    }
                }
            });
        }
    }




//	public static class CalendarMonth implements Serializable
//    {
//        private static final long serialVersionUID = -5456695978688356202L;
//        private Calendar calendar;
//
//		int month;
//		int year;
//
//
//		public CalendarMonth() {
//			setTime(System.currentTimeMillis());
//		}
//
//		public CalendarMonth(int year, int month) {
//			setDay(year, month);
//		}
//
//		public CalendarMonth(int year, int month,MotionEvent event) {
//			setDay(year, month);
//		}
//
//		public CalendarMonth(long timeInMillis) {
//			setTime(timeInMillis);
//		}
//
//		public CalendarMonth(Calendar calendar) {
//			year = calendar.get(Calendar.YEAR);
//			month = calendar.get(Calendar.MONTH);
//		}
//
//		private void setTime(long timeInMillis) {
//			if (calendar == null) {
//				calendar = Calendar.getInstance();
//            }
//			calendar.setTimeInMillis(timeInMillis);
//			month = this.calendar.get(Calendar.MONTH);
//			year = this.calendar.get(Calendar.YEAR);
//		}
//
//		public void set(CalendarMonth calendarDay) {
//		    year = calendarDay.year;
//			month = calendarDay.month;
//		}
//
//		public void setDay(int year, int month) {
//			this.year = year;
//			this.month = month;
//		}
//
//        public Date getDate()
//        {
//            if (calendar == null) {
//                calendar = Calendar.getInstance();
//            }
//            calendar.set(year, month);
//            return calendar.getTime();
//        }
//
//
//        @Override
//        public String toString()
//        {
//            final StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("{ year: ");
//            stringBuilder.append(year);
//            stringBuilder.append(", month: ");
//            stringBuilder.append(month);
//            stringBuilder.append(" }");
//
//            return stringBuilder.toString();
//        }
//    }

   

}