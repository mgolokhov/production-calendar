package com.osh.prodcal.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.osh.android.utils.ViewUtils;
import com.osh.android.view.BaseDataView;
import com.osh.prodcal.domain.MonthEntity;
import com.osh.prodcal.domain.dto.Day;
import com.osh.prodcal.R;

import java.util.Calendar;

/**
 * Created by olegshatava on 24.10.17.
 */

public class MonthCalendarWidget extends BaseDataView<MonthCalendarWidget.MonthCalendarWidgetPresenter, MonthEntity> {

    private static final int DEFAULT_TEXT_SIZE = 12;
    private Calendar calendar = Calendar.getInstance();

    private int textSizeSP;
    private int weekTitleTextColor;
    private int workingDayTextColor;
    private int nonWorkingDayTextColor;
    private int workingNonFullDayTextColor;
    private int nonWorkingDayBgColor;
    private int workingNonFullDayBgColor;
    private boolean showMonthLabel;
    private boolean showWeekNumber;

    private Paint headerTextPaint;
    private Paint headerCellPaint;

    private Paint workDayTextPaint;
    private Paint workDayCellPaint;

    private Paint nonWorkDayTextPaint;
    private Paint nonWorkDayCellPaint;

    private Paint nonFullWorkDayTextPaint;
    private Paint nonFullWorkDayCellPaint;

    private float cellCircleBorder = 0;

    public MonthCalendarWidget(Context context) {
        this(context, null);
    }

    public MonthCalendarWidget(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthCalendarWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs);
        initPaint();
    }

    private void initPaint(){


        float textSize = ViewUtils.dpToPx(getContext(), textSizeSP);
        cellCircleBorder = ViewUtils.dpToPx(getContext(), 1);

        if(isInEditMode()) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            hasDayOffset = calendar.get(Calendar.WEEK_OF_MONTH) == 0;
        }

        setWillNotDraw(false);

        headerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        headerTextPaint.setColor(weekTitleTextColor);
        headerTextPaint.setTextAlign(Paint.Align.CENTER);
        headerTextPaint.setTextSize(textSize);

        headerCellPaint = null;


        workDayTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        workDayTextPaint.setColor(workingDayTextColor);
        workDayTextPaint.setTextSize(textSize);
        workDayTextPaint.setTextAlign(Paint.Align.CENTER);

        workDayCellPaint = null;

        nonWorkDayTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nonWorkDayTextPaint.setColor(nonWorkingDayTextColor);
        nonWorkDayTextPaint.setTextSize(textSize);
        nonWorkDayTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        nonWorkDayTextPaint.setTextAlign(Paint.Align.CENTER);

        nonWorkDayCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nonWorkDayCellPaint.setColor(nonWorkingDayBgColor);

        nonFullWorkDayTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nonFullWorkDayTextPaint.setColor(workingNonFullDayTextColor);
        nonFullWorkDayTextPaint.setTextSize(textSize);
        nonFullWorkDayTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        nonFullWorkDayTextPaint.setTextAlign(Paint.Align.CENTER);

        nonFullWorkDayCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nonFullWorkDayCellPaint.setColor(workingNonFullDayBgColor);

    }

    private void initAttributes(Context context, AttributeSet attrs) {

        if (attrs!=null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MonthCalendarWidget, 0, 0);
            textSizeSP = a.getInteger(R.styleable.MonthCalendarWidget_textSize, DEFAULT_TEXT_SIZE);
            weekTitleTextColor = a.getColor(R.styleable.MonthCalendarWidget_weekTitleTextColor, Color.DKGRAY);
            workingDayTextColor = a.getColor(R.styleable.MonthCalendarWidget_workingDayTextColor, Color.BLACK);
            nonWorkingDayTextColor = a.getColor(R.styleable.MonthCalendarWidget_nonWorkingDayTextColor, Color.WHITE);
            workingNonFullDayTextColor = a.getColor(R.styleable.MonthCalendarWidget_workingNonFullDayTextColor, Color.BLACK);
            nonWorkingDayBgColor = a.getColor(R.styleable.MonthCalendarWidget_nonWorkingDayBgColor, Color.RED);
            workingNonFullDayBgColor = a.getColor(R.styleable.MonthCalendarWidget_workingNonFullDayBgColor, Color.YELLOW);
            showMonthLabel = a.getBoolean(R.styleable.MonthCalendarWidget_showMonthLabel, false);
            showWeekNumber = a.getBoolean(R.styleable.MonthCalendarWidget_showWeekNumber, false);
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHeader(canvas);
        drawDays(canvas);
    }


    private boolean hasDayOffset = false;
    private int firstWeekIndex = 100000;
    private int lastWeekIndex = 0;

    private int getWeekOfMonth(Calendar calendar){
        return calendar.get(Calendar.WEEK_OF_MONTH) - (hasDayOffset?0:1);
    }

    private int dayOfWeekToIndex(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int index = 0;


        if (calendar.getFirstDayOfWeek() == Calendar.SUNDAY) {
            index = dayOfWeek - 1;
        } else {
            // monday
            index = dayOfWeek - 2;
            if (index < 0) index += 7;
        }

        return index;
    }

    private void drawDays(Canvas canvas) {
        for(int i=1; i<=calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
            drawDay(canvas, i);
        }
        if(showWeekNumber){
            for(int i=0; i<=lastWeekIndex - firstWeekIndex; i++){
                drawTextLabel(canvas, getCellRect(0, (showMonthLabel?1:0)+1+i), Integer.toString(i+firstWeekIndex), headerTextPaint);
            }
        }
    }

    private Calendar getCalendar(int dayNumber){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, this.calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, this.calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, dayNumber);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        return calendar;
    }


    private void drawDay(Canvas canvas, int i) {
        Calendar calendar = getCalendar(i);
        if(showWeekNumber) {
            int wof = calendar.get(Calendar.WEEK_OF_YEAR) - (hasDayOffset?0:1);
            if (wof< firstWeekIndex) firstWeekIndex = wof;
            if (wof > lastWeekIndex) lastWeekIndex = wof;
        }

        int row = getWeekOfMonth(calendar) + 1 + (showMonthLabel ? 1 : 0);
        int col = dayOfWeekToIndex(calendar) + (showWeekNumber ? 1 : 0);
        Day day = getDay(calendar.get(Calendar.DAY_OF_MONTH));
        if (day == null) {
            day = new Day(i, -1, true, !(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY));
        }

        drawCellBg(canvas, getCellRect(col, row), getBgPaintForDay(day));
        drawTextLabel(canvas, getCellRect(col, row), Integer.toString(i), getTextPaintForDay(day));
    }

    private Paint getBgPaintForDay(Day day) {
        if(day.isWorked() && !day.isFull())
            return nonFullWorkDayCellPaint;
        if(day.isWorked() && day.isFull())
            return workDayCellPaint;
        if(!day.isWorked())
            return nonWorkDayCellPaint;

        return null;
    }

    private Paint getTextPaintForDay(Day day) {
        if(day.isWorked() && !day.isFull())
            return nonFullWorkDayTextPaint;
        if(day.isWorked() && day.isFull())
            return workDayTextPaint;
        if(!day.isWorked())
            return nonWorkDayTextPaint;

        return workDayTextPaint;
    }


    private Day getDay(int i) {
        if(getData()!=null){
            if(getData().getDays().containsKey(i)){
                return getData().getDays().get(i);
            }
        }
        return null;
    }

    private float getCollCount(){
        return showWeekNumber?8:7;
    }

    private float getRowCount(){
        return showMonthLabel?8:7;
    }

    private float getCellWidth() {
        return getMeasuredWidth() / getCollCount();
    }

    private float getCellHeight() {
        return getMeasuredHeight() / getRowCount();
    }

    private RectF getCellRect(int col, int row) {
        return new RectF(col * getCellWidth(),
                row * getCellHeight(),
                (col + 1) * getCellWidth(),
                (row + 1) * getCellHeight());
    }

    private RectF getCellsRect(int col0, int row0, int col1, int row1) {
        return new RectF(col0 * getCellWidth(),
                row0 * getCellHeight(),
                (col1) * getCellWidth(),
                (row1) * getCellHeight());
    }

    private void drawHeader(Canvas canvas) {
        if(showMonthLabel)
            drawTextLabel(canvas, getCellsRect(0, 0, (int)getCollCount(), 1), getMonthLabelText(calendar.get(Calendar.MONTH)), headerTextPaint);

        for(int i=0; i<7; i++){
            drawCellBg(canvas, getCellRect((showWeekNumber?1:0)+i, showMonthLabel?1:0), headerCellPaint);
            drawTextLabel(canvas, getCellRect((showWeekNumber?1:0)+i, showMonthLabel?1:0), getShortDayLabelText(i), headerTextPaint);
        }
    }

    private void drawCellBg(Canvas canvas, RectF cellRect, Paint paint) {
        if (paint == null)
            return;
        canvas.drawCircle(cellRect.centerX(), cellRect.centerY(),
                Math.min(cellRect.width() / 2, cellRect.height() / 2) - cellCircleBorder,
                paint);
    }

    private void drawTextLabel(Canvas canvas, RectF cellRect, String text, Paint paint) {
        float xPos = cellRect.left +  (cellRect.width() / 2);
        float yPos = cellRect.top + (cellRect.height() / 2) - ((paint.descent() + paint.ascent()) / 2);
        canvas.drawText(text, xPos, yPos, paint);
    }

    private String getShortDayLabelText(int i) {
        return getResources().getStringArray(R.array.week_day_short_names)[i].toUpperCase();
    }

    private String getMonthLabelText(int i) {
        return getResources().getStringArray(R.array.month_names_full)[i];
    }

    @Override
    protected boolean onInitView() {
        return true;
    }

    @Override
    protected void onUpdateView(MonthEntity data) {
        calendar.setTimeInMillis(data.getCurrentMonth().getTimeInMillis());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        hasDayOffset = calendar.get(Calendar.WEEK_OF_MONTH) == 0;
        postInvalidate();
    }


    public interface MonthCalendarWidgetPresenter{
        void onDateSelected(Calendar date);
    }
}
