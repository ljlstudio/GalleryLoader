package com.lee.album.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

import com.lee.album.R;


/**
 * Author : 李嘉伦
 */
public class MaxHeightRecyclerView extends RecyclerView {
    private int mMaxHeight;

    public MaxHeightRecyclerView(Context context) {
        super(context);
    }

    public MaxHeightRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public MaxHeightRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        @SuppressLint("CustomViewStyleable") TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.MMaxHeightRecyclerView);
        mMaxHeight = arr.getLayoutDimension(R.styleable.MMaxHeightRecyclerView_maxHeightRcvHeight, mMaxHeight);
        arr.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
