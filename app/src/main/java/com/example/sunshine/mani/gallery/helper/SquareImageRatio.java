package com.example.sunshine.mani.gallery.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by mani on 1/6/18.
 */

//This class is for setting dimensions of images into squares.

public class SquareImageRatio extends RelativeLayout {

    public SquareImageRatio(Context context) {
        super(context);
    }

    public SquareImageRatio(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageRatio(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareImageRatio(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int imageWidth, int imageHeight) {
        super.onMeasure(imageWidth, imageHeight);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth() + (getMeasuredWidth() / 2));

    }
}
