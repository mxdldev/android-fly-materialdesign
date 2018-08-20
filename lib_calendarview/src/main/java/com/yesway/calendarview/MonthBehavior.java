package com.yesway.calendarview;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

public class MonthBehavior extends CoordinatorLayout.Behavior<MonthViewPager> {

    public MonthBehavior() {
    }

    public MonthBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, MonthViewPager child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    private void scrollProperly(MonthViewPager view, int dy) {
        float destY = ViewCompat.getTranslationY(view) - dy;
        if(destY > 0) {
            destY = 0;
        } else {
            int minimumY = -view.getMaximumScrollRange();

            if (destY < minimumY)
                destY = minimumY;
        }

        ViewCompat.setTranslationY(view, destY);
//        Log.d("month_behavior1", "translationY=" + ViewCompat.getTranslationY(view));
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, MonthViewPager child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        scrollProperly(child, dyUnconsumed);
    }
}
