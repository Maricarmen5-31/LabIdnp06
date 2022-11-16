package com.example.labidnp06;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class PieChart extends View {

    public interface OnPieClickListener{
        void onPieClick(int index);
    }

    private Paint cirPaint;
    private Paint whiteLinePaint;
    private Point pieCenterPoint;
    private Paint textPaint;
    private RectF cirRect;
    private RectF cirSelectedRect;

    private int mViewWidth;
    private int mViewHeight;
    private int margin;
    private int pieRadius;

    private OnPieClickListener onPieClickListener;

    private ArrayList<PieDetail> pieHelperList;
    private int selectedIndex = NO_SELECTED_INDEX;

    private boolean showPercentLabel = true;
    public static final int NO_SELECTED_INDEX = -999;
    private final int[] DEFAULT_COLOR_LIST = {Color.parseColor("#33B5E5"),
            Color.parseColor("#AA66CC"),
            Color.parseColor("#99CC00"),
            Color.parseColor("#FFBB33"),
            Color.parseColor("#FF4444")};


    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = false;
            for(PieDetail pie : pieHelperList){
                pie.update();
                if(!pie.isAtRest()){
                    needNewFrame = true;
                }
            }
            if (needNewFrame) {
                postDelayed(this, 10);
            }
            invalidate();
        }
    };

    public PieChart(Context context){
        this(context,null);
    }
    public PieChart(Context context, AttributeSet attrs){
        super(context, attrs);

        pieHelperList = new ArrayList<PieDetail>();
        cirPaint = new Paint();
        cirPaint.setAntiAlias(true);
        cirPaint.setColor(Color.GRAY);
        whiteLinePaint = new Paint(cirPaint);
        whiteLinePaint.setColor(Color.WHITE);
        whiteLinePaint.setStrokeWidth(2f);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(Metodos.sp2px(getContext(), 13));
        textPaint.setStrokeWidth(5);
        textPaint.setTextAlign(Paint.Align.CENTER);
        pieCenterPoint = new Point();
        cirRect = new RectF();
        cirSelectedRect = new RectF();
    }

    public void showPercentLabel(boolean show){
        showPercentLabel = show;
        postInvalidate();
    }

    public void setOnPieClickListener(OnPieClickListener listener){
        onPieClickListener = listener;
    }

    public void setDate(ArrayList<PieDetail> helperList){
        initPies(helperList);
        pieHelperList.clear();
        removeSelectedPie();

        if(helperList != null && !helperList.isEmpty()){
            for(PieDetail pieHelper:helperList){
                pieHelperList.add(new PieDetail(pieHelper.getStartDegree(),pieHelper.getStartDegree(),pieHelper));
            }
        }else {
            pieHelperList.clear();
        }

        removeCallbacks(animator);
        post(animator);


    }


    private void initPies(ArrayList<PieDetail> helperList){
        float totalAngel = 270;
        for(PieDetail pie:helperList){
            pie.setDegree(totalAngel, totalAngel + pie.getSweep());
            totalAngel += pie.getSweep();
        }
    }

    public void selectedPie(int index){
        selectedIndex = index;
        if(onPieClickListener!=null) onPieClickListener.onPieClick(index);
        postInvalidate();
    }

    public void removeSelectedPie(){
        selectedIndex = NO_SELECTED_INDEX;
        if(onPieClickListener!=null) onPieClickListener.onPieClick(NO_SELECTED_INDEX);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(pieHelperList.isEmpty()){
            return ;
        }

        int index = 0;
        for(PieDetail pieHelper : pieHelperList){
            boolean selected = (selectedIndex == index);
            RectF rect = selected? cirSelectedRect: cirRect;
            if(pieHelper.isColorSetted()){
                cirPaint.setColor(pieHelper.getColor());
            }else {
                cirPaint.setColor(DEFAULT_COLOR_LIST[index%5]);
            }
            canvas.drawArc(rect, pieHelper.getStartDegree(), pieHelper.getSweep(), true, cirPaint);
            drawPercentText(canvas, pieHelper);

            drawLineBesideCir(canvas, pieHelper.getStartDegree(), selected);
            drawLineBesideCir(canvas, pieHelper.getEndDegree(), selected);
            index++;
        }

    }

    private void drawLineBesideCir(Canvas canvas, float angel, boolean selectedCir){
        int sth2 = selectedCir? mViewHeight/2 : pieRadius; // Sorry I'm really don't know how to name the variable..
        int sth = 1;                                       // And it's
        if(angel%360 > 180 && angel%360 < 360){
            sth = -1;
        }
        float lineToX = (float)(mViewHeight/2 + Math.cos(Math.toRadians(-angel)) * sth2);
        float lineToY = (float)(mViewHeight/2 + sth * Math.abs(Math.sin(Math.toRadians(-angel))) * sth2);
        canvas.drawLine(pieCenterPoint.x, pieCenterPoint.y, lineToX, lineToY, whiteLinePaint);
    }

    private void drawPercentText(Canvas canvas, PieDetail pieHelper){
        if(!showPercentLabel) return ;
        float angel = (pieHelper.getStartDegree() + pieHelper.getEndDegree()) /2;
        int sth = 1;
        if(angel%360 > 180 && angel%360 < 360){
            sth = -1;
        }
        float x = (float)(mViewHeight/2 + Math.cos(Math.toRadians(-angel)) * pieRadius/2);
        float y = (float)(mViewHeight/2 + sth * Math.abs(Math.sin(Math.toRadians(-angel))) * pieRadius/2);
        canvas.drawText(pieHelper.getPercentStr(), x, y, textPaint);
    }

    private void drawText(Canvas canvas, PieDetail pieHelper){
        if(pieHelper.getTitle() == null) return ;
        float angel = (pieHelper.getStartDegree() + pieHelper.getEndDegree()) /2;
        int sth = 1;
        if(angel%360 > 180 && angel%360 < 360){
            sth = -1;
        }
        float x = (float)(mViewHeight/2 + Math.cos(Math.toRadians(-angel)) * pieRadius/2);
        float y = (float)(mViewHeight/2 + sth * Math.abs(Math.sin(Math.toRadians(-angel))) * pieRadius/2);
        canvas.drawText(pieHelper.getTitle(), x, y, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN ||event.getAction() == MotionEvent.ACTION_MOVE){
            selectedIndex = findPointAt((int) event.getX(), (int) event.getY());
            if(onPieClickListener != null){
                onPieClickListener.onPieClick(selectedIndex);
            }
            postInvalidate();
        }

        return true;
    }


    private int findPointAt(int x, int y){
        double degree = Math.atan2(x-pieCenterPoint.x, y-pieCenterPoint.y)* 180 / Math.PI;
        degree = -(degree-180) + 270;
        int index = 0;
        for(PieDetail pieHelper:pieHelperList){
            if(degree>=pieHelper.getStartDegree() && degree<=pieHelper.getEndDegree()){
                return index;
            }
            index++;
        }
        return NO_SELECTED_INDEX;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        margin = mViewWidth/16;
        pieRadius = (mViewWidth)/2-margin;
        pieCenterPoint.set(pieRadius+margin, pieRadius+margin);
        cirRect.set(pieCenterPoint.x-pieRadius,
                pieCenterPoint.y-pieRadius,
                pieCenterPoint.x+pieRadius,
                pieCenterPoint.y+pieRadius);
        cirSelectedRect.set(2, //minor margin for bigger circle
                2,
                mViewWidth-2,
                mViewHeight-2);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private int measureWidth(int measureSpec){
        int preferred = 3;
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec){
        int preferred = mViewWidth;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred){
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int measurement;

        switch(View.MeasureSpec.getMode(measureSpec)){
            case View.MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case View.MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }
}
