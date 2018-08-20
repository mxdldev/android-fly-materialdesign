package com.yesway.calendardemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.yesway.calendarview.CalendarMonth;
import com.yesway.calendarview.MonthView;
import com.yesway.calendarview.YMTransitRootView;
import com.yesway.calendarview.YearView;
import com.yesway.calendarview.month.ScrollerMonthAdapter;
import com.yesway.calendarview.month.ScrollerMonthController;
import com.yesway.calendarview.month.ScrollerMonthView;
import com.yesway.calendarview.month.ScrollerMonthViewClickListener;
import com.yesway.calendarview.year.ScrollerYearView;
import com.yesway.calendarview.year.ScrollerYearViewClickListener;

import java.io.File;

/**
 * Created by gxl on 2017/9/18.
 */

public class ScrollerComplexActivity extends AppCompatActivity {
    public static final String TAG = ScrollerComplexActivity.class.getSimpleName();
    private ScrollerYearView mScrollerYearView;
    private ScrollerMonthView mScrollerMonthView;
    private YMTransitRootView mRootView;
    private LinearLayout mLinearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller_complex);

        mRootView = (YMTransitRootView) findViewById(R.id.trv);
        mLinearLayout = (LinearLayout) findViewById(R.id.layout_month_view);

        mScrollerYearView = (ScrollerYearView) findViewById(R.id.scrollYearView);
        mScrollerMonthView = (ScrollerMonthView) findViewById(R.id.scrollMonthView);

        mRootView.setRecycleViewYear(mScrollerYearView);
        mRootView.setRecyclerViewMonth(mLinearLayout);

        mScrollerYearView.setVisibility(View.GONE);
        mScrollerYearView.setYearViewClickListener(new ScrollerYearViewClickListener() {
            @Override
            public void onMonthOfYearSelected(final YearView yearView, final CalendarMonth calendarMonth) {
                mLinearLayout.setVisibility(View.VISIBLE);
                mLinearLayout.setAlpha(0);
                Log.v(TAG,"calendarMonth:"+calendarMonth.toString());
                mScrollerMonthView.scrollToMonth(calendarMonth.getYear(),calendarMonth.getMonth());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            int currPosition = mScrollerMonthView.getCurrPosition();
                            if(currPosition >= 0){
                                ScrollerMonthAdapter.ViewHolder viewHolderForLayoutPosition = (ScrollerMonthAdapter.ViewHolder) mScrollerMonthView.findViewHolderForPosition(currPosition);
                                //ScrollerMonthAdapter.ViewHolder viewHolderForLayoutPosition = (ScrollerMonthAdapter.ViewHolder) mScrollerMonthView.findViewHolderForLayoutPosition(mScrollerMonthView.getCurrPosition());
                                // ScrollerMonthAdapter.ViewHolder viewHolderForLayoutPosition = (ScrollerMonthAdapter.ViewHolder) mScrollerMonthView.findViewHolderForAdapterPosition(mScrollerMonthView.getCurrPosition());
                                if( viewHolderForLayoutPosition != null && viewHolderForLayoutPosition.simpleMonthView != null){
                                    MonthView monthView = viewHolderForLayoutPosition.simpleMonthView;
                                    mRootView.assignView(yearView, monthView);
                                }
                            }
                                                   }
                    },500);

            }
        });
        mScrollerMonthView.setController(new ScrollerMonthController() {
            @Override
            public int getMaxYear() {
                return 2017;
            }

            @Override
            public int getMinYear() {
                return 2016;
            }

            @Override
            public int getCurrentYear() {
                return 2017;
            }

        });
        mScrollerMonthView.setViewClickListener(new ScrollerMonthViewClickListener() {
            @Override
            public void onDayOfMonthSelected(int year, int month, int day) {
            }
            @Override
            public void onDateRangeSelected(ScrollerMonthAdapter.SelectedDays<ScrollerMonthAdapter.CalendarDay> selectedDays) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (!mRootView.applyHide())
           super.onBackPressed();
    }
}
