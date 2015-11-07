package com.example.android.sunshine.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by jlipata on 10/15/15.
 */
public class MyView extends View {

    int mCenterX;
    int mCenterY;
    float mDirection;


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

        mCenterX = w / 2;
        mCenterY = h / 2;

        double needleLengthOffset = 0.70; // What percentage the needle size should compared to the compass face
        double needleLength = compassFaceRadius * needleLengthOffset;
        int boarder = 15;

        // Initialize Paint object
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        // Draw boarder
        paint.setColor(getResources().getColor(R.color.sunshine_dark_blue));
        canvas.drawCircle(mCenterX, mCenterY, compassFaceRadius + boarder, paint);

        // Draw inner compass face
        paint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterX, mCenterY, compassFaceRadius, paint);

        // Draw Needle
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        canvas.drawLine(mCenterX, mCenterY, (float) (w / 2 + needleLength * Math.sin(Math.toRadians(mDirection))),
                (float) (h / 2 - needleLength * Math.cos(Math.toRadians(mDirection))), paint);
    }


    protected void initialize(float direction){
        mDirection = direction;
        Log.d("MyView", Float.toString(direction));
        invalidate();
    }
}
