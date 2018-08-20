/***********************************************************************************
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2014 Robin Chutaux
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ***********************************************************************************/
package com.yesway.calendarview.month;

import com.yesway.calendarview.MonthView;
import com.yesway.calendarview.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class ScrollerMonthView extends RecyclerView {
    protected Context mContext;
    protected ScrollerMonthAdapter mAdapter;
    private ScrollerMonthController mController;
    private ScrollerMonthViewClickListener mViewClickListener;
    protected int mCurrentScrollState = 0;
    protected long mPreviousScrollPosition;
    protected int mPreviousScrollState = 0;
    private TypedArray typedArray;
    private OnScrollListener onScrollListener;
    private int mCurrPosition;

    public ScrollerMonthView(Context context) {
        this(context, null);
    }

    public ScrollerMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollerMonthView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.MonthView);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            init(context);
        }
    }
    protected void init(Context paramContext) {
        setLayoutManager(new LinearLayoutManager(paramContext));
        mContext = paramContext;
        setUpListView();
        onScrollListener = new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final MonthView child = (MonthView) recyclerView.getChildAt(0);
                if (child == null) {
                    return;
                }

                mPreviousScrollPosition = dy;
                mPreviousScrollState = mCurrentScrollState;
            }
        };
    }
    //初始化默认滚动效果
    protected void setUpListView() {
        setVerticalScrollBarEnabled(false);
        setOnScrollListener(onScrollListener);
        setFadingEdgeLength(0);
    }
    //初始化数据适配器
    protected void setUpAdapter() {
        if (mAdapter == null) {
            mAdapter = new ScrollerMonthAdapter(getContext(), mController, typedArray);
            mAdapter.setListener(mViewClickListener);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void setController(ScrollerMonthController mController) {
        this.mController = mController;
        setUpAdapter();
        scrollToCurrentYear();
        setAdapter(mAdapter);
    }

    public void setViewClickListener(ScrollerMonthViewClickListener viewClickListener) {
        mViewClickListener = viewClickListener;
    }

    //滚动到当前的年
    protected void scrollToCurrentYear() {
        mController.getMaxYear();
        mController.getMinYear();

        int currentYear = mController.getCurrentYear();
        if (currentYear != -1) {
            scrollToMonth(currentYear, mAdapter.getFirstMonth());
        }
    }

    public void scrollToMonth(int year, int month) {
        if (mController != null && mAdapter != null) {
            //int position = ((year - mController.getMinYear()) * ScrollerMonthAdapter.MONTHS_IN_YEAR)
                   // + (mAdapter.getFirstMonth() - month);
            int position = ((year - mController.getMinYear()) * ScrollerMonthAdapter.MONTHS_IN_YEAR)
                    +  month - 1;
            mCurrPosition = position;
            if (position >= 0) {
                scrollToPosition(position);
            }
        } else {
            throw new UnsupportedOperationException("Do you specified a ScrollerMonthController?");
        }
    }

    //获取所选的日期范围
    public ScrollerMonthAdapter.SelectedDays<ScrollerMonthAdapter.CalendarDay> getSelectedDays() {
        return mAdapter.getSelectedDays();
    }

    protected ScrollerMonthController getController() {
        return mController;
    }

    protected TypedArray getTypedArray() {
        return typedArray;
    }

    @Override
    public ScrollerMonthAdapter getAdapter() {
        return mAdapter;
    }

    public int getCurrPosition() {
        return mCurrPosition;
    }
}