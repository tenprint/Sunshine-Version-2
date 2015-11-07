package com.example.android.sunshine.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by jlipata on 10/15/15.
 */
public class MyView extends View {

    int mCenterX;
    int mCenterY;
    String mDirection;


    public MyView(Context context){
        super(context);
    }

    public MyView(Context context, AttributeSet attrs){
        super(context, attrs);
    }


    public MyView(Context context, AttributeSet attrs, int defaultStyle){
        super(context, attrs, defaultStyle);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Hard coding these for the time being. TODO: Replace these with layout size from the framework
        int w = 200;
        int h = 200;
        int compassFaceRadius = 80;

//        // Initialize compass face size according to size provided by layout
//        int compassFaceRadius;
//        if (w<h) compassFaceRadius = w; else compassFaceRadius = h;

        mCenterX = w/2;
        mCenterY = h/2;

        double needleLengthOffset = 0.65; // What percentage the needle size should compared to the compass face
        double needleLength = compassFaceRadius * needleLengthOffset;
        int boarder = 15;

        // Initialize Paint object
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        // Draw boarder
        paint.setColor(getResources().getColor(R.color.sunshine_dark_blue));
        canvas.drawCircle(mCenterX, mCenterY, compassFaceRadius+boarder, paint);

        // Draw inner compass face
        paint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterX, mCenterY, compassFaceRadius, paint);

        // Draw Needle
        double xOffset = 0;
        double yOffset = 0;

        // Determine end-point of needle based on direction param
        if(mDirection!=null) {

            if (mDirection.contains("N")) {
                yOffset = -needleLength;
            }
            if (mDirection.contains("E")) {
                xOffset = needleLength;
            }
            if (mDirection.contains("S")) {
                yOffset = needleLength;
            }
            if (mDirection.contains("W")) {
                xOffset = -needleLength;
            }

            Log.d("MyView Offset", Double.toString(xOffset) + ", " + Double.toString(yOffset));

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(10);
            canvas.drawLine(mCenterX, mCenterY, mCenterX + (float) xOffset, mCenterY + (float) yOffset, paint);
        } else Toast.makeText(getContext(), "Compass: No direction data", Toast.LENGTH_SHORT);
    }

    protected void initialize(String direction){
        mDirection = direction;
        Log.d("MyView", direction);
        invalidate();
    }
}
