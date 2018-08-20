package com.yesway.calendarview.year;

import com.yesway.calendarview.CalendarMonth;
import com.yesway.calendarview.R;
import com.yesway.calendarview.YearView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class ScrollerYearView extends RecyclerView
{
	private ScrollerYearAdapter yearAdapter;
    private TypedArray typedArray;
    private OnScrollListener onScrollListener;
    private ScrollerYearViewClickListener mScrollerYearViewClickListener;

    public ScrollerYearView(Context context)
    {
        this(context, null);
    }

    public ScrollerYearView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    @SuppressLint("Recycle")
	public ScrollerYearView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if (!isInEditMode())
        {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.YearView);;
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            init(context);
        }
    }
    
	public void init(Context paramContext) {
        setLayoutManager(new LinearLayoutManager(paramContext));
		setUpListView();

        onScrollListener = new OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
            	
                super.onScrolled(recyclerView, dx, dy);
                final YearView child = (YearView) recyclerView.getChildAt(0);
                if (child == null) {
                    return;
                }
            }
        };
       
	}
	
    public void setYearViewClickListener(ScrollerYearViewClickListener scrollerYearViewClickListener)
    {
        this.mScrollerYearViewClickListener = scrollerYearViewClickListener;
        setUpAdapter();
        setAdapter(yearAdapter);
    }

    public CalendarMonth getSelectedMonths()
    {
        return yearAdapter.getSelectedMonths();
    }

    public ScrollerYearViewClickListener getYearViewClickListener()
    {
        return mScrollerYearViewClickListener;
    }

	private void setUpAdapter() {
		if (yearAdapter == null) {
			yearAdapter = new ScrollerYearAdapter(getContext(), mScrollerYearViewClickListener, typedArray);
        }
		scrollToPosition(yearAdapter.getYearRange() / 2);
		yearAdapter.notifyDataSetChanged();
	}

	private void setUpListView() {
		setVerticalScrollBarEnabled(false);
		setOnScrollListener(onScrollListener);
		setFadingEdgeLength(0);
	}
}