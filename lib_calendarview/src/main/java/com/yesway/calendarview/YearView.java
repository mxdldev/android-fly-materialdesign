package com.yesway.calendarview;

import java.security.InvalidParameterException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class YearView extends ViewGroup {
    public static final String VIEW_PARAMS_YEAR_CURRENT = "current";
    public static final String VIEW_PARAMS_WEEK_START = "week_start";
    protected int DAY_LABEL_CIRCLE_RADIUS;
    protected int MONTH_HEADER_HEIGHT;
    protected int YEAR_HEADER_TEXT_HEIGHT;

    protected int YEAR_HEADER_TEXT_SIZE;
    protected int YEAR_HEADER_LUNAR_TEXT_SIZE;
    protected int MONTH_LABEL_TEXT_SIZE;
    protected int DAY_LABEL_TEXT_SIZE;

    protected int padding = 0;
    protected int lineSpacingBetweenYearAndMonth = 0;
    protected int dayRowHeight = 0;

    protected Paint yearHeaderTextPaint;
    protected Paint dividerPaint;
    protected Paint yearHeaderLunarTextPaint;
    protected Paint yearHeaderDashPaint;
    protected Paint monthLabelTextPaint;

    protected int yearHeaderTextColor;
    protected int dividerColor;
    protected int yearHeaderLunarTextColor;
    protected int yearHeaderDashColor;
    protected int monthTextColor;
    protected int dayLabelTextColor;

    protected int daysInWeek = 7;

    protected CalendarDay today;

    private int width;

    private boolean showYearLabel;
    private boolean showYearLunarLabel;
    private Calendar calendar;
    private OnMonthClickListener mOnMonthClickListener;
    private float downX;
    private float downY;
    private int[] monthRowHeight = new int[4];
    private DayDecor mDecors;

    protected int today1 = -1;
    protected int weekStart = 1;
    protected int numCells = 0;
    private int dayOfWeekStart = 0;
    protected int month;
    protected int year;
    protected Time currentTime;
    private Context mContext;

    public YearView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,context.obtainStyledAttributes(attrs,R.styleable.YearView));
    }

    public YearView(Context context, TypedArray typedArray) {
        super(context);
        initView(context, typedArray);
    }

    private void initView(Context context, TypedArray typedArray) {
        mContext = context;
        setWillNotDraw(false);
        calendar = Calendar.getInstance();
        today = new CalendarDay(Calendar.getInstance());
        year = today.getYear();
        currentTime = new Time(Time.getCurrentTimezone());
        currentTime.setToNow();
        Resources resources = context.getResources();

        showYearLabel = typedArray.getBoolean(R.styleable.YearView_showYearLabel, true);
        showYearLunarLabel = typedArray.getBoolean(R.styleable.YearView_showYearLunarLabel, false);
        dividerColor = typedArray.getColor(R.styleable.YearView_dividerColor, resources.getColor(R.color.divider_color));
        yearHeaderTextColor = typedArray.getColor(R.styleable.YearView_yearHeaderTextColor, resources.getColor(R.color.year_header_text_color));
        yearHeaderLunarTextColor = typedArray.getColor(R.styleable.YearView_yearHeaderLunarTextColor, resources.getColor(R.color.year_header_lunar_text_color));
        yearHeaderDashColor = typedArray.getColor(R.styleable.YearView_yearHeaderDashColor, resources.getColor(R.color.year_header_dash_color));
        monthTextColor = typedArray.getColor(R.styleable.YearView_monthLabelTextColor, resources.getColor(R.color.month_label_text_color));
        dayLabelTextColor = typedArray.getColor(R.styleable.YearView_dayLabelTextColor, resources.getColor(R.color.day_label_text_color));

        YEAR_HEADER_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.YearView_yearHeaderTextSize, resources.getDimensionPixelSize(R.dimen.year_header_text_size));
        YEAR_HEADER_LUNAR_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.YearView_yearHeaderLunarTextSize, resources.getDimensionPixelSize(R.dimen.year_header_lunar_text_size));
        DAY_LABEL_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.YearView_dayLabelTextSize, resources.getDimensionPixelSize(R.dimen.day_label_text_size));
        MONTH_LABEL_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.YearView_monthLabelTextSize, resources.getDimensionPixelSize(R.dimen.month_label_text_size));
        YEAR_HEADER_TEXT_HEIGHT = typedArray.getDimensionPixelSize(R.styleable.YearView_yearHeaderTextHeight, resources.getDimensionPixelOffset(R.dimen.year_header_text_height));
        MONTH_HEADER_HEIGHT = typedArray.getDimensionPixelOffset(R.styleable.YearView_monthLabelTextHeight, resources.getDimensionPixelSize(R.dimen.month_label_text_height));
        DAY_LABEL_CIRCLE_RADIUS = typedArray.getDimensionPixelSize(R.styleable.YearView_dayLabelCircleRadius, resources.getDimensionPixelSize(R.dimen.day_label_circle_radius));
        dayRowHeight = typedArray.getDimensionPixelSize(R.styleable.YearView_dayLabelRowHeight, resources.getDimensionPixelSize(R.dimen.day_row_height));

        padding = resources.getDimensionPixelSize(R.dimen.main_padding);
        lineSpacingBetweenYearAndMonth = resources.getDimensionPixelSize(R.dimen.padding_between_year_and_month);
        //typedArray.recycle();

        if(!showYearLabel) {
            showYearLunarLabel = false;
            YEAR_HEADER_TEXT_HEIGHT = 0;
            lineSpacingBetweenYearAndMonth = 0;
        }

        // no background will cause a performance problem in TransitRootView
        if(getBackground() == null) {
            setBackgroundColor(Color.WHITE);
        }
        initPaint();
        addMonth();
    }

    private void addMonth() {
        for (int i = 1; i <= 12; i++) {
            MonthView monthView = createMonthView();
            addView(monthView);
        }
    }

    protected MonthView createMonthView() {
        MonthView monthView = (MonthView) LayoutInflater.from(getContext()).inflate(R.layout.calendar_monthview_in_yearview, null);
        monthView.setNormalDayTextColor(dayLabelTextColor);
        monthView.setNormalDayTextSize(DAY_LABEL_TEXT_SIZE);
        monthView.setDayCircleRadius(DAY_LABEL_CIRCLE_RADIUS);
        monthView.setDayRowHeight(dayRowHeight);
        return monthView;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);

        //measure children
        int childWidthSpec = MeasureSpec.makeMeasureSpec(getSingleMonthWidth(), MeasureSpec.EXACTLY);

        int maxHeight = 0;
        for (int i = 1; i <= 12; i++) {
            MonthView monthView = (MonthView) getChildAt(i - 1);
            // update month
            monthView.setYearAndMonth(year, i);
            //measure MonthView
            int shouldHeight = monthView.getShouldHeight();
            int childHeightSpec = MeasureSpec.makeMeasureSpec(shouldHeight, MeasureSpec.EXACTLY);
            measureChild(monthView, childWidthSpec, childHeightSpec);

            maxHeight = shouldHeight > maxHeight ? shouldHeight : maxHeight;

            if (i % 3 == 0) {
                monthRowHeight[i / 3 - 1] = maxHeight + MONTH_HEADER_HEIGHT;
                maxHeight = 0;
            }
        }

        setMeasuredDimension(width, monthRowHeight[0] + monthRowHeight[1] + monthRowHeight[2] + monthRowHeight[3] + YEAR_HEADER_TEXT_HEIGHT
                + lineSpacingBetweenYearAndMonth);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int pad = getColumnPadding();
        int y = MONTH_HEADER_HEIGHT + YEAR_HEADER_TEXT_HEIGHT + lineSpacingBetweenYearAndMonth;
        int x = pad;

        for (int i = 1; i <= 12; i++) {
            MonthView monthView = (MonthView) getChildAt(i - 1);
            //layout month
            int measuredHeight = monthView.getMeasuredHeight();
            int measuredWidth = monthView.getMeasuredWidth();
            monthView.layout(x, y, x + measuredWidth, y + measuredHeight);

            x = x + measuredWidth + pad;
            if (i % 3 == 0) {
                y = y + monthRowHeight[i / 3 - 1];
                x = pad;
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        if(showYearLabel) {
            drawYearHeaderLabels(canvas);
        }
        drawMonthTitle(canvas);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
    }

    /**
     * 设置当前时间
     *
     * @param today 当前时间
     */
    public void setToday(CalendarDay today) {
        this.today = today;
        for(int i = 0; i < getChildCount(); i++) {
            MonthView monthView = (MonthView) getChildAt(i);
            // add decorates
            monthView.setToday(today);
        }
    }

    private void drawYearHeaderLabels(Canvas canvas) {
        if(YEAR_HEADER_TEXT_HEIGHT == 0)
            return;
        int y = (4 * YEAR_HEADER_TEXT_HEIGHT) / 5;
        boolean chinaArea = Locale.getDefault().equals(Locale.CHINA);
        canvas.drawText(chinaArea ? year + "年" : year + "", 2 * padding, y, yearHeaderTextPaint);

        if (showYearLunarLabel && chinaArea) { //显示农历
            String animal;
            String zhiGan;
            if (year == today.getYear()) { //显示年=今年
                Lunar lunar = new Lunar(today);
                animal = lunar.animalsYear();
                zhiGan = lunar.cyclical();
                String lunarMonth = lunar.getLunarMonthString();
                String lunarDay = lunar.getLunarDayString();

                //显示当前日的农历日
                yearHeaderDashPaint.setStrokeWidth((float) 2.0);
                canvas.drawLine(width - 5 * YEAR_HEADER_LUNAR_TEXT_SIZE - 2
                        * padding, (7 * y) / 8, width - 5
                        * YEAR_HEADER_LUNAR_TEXT_SIZE, (7 * y) / 8, yearHeaderDashPaint);
                canvas.drawText(lunarMonth + lunarDay, width - 2 * padding, y,
                        yearHeaderLunarTextPaint);
            } else {
                animal = Lunar.animalsYear(year);
                zhiGan = Lunar.cyclical(year);
            }

            //显示当前年的农历年
            yearHeaderDashPaint.setStrokeWidth((float) 4.0);
            canvas.drawLine(width - 5 * YEAR_HEADER_LUNAR_TEXT_SIZE - 2
                    * padding, (3 * y) / 8, width - 5
                    * YEAR_HEADER_LUNAR_TEXT_SIZE, (3 * y) / 8, yearHeaderDashPaint);
            canvas.drawText(zhiGan + animal + "年", width - 2
                    * padding, y / 2, yearHeaderLunarTextPaint);
        }

        //canvas.drawLine(2 * padding, YEAR_HEADER_TEXT_HEIGHT, width,
                //YEAR_HEADER_TEXT_HEIGHT, dividerPaint);
    }

    private void drawMonthTitle(Canvas canvas) {
        int pad = getColumnPadding();
        int monthWidth = getSingleMonthWidth();
        int x;
        int y = 0;

        for (int i = 1; i <= 12; i++) {
            String monthLabel = DateFormatSymbols.getInstance().getShortMonths()[i - 1];
            int xoffset = (int) (monthLabelTextPaint.measureText(monthLabel) / 2f) + padding * 2;
            int num = (i % 3 == 0 ? 3 : i % 3);
            x = pad * num + monthWidth * (num - 1) + xoffset;
            switch (i) {
                case 1:
                case 2:
                case 3:
                    y = (MONTH_HEADER_HEIGHT + MONTH_LABEL_TEXT_SIZE) / 2 + YEAR_HEADER_TEXT_HEIGHT
                            + lineSpacingBetweenYearAndMonth;
                    break;
                case 4:
                case 5:
                case 6:
                    y = (MONTH_HEADER_HEIGHT + MONTH_LABEL_TEXT_SIZE) / 2 + YEAR_HEADER_TEXT_HEIGHT
                            + lineSpacingBetweenYearAndMonth + monthRowHeight[0];
                    break;
                case 7:
                case 8:
                case 9:
                    y = (MONTH_HEADER_HEIGHT + MONTH_LABEL_TEXT_SIZE) / 2 + YEAR_HEADER_TEXT_HEIGHT
                            + lineSpacingBetweenYearAndMonth + monthRowHeight[0] + monthRowHeight[1];
                    break;
                case 10:
                case 11:
                case 12:
                    y = (MONTH_HEADER_HEIGHT + MONTH_LABEL_TEXT_SIZE) / 2 + YEAR_HEADER_TEXT_HEIGHT
                            + lineSpacingBetweenYearAndMonth + monthRowHeight[0] + monthRowHeight[1] + monthRowHeight[2];
                    break;
            }

            canvas.drawText(monthLabel, x, y, monthLabelTextPaint);
        }
    }

    void onMonthClick(CalendarMonth calendarMonth) {
        if (mOnMonthClickListener != null) {
            mOnMonthClickListener.onMonthClick(this, calendarMonth);
        }
    }

    protected void initPaint() {
        monthLabelTextPaint = new Paint();
        monthLabelTextPaint.setFakeBoldText(true);
        monthLabelTextPaint.setAntiAlias(true);
        monthLabelTextPaint.setTextSize(MONTH_LABEL_TEXT_SIZE);
        monthLabelTextPaint.setColor(monthTextColor);
        monthLabelTextPaint.setTextAlign(Align.CENTER);
        monthLabelTextPaint.setStyle(Style.FILL);

        yearHeaderTextPaint = new Paint();
        yearHeaderTextPaint.setAntiAlias(true);
        yearHeaderTextPaint.setTextAlign(Align.LEFT);
        yearHeaderTextPaint.setTextSize(YEAR_HEADER_TEXT_SIZE);
        yearHeaderTextPaint.setColor(yearHeaderTextColor);
        yearHeaderTextPaint.setStyle(Style.FILL);
        yearHeaderTextPaint.setFakeBoldText(true);

        yearHeaderLunarTextPaint = new Paint();
        yearHeaderLunarTextPaint.setAntiAlias(true);
        yearHeaderLunarTextPaint.setTextAlign(Align.RIGHT);
        yearHeaderLunarTextPaint.setTextSize(YEAR_HEADER_LUNAR_TEXT_SIZE);
        yearHeaderLunarTextPaint.setColor(yearHeaderLunarTextColor);
        yearHeaderLunarTextPaint.setStyle(Style.FILL);
        yearHeaderLunarTextPaint.setFakeBoldText(false);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setColor(dividerColor);
        dividerPaint.setStyle(Style.FILL);
        dividerPaint.setTextAlign(Align.CENTER);
        dividerPaint.setFakeBoldText(false);

        yearHeaderDashPaint = new Paint();
        yearHeaderDashPaint.setAntiAlias(true);
        yearHeaderDashPaint.setColor(yearHeaderDashColor);
    }

    protected int getSingleMonthWidth() {
        int pad = getColumnPadding();
        return (width - 4 * pad) / 3;
    }

    private int getColumnPadding() {
        return (width - 2 * padding) / (4 * daysInWeek) - padding;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled()) {
            // still consume touch event
            return true;
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(downX - event.getX()) < 10
                        && Math.abs(downY - event.getY()) < 10
                        && event.getEventTime() - event.getDownTime() < 500) {
                    CalendarMonth calendarDay = getMonthFromLocation((int)event.getX(), (int)event.getY());
                    if (calendarDay != null) {
                        onMonthClick(calendarDay);
                    }
                }
                break;
        }
        return true;
    }

    protected CalendarMonth getMonthFromLocation(int x, int y) {
        int i = 1;
        do {
            if(getMonthRect(i, true).contains(x, y)) {
                return new CalendarMonth(year, i);
            }
            i++;
        } while (i <= 12);
        return null;
    }

    /**
     * get rect of a month.
     * @param month month
     * @param includeLabel true - include month label, false - just days
     * @return rect
     */
    public Rect getMonthRect(int month, boolean includeLabel) {
        Rect rect = new Rect();
        View child = getChildAt(month - 1);
        rect.left = child.getLeft();
        rect.top = child.getTop() - (includeLabel ? MONTH_HEADER_HEIGHT : 0);
        rect.right = child.getRight();
        rect.bottom = child.getBottom();
        return rect;
    }

    /**
     * set decorators associate with a day, decorators will display a specified color circle
     * in the day.
     * @param decors DayDecor
     */
    public void setDecors(DayDecor decors) {
        this.mDecors = decors;
        for(int i = 0; i < getChildCount(); i++) {
            MonthView monthView = (MonthView) getChildAt(i);
            // add decorates
            monthView.setDecors(mDecors);
        }
    }

    public DayDecor getDecors() {
        return mDecors;
    }

    /**
     * 设置日历显示年
     *
     * @param year 显示年
     */
    public void setYear(int year) {
        this.year = year;
        requestLayout();
    }

    public int getYear() {
        return year;
    }

    /**
     * get year localized string
     * @return year string
     */
    public String getYearString() {
        boolean chinaArea = Locale.getDefault().equals(Locale.CHINA);
        return chinaArea ? year + "年" : year + "";
    }

    public void setOnMonthClickListener(OnMonthClickListener onMonthClickListener) {
        mOnMonthClickListener = onMonthClickListener;
    }

    public interface OnMonthClickListener {
        void onMonthClick(YearView yearView, CalendarMonth calendarMonth);
    }
    public void reuse() {
        requestLayout();
    }

    public void setYearParams(int year, int month) {
        this.month = month;
        this.year = year;

        today1 = -1;

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dayOfWeekStart = calendar.get(Calendar.DAY_OF_WEEK);

        weekStart = calendar.getFirstDayOfWeek();
        numCells = CalendarUtils.getDaysInMonth(month, year);

        for (int i = 0; i < numCells; i++) {
            final int day = i + 1;
            if (isToday(day, currentTime)) {
                today1 = day;
            }
        }
    }



    @SuppressWarnings("deprecation")
    public void setYearParams(HashMap<String, Integer> params) {
        if (!params.containsKey(VIEW_PARAMS_YEAR_CURRENT)) {
            throw new InvalidParameterException(
                    "You must specify current_year for this view");
        }
        setTag(params);

        year = params.get(VIEW_PARAMS_YEAR_CURRENT);

        today1 = -1;

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dayOfWeekStart = calendar.get(Calendar.DAY_OF_WEEK);

        if (params.containsKey(VIEW_PARAMS_WEEK_START)) {
            weekStart = params.get(VIEW_PARAMS_WEEK_START);
        } else {
            weekStart = calendar.getFirstDayOfWeek();
        }

        numCells = CalendarUtils.getDaysInMonth(month, year);

        for (int i = 0; i < numCells; i++) {
            final int day = i + 1;
            if (isToday(day, currentTime)) {
                today1 = day;
            }

        }

        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

    }
    private boolean isToday(int monthDay, Time time) {
        return (year == time.year) && (month == time.month)
                && (monthDay == time.monthDay);
    }

}