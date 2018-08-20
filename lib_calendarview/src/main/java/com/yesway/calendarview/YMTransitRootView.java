package com.yesway.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class YMTransitRootView extends FrameLayout {
  public static final String TAG = YMTransitRootView.class.getSimpleName();
  private final int DEFAULT_Y_TIME = 300;
  private final int DEFAULT_M_TIME = 300;
  private final int DEFAULT_BASE_TRANSIT_TIME = 300;
  private MonthView transitView;// 隐藏的view
  RecyclerView mRecycleViewYear;
  LinearLayout mRecyclerViewMonth;
  boolean mReceiveEvent = true;
  private final int y2m_interpolator;
  private final int m2y_interpolator;
  private final int y_anim_dura;
  private final int m_anim_dura;
  private final int transit_dura;
  private YMTransformer transformer;
  private OnTransitListener transitListener;

  public interface OnTransitListener {
    void onY2MTransitStart(AnimTransiter transiter, YearView yearView, MonthView monthView);

    void onY2MTransitEnd(AnimTransiter transiter, YearView yearView, MonthView monthView);

    void onM2YTransitStart(AnimTransiter transiter, YearView yearView, MonthView monthView);

    void onM2YTransitEnd(AnimTransiter transiter, YearView yearView, MonthView monthView);
  }

  public YMTransitRootView(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TransitRootView);
    y2m_interpolator = typedArray.getResourceId(R.styleable.TransitRootView_y2m_interpolator,
        android.R.interpolator.decelerate_quint);
    m2y_interpolator = typedArray.getResourceId(R.styleable.TransitRootView_m2y_interpolator,
        android.R.interpolator.decelerate_quad);
    y_anim_dura =
        typedArray.getInteger(R.styleable.TransitRootView_y_anim_duration, DEFAULT_Y_TIME);
    m_anim_dura =
        typedArray.getInteger(R.styleable.TransitRootView_m_anim_duration, DEFAULT_M_TIME);
    transit_dura = typedArray.getInteger(R.styleable.TransitRootView_transit_base_duration,
        DEFAULT_BASE_TRANSIT_TIME);

    typedArray.recycle();
    init();
  }

  private void init() {
    transitView = new MonthView(getContext());
    transitView.showMonthTitle(false);
    transitView.showWeekLabel(false);
    transitView.setVisibility(View.GONE);
    super.addView(transitView);
  }

  private void changeChildrenVisibility() {
    if (mRecycleViewYear.getVisibility() == View.VISIBLE) {
      mRecyclerViewMonth.setVisibility(View.GONE);
      return;
    }
    if (mRecyclerViewMonth.getVisibility() == View.VISIBLE) {
      mRecycleViewYear.setVisibility(View.GONE);
      return;
    }
    mRecycleViewYear.setVisibility(View.VISIBLE);
    mRecyclerViewMonth.setVisibility(View.GONE);
  }

  public void setReceiveEvent(boolean receive) {
    mReceiveEvent = receive;
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    return !mReceiveEvent || super.dispatchTouchEvent(ev);
  }

  protected MonthView useTransitView() {
    transitView.setVisibility(View.VISIBLE);
    return transitView;
  }

  protected void recycleTransitView() {
    transitView.setVisibility(View.GONE);
  }

  public void setOnTransitListener(OnTransitListener listener) {
    this.transitListener = listener;
  }

  public OnTransitListener getOnTransitListener() {
    return transitListener;
  }

  public void assignView(YearView yearView, MonthView monthView) {
    if (transformer == null) {
      transformer = new YMTransformer(this, yearView, monthView);
      assignTransitAttrs();
    } else {
      transformer.configYearMonth(yearView, monthView);
    }
    transformer.applyShow(monthView.getCurrentMonth().getMonth());
  }

  private void assignTransitAttrs() {
    transformer.setMDelayTime(m_anim_dura);
    transformer.setYDelayTime(y_anim_dura);
    transformer.setBaseTransitionTime(transit_dura);
    transformer
        .setShowInterpolator(AnimationUtils.loadInterpolator(getContext(), y2m_interpolator));
    transformer
        .setHideInterpolator(AnimationUtils.loadInterpolator(getContext(), m2y_interpolator));
  }

  public void applyShow(int month) {
    if (transformer != null) {
      transformer.applyShow(month);
    }
  }

  public boolean applyHide() {
    if (transformer != null) {
      return transformer.applyHide();
    }
    return false;
  }

  public RecyclerView getRecycleViewYear() {
    return mRecycleViewYear;
  }

  public LinearLayout getRecyclerViewMonth() {
    return mRecyclerViewMonth;
  }

  public void setRecycleViewYear(RecyclerView recycleViewYear) {
    mRecycleViewYear = recycleViewYear;
  }

  public void setRecyclerViewMonth(LinearLayout recyclerViewMonth) {
    mRecyclerViewMonth = recyclerViewMonth;
  }
}
