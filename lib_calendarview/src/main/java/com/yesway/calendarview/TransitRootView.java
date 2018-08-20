package com.yesway.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

public class TransitRootView extends FrameLayout {
    public static final String TAG = TransitRootView.class.getSimpleName();
    private final int DEFAULT_Y_TIME = 300;
    private final int DEFAULT_M_TIME = 300;
    private final int DEFAULT_BASE_TRANSIT_TIME = 300;
    private MonthView transitView;//隐藏的view
    View child1;
    View child2;
    boolean mReceiveEvent = true;
    private final int y2m_interpolator;
    private final int m2y_interpolator;
    private final int y_anim_dura;
    private final int m_anim_dura;
    private final int transit_dura;
    private YearMonthTransformer transformer;
    private OnTransitListener transitListener;

    /**
     * transition listener. use this to implement animations of your custom views
     * to show in or show out. You can also make some initialization operation.
     */
    public interface OnTransitListener {
        /**
         * When YearView to MonthView transit process start.
         * use this to animate views to show out in YearView layout.
         * @param transiter animation helpers
         * @param yearView yearView
         * @param monthView monthView
         */
        void onY2MTransitStart(AnimTransiter transiter, YearView yearView, MonthView monthView);
        /**
         * When YearView to MonthView transit process finished.
         * use this to animate views to show in in MonthView layout.
         * @param transiter animation helpers
         * @param yearView yearView
         * @param monthView monthView
         */
        void onY2MTransitEnd(AnimTransiter transiter, YearView yearView, MonthView monthView);
        /**
         * When MonthView to YearView transit process start.
         * use this to animate views to show out in MonthView layout.
         * @param transiter animation helpers
         * @param yearView yearView
         * @param monthView monthView
         */
        void onM2YTransitStart(AnimTransiter transiter, YearView yearView, MonthView monthView);
        /**
         * When MonthView to YearView transit process finished.
         * use this to animate views to show in in YearView layout.
         * @param transiter animation helpers
         * @param yearView yearView
         * @param monthView monthView
         */
        void onM2YTransitEnd(AnimTransiter transiter, YearView yearView, MonthView monthView);
    }

    public TransitRootView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TransitRootView);
        y2m_interpolator = typedArray.getResourceId(R.styleable.TransitRootView_y2m_interpolator, android.R.interpolator.decelerate_quint);
        m2y_interpolator = typedArray.getResourceId(R.styleable.TransitRootView_m2y_interpolator, android.R.interpolator.decelerate_quad);
        y_anim_dura = typedArray.getInteger(R.styleable.TransitRootView_y_anim_duration, DEFAULT_Y_TIME);
        m_anim_dura = typedArray.getInteger(R.styleable.TransitRootView_m_anim_duration, DEFAULT_M_TIME);
        transit_dura = typedArray.getInteger(R.styleable.TransitRootView_transit_base_duration, DEFAULT_BASE_TRANSIT_TIME);

        typedArray.recycle();
        init();
    }
    //创建了一个隐藏的MonthView
    private void init() {
        transitView = new MonthView(getContext());
        transitView.showMonthTitle(false);
        transitView.showWeekLabel(false);
        transitView.setVisibility(View.GONE);
        super.addView(transitView);
    }

    @Override
    public void addView(View child) {
        addView(child, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        cookChild(child);//初始化childView1、childView2
        super.addView(child, params);
    }

    private void cookChild(View child) {
        if(getChildCount() > 3) {
            throw new IllegalStateException("TransitRootView can host only two direct children in xml");
        }
        if(child1 == null) {
            child1 = child;
        } else {
            child2 = child;
            changeChildrenVisibility();
        }
    }

    // just one child visible at once, another set to gone.
    private void changeChildrenVisibility() {
        if(child1.getVisibility() == View.VISIBLE) {
            child2.setVisibility(View.GONE);
            return;
        }
        if(child2.getVisibility() == View.VISIBLE) {
            child1.setVisibility(View.GONE);
            return;
        }
        // no child is visible
        child1.setVisibility(View.VISIBLE);
        child2.setVisibility(View.GONE);
    }

    /**
     * if view should receive touch event.
     * @param receive false - receive nothing.
     */
    public void setReceiveEvent(boolean receive) {
        mReceiveEvent = receive;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return !mReceiveEvent || super.dispatchTouchEvent(ev);
    }

    /**
     * use a TransitView to show transition animation
     * @return a useable MonthView
     */
    protected MonthView useTransitView() {
        transitView.setVisibility(View.VISIBLE);
        return transitView;
    }

    /**
     * should call this to recycle the TransitView
     */
    protected void recycleTransitView() {
        transitView.setVisibility(View.GONE);
    }

    public void setOnTransitListener(OnTransitListener listener) {
        this.transitListener = listener;
    }

    public OnTransitListener getOnTransitListener() {
        return transitListener;
    }

    /**
     * assign YearView and MonthView to transit. It should be called before any other operations
     * @param yearView YearView
     * @param monthView MonthView
     */
    public void assignView(YearView yearView, MonthView monthView) {
        transformer = new YearMonthTransformer(this, yearView, monthView);
        assignTransitAttrs();
    }
    /**
     * assign YearView and MonthViewPager to transit. It should be called before any other operations
     * @param yearView YearView
     * @param monthViewPager MonthViewPager
     */
    public void assignView(YearView yearView, MonthViewPager monthViewPager) {
        transformer = new YearMonthTransformer(this, yearView, monthViewPager);
        assignTransitAttrs();
    }

    private void assignTransitAttrs() {
        transformer.setMDelayTime(m_anim_dura);
        transformer.setYDelayTime(y_anim_dura);
        transformer.setBaseTransitionTime(transit_dura);
        transformer.setShowInterpolator(AnimationUtils.loadInterpolator(getContext(), y2m_interpolator));
        transformer.setHideInterpolator(AnimationUtils.loadInterpolator(getContext(), m2y_interpolator));
    }

    public void applyShow(int month) {
        if(transformer == null)
            throw new IllegalStateException("call assignView() before this method");
        transformer.applyShow(month);
    }

    public boolean applyHide() {
        if(transformer == null)
            throw new IllegalStateException("call assignView() before this method");
        return transformer.applyHide();
    }

}
