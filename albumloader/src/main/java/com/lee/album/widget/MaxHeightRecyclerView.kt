package com.lee.album.widget

import androidx.recyclerview.widget.RecyclerView
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.lee.album.R
import android.view.View.MeasureSpec

class MaxHeightRecyclerView : RecyclerView {
    private var mMaxHeight = 0

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(context, attrs)
    }

    private fun initialize(context: Context, attrs: AttributeSet?) {
        @SuppressLint("CustomViewStyleable") val arr =
            context.obtainStyledAttributes(attrs, R.styleable.MMaxHeightRecyclerView)
        mMaxHeight = arr.getLayoutDimension(
            R.styleable.MMaxHeightRecyclerView_maxHeightRcvHeight,
            mMaxHeight
        )
        arr.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        if (mMaxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}