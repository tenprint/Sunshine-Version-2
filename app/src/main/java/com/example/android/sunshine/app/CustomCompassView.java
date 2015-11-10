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
 *
 * Custom Compass View
 *
 * Needle will point to cardinal direction as provided via `direction` param in
 * initialize() method.
 *
 * initialize() must be called to display the needle correctly
 *
 * This will scale according to the layout_width and layout_height specified in
 * the layout XML
 *
 * TODO: Add shadows to conform to material design patterns
 *
 */

public class CustomCompassView extends View {

    int mCenterX;
    int mCenterY;
    float mDirection;


    public CustomCompassView(Context context){
        super(context);
    }

    public CustomCompassView(Context context, AttributeSet attrs){
        super(context, attrs);
    }


    public CustomCompassView(Context context, AttributeSet attrs, int defaultStyle){
        super(context, attrs, defaultStyle);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Get canvas size specified in layout
        int wCanvas = getWidth();
        int hCanvas = getHeight();

        // Initialize compass face size according to size provided by layout
        // Radius is calculated as a fraction of the smallest canvas side
        int compassFaceRadius;
        if (wCanvas<hCanvas)  compassFaceRadius = (int) (wCanvas/2.4); else compassFaceRadius = (int) (hCanvas/2.4);

        mCenterX = wCanvas / 2;
        mCenterY = hCanvas / 2;

        double needleLengthOffset = 0.80; // How long the needle should be compared to the compass face, as a percentage
        double needleLength = compassFaceRadius * needleLengthOffset;
        int boarder = (int) (compassFaceRadius * .10);

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
        paint.setStrokeWidth( (int) (compassFaceRadius*0.08) );
        canvas.drawLine(mCenterX, mCenterY, (float) (wCanvas / 2 + needleLength * Math.sin(Math.toRadians(mDirection))),
                (float) (hCanvas / 2 - needleLength * Math.cos(Math.toRadians(mDirection))), paint);

//        // For debugging -- draw boarder around View boundaries
//        canvas.drawLine(0,0, getWidth(), 0, paint);
//        canvas.drawLine(0,0, 0, getHeight(), paint);
//        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
//        canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), paint);


    }


    protected void initialize(float direction){
        mDirection = direction;
        Log.d("MyView", Float.toString(direction));
        invalidate();
        requestLayout();
    }
}
