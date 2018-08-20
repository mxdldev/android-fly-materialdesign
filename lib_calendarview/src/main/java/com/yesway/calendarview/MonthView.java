package com.yesway.calendarview;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MonthView extends View {
    private final int DEFAULT_NUM_ROWS = 6;
    protected int dayCircleRadius;
    protected int SPACE_BETWEEN_WEEK_AND_DAY = 0;
    protected int normalDayTextSize;
    protected int WEEK_LABEL_TEXT_SIZE;
    protected int MONTH_HEADER_HEIGHT;
    protected int WEEK_LABEL_HEIGHT;
    protected int MONTH_LABEL_TEXT_SIZE;
    protected int mSelectedCircleColor;
    protected int spaceBetweenWeekAndDivider;
    protected int monthHeaderSizeCache;
    protected int weekLabelOffset = 0;
    protected int monthLabelOffset = 0;
    private int mOtherMonthTextColor;

    protected int mPadding = 0;

    private String mDayOfWeekTypeface;
    private String mMonthTitleTypeface;

    protected Paint mWeekLabelPaint;
    protected Paint mDayNumPaint;
    protected Paint mMonthTitlePaint;
    protected Paint mDayBgPaint;

    protected int decorTextColor;
    protected int mMonthTextColor;
    protected int mWeekColor;
    protected int normalDayTextColor;
    protected int todayTextColor;

    protected int mWeekStart = Calendar.SUNDAY;
    protected int mNumDays = 7;
    protected int mNumCells = mNumDays;
    private int mDayOfWeekStart = 0;
    private int mMonth = 0;
    protected int dayRowHeight = 0;
    protected int mWidth;
    private int mYear = 0;
    protected CalendarDay today;

    private int mNumRows = DEFAULT_NUM_ROWS;
    protected boolean mShowMonthTitle;
    protected boolean mShowWeekLabel;
    private boolean mShowWeekDivider;
    protected boolean mShowOtherMonth;

    private static final String DAY_OF_WEEK_FORMAT = "EEEEE";
    private OnSelectionChangeListener mOnSelectionChangeListener;
    private OnMonthTitleClickListener mOnMonthClicker;
    private CalendarDay selectedDay;
    private float downX;
    private float downY;
    private TypedArray mTypeArray;
    private boolean isCopy;
    private DayDecor mDecors;
    private float halfDayWidth;
    private DayDecor.Style todayStyle;
    private DayDecor.Style selectionStyle;
    private DayDecor.Style normalStyle;
    private DayDecor.Style otherMonthStyle;
    private Rect drawRect;
    private CalendarDay leftEdge;
    private CalendarDay rightEdge;
    private boolean mWeekMode;
    private int mWeekIndex = 0;

    public MonthView(Context context) {
        this(context, null);
    }
    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.MonthView);

        init(context, mTypeArray);
    }

    private MonthView(Context context, TypedArray typedArray, Void copy) {
        super(context);
        isCopy = true;
        init(context, typedArray);
    }

    private void init(Context context, TypedArray typedArray) {
        Resources resources = context.getResources();
        today = new CalendarDay(Calendar.getInstance());

        mDayOfWeekTypeface = resources.getString(R.string.sans_serif);
        mMonthTitleTypeface = resources.getString(R.string.sans_serif);
        decorTextColor = resources.getColor(R.color.day_label_decor_text_color);
        mMonthTextColor = typedArray.getColor(R.styleable.MonthView_monthTitleColor, resources.getColor(R.color.month_title_color));
        mWeekColor = typedArray.getColor(R.styleable.MonthView_weekLabelTextColor, resources.getColor(R.color.week_label_text_color));
        normalDayTextColor = typedArray.getColor(R.styleable.MonthView_dayTextColor, resources.getColor(R.color.day_label_text_color));
        todayTextColor = resources.getColor(R.color.today_text_color);
        mSelectedCircleColor = typedArray.getColor(R.styleable.MonthView_selectDayCircleBgColor, resources.getColor(R.color.day_select_circle_bg_color));

        normalDayTextSize = typedArray.getDimensionPixelSize(R.styleable.MonthView_dayTextSize, resources.getDimensionPixelSize(R.dimen.text_size_day));
        MONTH_LABEL_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.MonthView_monthTextSize, resources.getDimensionPixelSize(R.dimen.text_size_month));
        WEEK_LABEL_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.MonthView_weekLabelTextSize, resources.getDimensionPixelSize(R.dimen.text_size_week));
        MONTH_HEADER_HEIGHT = monthHeaderSizeCache = typedArray.getDimensionPixelOffset(R.styleable.MonthView_monthHeaderHeight, resources.getDimensionPixelOffset(R.dimen.header_month_height));
        dayCircleRadius = typedArray.getDimensionPixelSize(R.styleable.MonthView_dayCircleRadius, resources.getDimensionPixelOffset(R.dimen.selected_day_radius));

        dayRowHeight = typedArray.getDimensionPixelSize(R.styleable.MonthView_dayRowHeight, resources.getDimensionPixelOffset(R.dimen.row_height));
        mShowMonthTitle = typedArray.getBoolean(R.styleable.MonthView_showMonthTitle, true);
        mShowWeekLabel = typedArray.getBoolean(R.styleable.MonthView_showWeekLabel, true);
        mShowWeekDivider = typedArray.getBoolean(R.styleable.MonthView_showWeekDivider, false);

        spaceBetweenWeekAndDivider = resources.getDimensionPixelSize(R.dimen.week_label_between_divider_size);
        if (!mShowMonthTitle) {
            MONTH_HEADER_HEIGHT = 0;
        }
        if (!mShowWeekLabel) {
            WEEK_LABEL_HEIGHT = 0;
        } else {
            WEEK_LABEL_HEIGHT = WEEK_LABEL_TEXT_SIZE + spaceBetweenWeekAndDivider;
        }

        //        typedArray.recycle();
        mPadding = getPaddingLeft();
        drawRect = new Rect();
        initStyle();
        initPaint();
        setYearAndMonth(today.getYear(), today.getMonth());
    }

    private int calculateNumRows() {
        int offset = findDayOffset();
        int dividend = (offset + mNumCells) / mNumDays;
        int remainder = (offset + mNumCells) % mNumDays;
        return (dividend + (remainder > 0 ? 1 : 0));
    }

    /**
     * 设置当前时间
     *
     * @param today 当前时间
     */
    public void setToday(CalendarDay today) {
        this.today = today;
        invalidate();
    }

    private void initStyle() {
        todayStyle = new DayDecor.Style();
        todayStyle.setBold(true);
        todayStyle.setTextColor(todayTextColor);

        selectionStyle = new DayDecor.Style();
        selectionStyle.setPureColorBgShape(DayDecor.Style.CIRCLE);
        selectionStyle.setPureColorBg(mSelectedCircleColor);

        normalStyle = new DayDecor.Style();

        otherMonthStyle = new DayDecor.Style();
        otherMonthStyle.setTextColor(mOtherMonthTextColor);
    }

    private void drawWeekLabels(Canvas canvas) {
        int y = MONTH_HEADER_HEIGHT + WEEK_LABEL_TEXT_SIZE + weekLabelOffset;
        float dayWidthHalf = halfDayWidth;

        for (int i = 0; i < mNumDays; i++) {
            int calendarDay = (i + mWeekStart) % mNumDays;
            if (calendarDay == 0)
                calendarDay = mNumDays;
            float x = (2 * i + 1) * dayWidthHalf + mPadding;

            final Locale locale = getResources().getConfiguration().locale;
            SimpleDateFormat mDayOfWeekFormatter = new SimpleDateFormat(DAY_OF_WEEK_FORMAT, locale);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, calendarDay);
            canvas.drawText(mDayOfWeekFormatter.format(cal.getTime()), x, y, mWeekLabelPaint);
        }

        if (mShowWeekDivider) {
            //draw divider under week label
            int yLine = MONTH_HEADER_HEIGHT + WEEK_LABEL_HEIGHT + weekLabelOffset;
            canvas.drawLine(mPadding, yLine - 1, mWidth - mPadding, yLine, mWeekLabelPaint);
        }
    }

    private void drawMonthTitle(Canvas canvas) {
        //        Log.e("MonthView", "drawMonthTitle");
        int[] pos = getMonthDrawPoint();
        //        StringBuilder stringBuilder = new StringBuilder(getMonthAndYearString().toLowerCase());
        //        stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
        canvas.drawText(getMonthTitleString(), pos[0], pos[1], mMonthTitlePaint);
    }

    /**
     * draw the day of month
     */
    protected void drawMonthDays(Canvas canvas) {
        int dayTop = SPACE_BETWEEN_WEEK_AND_DAY + MONTH_HEADER_HEIGHT + WEEK_LABEL_HEIGHT;
        float halfDay = halfDayWidth;
        int firstDayOffset = findDayOffset();
        boolean weekMode = mWeekMode;

        // in a line, the offset with left of current day
        int offsetInLine = 0;
        // the offset with top left of current day
        int startOffset = weekMode ? getWeekIndex() * mNumDays : 0;
        // times we loop
        int cells = weekMode ? mNumDays : mNumRows * mNumDays;
        // loop to draw
        for (int i = startOffset; i < startOffset + cells; i++) {
            int day = i - firstDayOffset + 1;
            CalendarMonth currentMonth = getCurrentMonth();
            // if true, current drawing day is in other month
            boolean otherMonth = false;
            if (day < 1) {
                if (!weekMode && !mShowOtherMonth) {
                    offsetInLine++;
                    continue;
                }

                otherMonth = true;
                currentMonth = currentMonth.previous();
                int preMDays = CalendarUtils.getDaysInMonth(currentMonth);
                day = preMDays + day;
            } else if (day > mNumCells) {
                if (!weekMode && !mShowOtherMonth) {
                    offsetInLine++;
                    continue;
                }

                otherMonth = true;
                currentMonth = currentMonth.next();
                day = day - mNumCells;
            }
            // x position
            CalendarDay currentDay = new CalendarDay(currentMonth, day);
            float dayLeft = offsetInLine * halfDay * 2 + mPadding;
            float x = halfDay + dayLeft;
            // if true, current drawing day is selected
            boolean selected = false;
            if (selectedDay != null && selectedDay.equals(currentDay)) { //selected
                selected = true;
            }

            DayDecor.Style decoration = null;
            if (mDecors != null) {
                decoration = mDecors.getDecorStyle(currentDay);
            }
            // default color and size
            mDayNumPaint.setColor(decorTextColor);
            mDayNumPaint.setTextSize(normalDayTextSize);

            // ==================================
            // cover order for drawing:
            // 1. text: other month > selection > decorator > today > normal
            // 2. bg: selection + decorator, selection in foreground
            // ==================================
            DayDecor.Style style;
            if (!weekMode && otherMonth) { // other month, if in week mode, other month is unnecessary
                style = otherMonthStyle;
            } else if (selected) { // select
                style = selectionStyle;
            } else if (decoration != null) { // exist decor
                style = decoration;
            } else if (today.equals(currentDay)) { // today
                style = todayStyle;
            } else { // normal
                style = normalStyle;
                style.setTextColor(normalDayTextColor);
            }
            style.assignStyleToPaint(mDayNumPaint);
            // get text height
            String dayStr = String.format(Locale.getDefault(), "%d", day);
            mDayNumPaint.getTextBounds(dayStr, 0, dayStr.length(), drawRect);
            int textHeight = drawRect.height();
            float y = (dayRowHeight + mDayNumPaint.getTextSize()) / 2 + dayTop;

            // draw background
            if (decoration != null) {
                drawDayBg(canvas, decoration, x, y, textHeight, dayTop, dayLeft);
            }
            if (selected) {
                drawDayBg(canvas, selectionStyle, x, y, textHeight, dayTop, dayLeft);
            }

            // draw text
            canvas.drawText(dayStr, x, y, mDayNumPaint);

            // goto next day
            offsetInLine++;
            if (offsetInLine == mNumDays) {
                offsetInLine = 0;
                dayTop += dayRowHeight;
            }
        }
    }

    private void drawDayBg(Canvas canvas, DayDecor.Style style, float x, float y, int textHeight,
                           int dayTop, float dayLeft) {
        float halfDay = halfDayWidth;
        if (style.isCircleBg()) {
            mDayBgPaint.setColor(style.getPureColorBg());
            canvas.drawCircle(x, y - textHeight / 2, dayCircleRadius, mDayBgPaint);
        } else if (style.isRectBg()) {
            mDayBgPaint.setColor(style.getPureColorBg());
            canvas.drawRect(dayLeft, dayTop, dayLeft + 2 * halfDay, dayTop + dayRowHeight, mDayBgPaint);
        } else if (style.isDrawableBg()) {
            Drawable drawable = style.getDrawableBg();
            int dHeight = drawable.getIntrinsicHeight();
            int dWidth = drawable.getIntrinsicWidth();

            float left, right, top, bottom;
            if (dWidth <= 0) { // fill
                left = dayLeft;
                right = dayLeft + 2 * halfDay;
            } else { // remain original size
                left = x - dWidth / 2;
                right = x + dWidth / 2;
            }
            if (dHeight <= 0) {
                top = dayTop;
                bottom = dayTop + dayRowHeight;
            } else {
                top = y - textHeight / 2 - dHeight / 2;
                bottom = y - textHeight / 2 + dHeight / 2;
            }
            drawable.setBounds((int) left, (int) top, (int) right, (int) bottom);
            drawable.draw(canvas);
        }
    }

    /**
     * The first day of current month offset x cells.
     *
     * @return the x
     */
    protected int findDayOffset() {
        return (mDayOfWeekStart < mWeekStart ? (mDayOfWeekStart + mNumDays) : mDayOfWeekStart) - mWeekStart;
    }

    public String getMonthTitleString() {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long millis = calendar.getTimeInMillis();
        return DateUtils.formatDateRange(getContext(), millis, millis, flags);
    }

    public int getMonthTitleWidth() {
        return (int) mMonthTitlePaint.measureText(getMonthTitleString());
    }

    private void onDayClick(CalendarDay calendarDay) {
        selectActual(calendarDay, true);
    }

    public void setSelectionStyle(DayDecor.Style selectionStyle) {
        this.selectionStyle.combine(selectionStyle);
        invalidate();
    }

    /**
     * Select a specified calendar day in this month. Selection day should always be visible, no
     * matter month mode or week mode.
     *
     * @param selection a day; set null to clear selection.
     * @return success selected
     */
    public boolean setSelection(@Nullable CalendarDay selection) {
        if (selection != null && selection.compareTo(leftEdge) < 0)
            return false;
        if (selection != null && selection.compareTo(rightEdge) > 0)
            return false;

        if (selection != null) {
            if (mWeekMode) {
                // selection can not out range of current week
                if (!isDayInWeek(selection)) {
                    return false;
                }
            } else {
                // selection can not out range of current month
                int com = getDayType(selection);
                if (com == -2 || com == 2) {
                    return false;
                }
            }
        }

        return selectActual(selection, false);
    }

    void setSelectionAtom(@Nullable CalendarDay selection) {
        selectedDay = selection;
        invalidate();
    }

    private boolean selectActual(CalendarDay selection, boolean byUser) {
        if (selectedDay == selection) // not changed
            return false;
        if (selection != null && selection.equals(selectedDay)) //same day
            return false;

        CalendarDay old = selectedDay;
        selectedDay = selection;
        invalidate();
        if (mOnSelectionChangeListener != null) {
            mOnSelectionChangeListener.onSelectionChanged(this, selection, old, byUser);
        }
        return true;
    }

    public CalendarDay getSelection() {
        return selectedDay;
    }

    protected void leftEdgeDay(CalendarDay lEdge) {
        leftEdge = lEdge;
    }

    protected void rightEdgeDay(CalendarDay rEdge) {
        rightEdge = rEdge;
    }

    private CalendarDay getDayFromLocation(float x, float y) {
        int padding = mPadding;
        if ((x < padding) || (x > mWidth - padding)) {
            return null;
        }

        float yDayOffset = y - MONTH_HEADER_HEIGHT - WEEK_LABEL_HEIGHT - SPACE_BETWEEN_WEEK_AND_DAY;
        if (yDayOffset < 0)
            return null;

        int yDay = (int) yDayOffset / dayRowHeight;
        int day = 1 + ((int) ((x - padding) / (2 * halfDayWidth)) - findDayOffset()) + yDay * mNumDays;

        if (mWeekMode) {
            day += getWeekIndex() * mNumDays;
        }

        if (day < 1) {
            if (mShowOtherMonth) {
                CalendarMonth preM = getCurrentMonth().previous();
                int preD = CalendarUtils.getDaysInMonth(preM) + day;
                return new CalendarDay(preM, preD);
            } else {
                return null;
            }
        } else if (day > mNumCells) {
            if (mShowOtherMonth) {
                CalendarMonth nextM = getCurrentMonth().next();
                int nextD = day - mNumCells;
                return new CalendarDay(nextM, nextD);
            } else {
                return null;
            }
        } else
            return new CalendarDay(getCurrentMonth(), day);
    }

    private boolean isClickMonth(int x, int y) {
        int[] pos = getMonthDrawPoint();
        int centerX = pos[0];
        int bottom = pos[1];
        int extra = 10;
        int width = getMonthTitleWidth();
        Rect monthTitleRect = new Rect(centerX - width / 2 - extra, bottom - MONTH_LABEL_TEXT_SIZE - extra,
                centerX + width / 2 + extra, bottom + extra);

        return monthTitleRect.contains(x, y);
    }

    private int[] getMonthDrawPoint() {
        int x = mWidth / 2;
        int y = MONTH_HEADER_HEIGHT / 2 + (MONTH_LABEL_TEXT_SIZE / 3) + monthLabelOffset;
        return new int[]{x, y};
    }

    protected void initPaint() {
        mMonthTitlePaint = new Paint();
        mMonthTitlePaint.setFakeBoldText(true);
        mMonthTitlePaint.setAntiAlias(true);
        mMonthTitlePaint.setTextSize(MONTH_LABEL_TEXT_SIZE);
        mMonthTitlePaint.setTypeface(Typeface.create(mMonthTitleTypeface, Typeface.BOLD));
        mMonthTitlePaint.setColor(mMonthTextColor);
        mMonthTitlePaint.setTextAlign(Align.CENTER);
        mMonthTitlePaint.setStyle(Style.FILL);

        mDayBgPaint = new Paint();
        mDayBgPaint.setAntiAlias(true);
        mDayBgPaint.setColor(mSelectedCircleColor);
        mDayBgPaint.setStyle(Style.FILL);

        mWeekLabelPaint = new Paint();
        mWeekLabelPaint.setAntiAlias(true);
        mWeekLabelPaint.setTextSize(WEEK_LABEL_TEXT_SIZE);
        mWeekLabelPaint.setColor(mWeekColor);
        mWeekLabelPaint.setTypeface(Typeface.create(mDayOfWeekTypeface, Typeface.NORMAL));
        mWeekLabelPaint.setStyle(Style.FILL);
        mWeekLabelPaint.setTextAlign(Align.CENTER);
        mWeekLabelPaint.setFakeBoldText(true);

        mDayNumPaint = new Paint();
        mDayNumPaint.setAntiAlias(true);
        mDayNumPaint.setTextSize(normalDayTextSize);
        mDayNumPaint.setStyle(Style.FILL);
        mDayNumPaint.setTextAlign(Align.CENTER);
        mDayNumPaint.setColor(normalDayTextColor);
        mDayNumPaint.setFakeBoldText(false);
    }

    protected void onDraw(Canvas canvas) {
        //        Log.d("MonthView", "onDraw");

        if (mShowMonthTitle) {
            drawMonthTitle(canvas);
        }
        if (mShowWeekLabel) {
            drawWeekLabels(canvas);
        }
        drawMonthDays(canvas);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //
        //        int height = 0;
        //        switch (heightMode) {
        //            case MeasureSpec.AT_MOST:
        //                height = getShouldHeight();
        //                break;
        //            case MeasureSpec.EXACTLY:
        //            case MeasureSpec.UNSPECIFIED:
        //                height = heightSize;
        //                break;
        //        }

        //        Log.d("MonthView", "onMeasure->" + this.getId());
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), getShouldHeight());
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        halfDayWidth = (float) (mWidth - 2 * mPadding) / (2 * mNumDays);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycle();
    }

    private void recycle() {
        if (mTypeArray != null) {
            mTypeArray.recycle();
            mTypeArray = null;
        }
    }

    protected void setOtherMonthTextColor(@ColorInt int color) {
        if (color == mOtherMonthTextColor)
            return;

        mOtherMonthTextColor = color;
        otherMonthStyle.setTextColor(color);
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            // still consume touch event
            return true;
        }
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(downX - x) < 10
                        && Math.abs(downY - y) < 10
                        && event.getEventTime() - event.getDownTime() < 500) {
                    CalendarDay calendarDay = getDayFromLocation(x, y);
                    if (calendarDay != null) {
                        // if this location is out of range.
                        if ((leftEdge != null && calendarDay.compareTo(leftEdge) < 0)
                                || (rightEdge != null && calendarDay.compareTo(rightEdge) > 0))
                            break;
                        // else
                        onDayClick(calendarDay);
                    } else if (isClickMonth((int) x, (int) y)) { // clicked month title
                        // month title clicked
                        if (mOnMonthClicker != null) {
                            mOnMonthClicker.onMonthClick(this, getCurrentMonth());
                        }
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 设置当前显示的年和月
     *
     * @param calendarMonth calendarMonth
     */
    public void setYearAndMonth(CalendarMonth calendarMonth) {
        setYearAndMonth(calendarMonth.getYear(), calendarMonth.getMonth());
    }

    /**
     * 设置当前显示的年和月
     *
     * @param year  年
     * @param month 月
     */
    public void setYearAndMonth(int year, int month) {
        if (year == mYear && month == mMonth + 1)
            return;

        mYear = year;
        mMonth = month - 1;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        mDayOfWeekStart = calendar.get(Calendar.DAY_OF_WEEK);

        mWeekStart = calendar.getFirstDayOfWeek();

        mNumCells = CalendarUtils.getDaysInMonth(mMonth, mYear);

        mNumRows = calculateNumRows();

        if (ViewCompat.isLaidOut(this)) {
            // we are not sure height will remain unchanged.
            requestLayout();
            invalidate();
        }
    }

    public void setDecors(DayDecor mDecors) {
        this.mDecors = mDecors;
        invalidate();
    }

    public DayDecor getDecors() {
        return mDecors;
    }

    public void showMonthTitle(boolean show) {
        this.mShowMonthTitle = show;
        if (!mShowMonthTitle) {
            MONTH_HEADER_HEIGHT = 0;
        } else {
            MONTH_HEADER_HEIGHT = monthHeaderSizeCache;
        }
    }

    public void showWeekLabel(boolean show) {
        this.mShowWeekLabel = show;
        if (!mShowWeekLabel) {
            WEEK_LABEL_HEIGHT = 0;
        } else {
            WEEK_LABEL_HEIGHT = WEEK_LABEL_TEXT_SIZE + spaceBetweenWeekAndDivider;
        }
    }

    public void setNormalDayTextColor(@ColorInt int color) {
        normalDayTextColor = color;
    }

    public void setNormalDayTextSize(int px) {
        normalDayTextSize = px;
    }

    protected void setShowOtherMonth(boolean show) {
        if (mShowOtherMonth == show)
            return;

        mShowOtherMonth = show;
        invalidate();
    }

    public void setDayCircleRadius(int px) {
        dayCircleRadius = px;
    }

    public void setDayRowHeight(int px) {
        dayRowHeight = px;
    }

    protected void setWeekLabelOffset(int weekLabelOffset) {
        this.weekLabelOffset = weekLabelOffset;
        invalidate();
    }

    protected void setMonthLabelOffset(int monthLabelOffset) {
        this.monthLabelOffset = monthLabelOffset;
        invalidate();
    }

    public void showWeekMode() {
        if (mWeekMode)
            return;

        this.mWeekMode = true;
        invalidate();
        requestLayout();
    }

    public void setWeekIndex(int weekIndex) {
        if (mWeekIndex == weekIndex)
            return;

        this.mWeekIndex = weekIndex;
        if (mWeekMode) {
            invalidate();
        }
    }

    public int getWeekIndex() {
        return mWeekIndex;
    }

    public boolean isWeekMode() {
        return mWeekMode;
    }

    boolean isDayInWeek(CalendarDay day) {
        if (!mWeekMode)
            throw new IllegalStateException("do not call this when not in week mode");

        int dayOffset = findDayOffset();
        int start = mWeekIndex * mNumDays - dayOffset;
        CalendarDay startDay = CalendarUtils.offsetDay(new CalendarDay(getCurrentMonth(), 1), start);
        if (day.compareTo(startDay) < 0)
            return false;

        CalendarDay endDay = CalendarUtils.offsetDay(startDay, mNumDays);
        if (day.compareTo(endDay) > 0)
            return false;

        return true;
    }

    public void showMonthMode() {
        if (!mWeekMode)
            return;

        this.mWeekMode = false;
        invalidate();
        requestLayout();
    }

    /**
     * Line index of selection showing.
     *
     * @return return -1 for no selection or selection is visible in current month.
     */
    public int getSelectionLineIndex() {
        if (selectedDay != null) {
            int type = getSelectionType();
            switch (type) {
                case 0:
                    return (findDayOffset() + selectedDay.getDay() - 1) / mNumDays;
                case 1:
                    return mNumRows - 1;
                case 2:
                    return -1;
                case -1:
                    return 0;
                case -2:
                    return -1;
            }
        }

        return -1;
    }

    /**
     * Selection day type.
     *
     * @return -3 - no selection, others see {@link #getDayType(CalendarDay)}
     */
    int getSelectionType() {
        if (selectedDay == null)
            return -3;

        return getDayType(selectedDay);
    }

    /**
     * A type of day relative to this month.
     *
     * @return <p>0 - if day is in current month</p>
     * <p>-1 - if day is in previous month and visible now</p>
     * <p>-2 - if day is in previous month but not visible</p>
     * <p>1 - if day is in next month and visible now</p>
     * <p>2 - if day is in next month but not visible</p>
     */
    int getDayType(@NonNull CalendarDay day) {
        int com = day.getCalendarMonth().compareTo(getCurrentMonth());
        if (com == 0) {
            // current month
            return 0;
        }

        int dayOffset = findDayOffset();
        if (com < 0) {
            CalendarDay first = new CalendarDay(getCurrentMonth(), 1);
            CalendarDay otherMonthStartDay = CalendarUtils.offsetDay(first, -dayOffset);
            if (mShowOtherMonth && day.compareTo(otherMonthStartDay) >= 0) {
                // select day is in previous month and visible
                return -1;
            } else {
                return -2;
            }
        } else {
            CalendarDay last = new CalendarDay(getCurrentMonth(), mNumCells);
            CalendarDay otherMonthEndDay = CalendarUtils.offsetDay(last, mNumRows * mNumDays - dayOffset - mNumCells);
            if (mShowOtherMonth && day.compareTo(otherMonthEndDay) <= 0) {
                // select day is in next month and visible
                return 1;
            } else {
                return 2;
            }
        }
    }

    /**
     * return the number of week of this month.
     *
     * @return week number rows.
     */
    int getWeekRows() {
        return mNumRows;
    }

    /**
     * Height of MonthView should be.
     *
     * @return should height
     */
    public int getShouldHeight() {
        if(mWeekMode) {
            return getHeightWithRows(1);
        } else {
            return getHeightWithRows(mNumRows);
        }
    }

    /**
     * the max height MonthView could be.
     *
     * @return max height
     */
    public int getMaxHeight() {
        return getHeightWithRows(DEFAULT_NUM_ROWS);
    }

    public int getHeightWithRows(int rows) {
        return MONTH_HEADER_HEIGHT + WEEK_LABEL_HEIGHT + SPACE_BETWEEN_WEEK_AND_DAY + dayRowHeight * rows;
    }

    /**
     * Height of one day row.
     * @return height
     */
    public int getDayRowHeight() {
        return dayRowHeight;
    }

    // get a copy with same attributes defined in layout.
    protected MonthView staticCopy() {
        if (isCopy)
            // this is a copy, should not make a copy again.
            return null;
        return new MonthView(getContext(), mTypeArray, null);
    }

    public CalendarMonth getCurrentMonth() {
        return new CalendarMonth(mYear, mMonth + 1);
    }

    public void setOnSelectionChangeListener(OnSelectionChangeListener listener) {
        mOnSelectionChangeListener = listener;
    }

    public OnMonthTitleClickListener getOnMonthTitleClickListener() {
        return mOnMonthClicker;
    }

    public OnSelectionChangeListener getOnSelectionChangeListener() {
        return mOnSelectionChangeListener;
    }

    public void setOnMonthTitleClickListener(OnMonthTitleClickListener onMonthTitleClickListener) {
        this.mOnMonthClicker = onMonthTitleClickListener;
    }

    public interface OnSelectionChangeListener {
        /**
         * Selection has changed on month view
         *
         * @param monthView monthView
         * @param now       now selection
         * @param old       old selection
         * @param byUser    true - selection changed by user click, false - selection changed by {@link #setSelection(CalendarDay)}
         */
        void onSelectionChanged(MonthView monthView, @Nullable CalendarDay now, @Nullable CalendarDay old, boolean byUser);
    }

    public interface OnMonthTitleClickListener {
        void onMonthClick(MonthView monthView, CalendarMonth calendarMonth);
    }
    public void reuse() {
        mNumRows = DEFAULT_NUM_ROWS;
        requestLayout();
    }

    public void setShowMonthTitle(boolean showMonthTitle) {
        mShowMonthTitle = showMonthTitle;
    }

    public void setShowWeekLabel(boolean showWeekLabel) {
        mShowWeekLabel = showWeekLabel;
    }

    public void setShowWeekDivider(boolean showWeekDivider) {
        mShowWeekDivider = showWeekDivider;
    }
}