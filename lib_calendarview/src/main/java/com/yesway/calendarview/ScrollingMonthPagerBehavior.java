package com.yesway.calendarview;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

public class ScrollingMonthPagerBehavior extends CoordinatorLayout.Behavior<View> {
    private MonthViewPager monthViewPager;
    private Scroller scroller;
    private Scroller scrollerOfM;
    private Handler handler = new Handler();
    private boolean isCollapsed = false;
    private OnStateChangeListener li;

    public ScrollingMonthPagerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        scrollerOfM = new Scroller(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (dependency instanceof MonthViewPager) {
            monthViewPager = (MonthViewPager) dependency;
            return true;
        }
        return false;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        int bottom = monthViewPager.getBottom();
        child.layout(0, bottom, child.getMeasuredWidth(), bottom + child.getMeasuredHeight());
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, final View child, View directTargetChild, View target, int nestedScrollAxes) {
        scroller.abortAnimation();
        scrollerOfM.abortAnimation();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!monthViewPager.isMonthMode()) {
                    final int translate = -monthViewPager.getMaximumScrollRange();
                    monthViewPager.setMonthMode();
                    ViewCompat.setTranslationY(monthViewPager, translate);
                    ViewCompat.setTranslationY(child, getTargetMaxTransY());
//                    Log.i("month_behavior0", "translationY=" + ViewCompat.getTranslationY(monthViewPager));
                }
            }
        });

        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        float destY = ViewCompat.getTranslationY(child) - dyUnconsumed;
        if (destY > 0) {
            destY = 0;
        } else {
            int minimumY = getTargetMaxTransY();

            if (destY < minimumY)
                destY = minimumY;
        }

        ViewCompat.setTranslationY(child, destY);
    }

    /**
     * The maximum translationY target child can translate top.
     *
     * @return maximum translationY
     */
    private int getTargetMaxTransY() {
        return -(monthViewPager.getShouldHeightInMonthMode() - monthViewPager.getShouldHeightInWeekMode());
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        int minimumY = getTargetMaxTransY();
        float translationY = ViewCompat.getTranslationY(child);

        int minimumYOfM = -monthViewPager.getMaximumScrollRange();
        float translationYOfM = ViewCompat.getTranslationY(monthViewPager);

        if((!isCollapsed && translationY < (minimumY / 3))
            ||(isCollapsed && translationY < (2 * minimumY / 3))) {
            int dy = (int) (minimumY - translationY);
            int duration = calculateDuration(Math.abs(dy), Math.abs(minimumY));
            scroller.startScroll(0, (int) translationY, 0, dy, duration);
            scrollerOfM.startScroll(0, (int) translationYOfM, 0, (int) (minimumYOfM - translationYOfM), duration);
        } else {
            int dy = (int) (-translationY);
            int duration = calculateDuration(Math.abs(dy), Math.abs(minimumY));
            scroller.startScroll(0, (int) translationY, 0, dy, duration);
            scrollerOfM.startScroll(0, (int) translationYOfM, 0, (int) (-translationYOfM), duration);
        }

        handler.post(new Running(child));
    }

    private int calculateDuration(int distance, int max) {
        int maxDuration = 600;
        float scale = (float) distance / max;
        return (int) (maxDuration * scale);
    }

    public void setOnStateChangeListener(OnStateChangeListener listener) {
        this.li = listener;
    }

    private class Running implements Runnable {
        private View target;

        Running(View target) {
            this.target = target;
        }

        @Override
        public void run() {
            boolean process;
            boolean processOfM;
            if(process = scroller.computeScrollOffset()) {
                ViewCompat.setTranslationY(target, scroller.getCurrY());
            }
            if(processOfM = scrollerOfM.computeScrollOffset()) {
                ViewCompat.setTranslationY(monthViewPager, scrollerOfM.getCurrY());
            }

            if(process || processOfM) {
                handler.post(this);
            } else {
                boolean oldCollapseState = isCollapsed;
                boolean newCollapseState = ViewCompat.getTranslationY(target) == getTargetMaxTransY();

                // always change to week mode when collapsed
                if(newCollapseState) {
                    monthViewPager.setWeekMode();
                    ViewCompat.setTranslationY(monthViewPager, 0);
                    // height of monthViewPager will changed on next layout step, so reset translationY
                    // of target
                    ViewCompat.setTranslationY(target, 0);
//                    Log.i("month_behavior3", "translationY=" + ViewCompat.getTranslationY(monthViewPager));
                }

                if (newCollapseState == oldCollapseState) {
                    // state not changed.
                    return;
                }

                if (newCollapseState) {
                    if(li != null)
                        li.onCollapsed();
                    isCollapsed = true;
                } else {
                    if(li != null)
                        li.onExpanded();
                    isCollapsed = false;
                }
            }
        }
    }

    public interface OnStateChangeListener {
        void onExpanded();
        void onCollapsed();
    }
}
