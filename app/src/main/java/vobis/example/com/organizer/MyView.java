package vobis.example.com.organizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;


public class MyView extends View{

    float topMargin;
    float leftMargin;
    float fieldSize;
    int days;
    float canvasHeight;
    float canvasWidth;
    float vectorX;
    float vectorY;
    Paint paint;

    public MyView(Context context, int days){
        super(context);
        this.days = days;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        initializeCalendar(canvas);
        putDaysHeadline(canvas,leftMargin,topMargin,fieldSize);
        putCalendar(canvas,leftMargin,topMargin,fieldSize);
        invalidate();
    }

    private void initializeCalendar(Canvas canvas){
        paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);       //background
        canvasHeight = canvas.getHeight() - topMargin;
        canvasWidth = canvas.getWidth();
        fieldSize = canvasHeight/5;
        leftMargin = (canvasWidth - 7*fieldSize)/3;
        topMargin = 20;
    }

    private void putCalendar(Canvas canvas, float leftMargin, float topMargin,float fieldSize){
        for(int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++)
                drawCalendarField(canvas,leftMargin,topMargin,j,i,fieldSize);
    }

    private void putDaysHeadline(Canvas canvas,float leftMargin,float topMargin,float fieldSize){
        String[] dayNames = {"Mon"," Tue"," Wed"," Thu"," Fri"," Sat"," San"};
        for (int i = 0; i < 7; i++) putText(canvas,leftMargin + i * fieldSize,topMargin,dayNames[i]);
    }
    private void drawCalendarField(Canvas canvas,float leftMargin, float topMargin,int j,int i,float fieldSize){
        drawRectInside(canvas, leftMargin, topMargin, j, i, fieldSize);
        drawRectOutline(canvas, leftMargin, topMargin, j, i, fieldSize);
    }

    private void putText(Canvas canvas, float x, float y,  String text){
        paint.setColor(Color.BLACK);
        paint.setTextSize(13);
        canvas.drawText(text,x + 2,y - 2, paint);
    }

    private void drawRectInside(Canvas canvas, float leftMargin, float topMargin,int j, int i, float fieldSize){
        String text;
        String colorSequence;
        if (7 * i + j + 1 <= days) {
            colorSequence = "#CD5C5C";
            text = Integer.toString(7 * i + j + 1);
        }
        else{
            colorSequence = "#FFFFFF";
            text = Integer.toString((7 * i + j + 1)%days);
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(colorSequence));
        canvas.drawRect(leftMargin + j * fieldSize, topMargin + i * fieldSize, leftMargin + j * fieldSize + fieldSize, topMargin + i * fieldSize + fieldSize, paint);
        putText(canvas,leftMargin + j * fieldSize, topMargin + (i+1) * fieldSize,text);
    }

    private void drawRectOutline(Canvas canvas, float leftMargin, float topMargin, int j, int i, float fieldSize){
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#000000"));
        canvas.drawRect(leftMargin + j * fieldSize, topMargin + i * fieldSize, leftMargin + j * fieldSize + fieldSize,topMargin + i * fieldSize + fieldSize, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        vectorX = event.getX();
        vectorY = event.getY();
        if(event.getAction() == MotionEvent.ACTION_UP){

        }
        return super.onTouchEvent(event);
    }
}

