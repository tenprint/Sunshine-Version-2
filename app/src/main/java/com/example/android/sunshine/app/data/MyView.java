package com.example.android.sunshine.app.data;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jlipata on 10/15/15.
 */
public class MyView extends View {
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
