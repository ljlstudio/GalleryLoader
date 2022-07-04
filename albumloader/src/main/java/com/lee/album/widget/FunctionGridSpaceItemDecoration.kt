package com.lee.album.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2021/2/2419:39
 * Package: com.shyz.picture.gallery
 * desc   :
 */
open class FunctionGridSpaceItemDecoration
/**
 * @param spanCount     列数
 * @param rowSpacing    行间距
 * @param columnSpacing 列间距
 */(//横条目数量
    private val mSpanCount: Int, //行间距
    private val mRowSpacing: Int, // 列间距
    private val mColumnSpacing: Int
) : ItemDecoration() {

    companion object {
        private const val TAG = "GridSpaceItemDecoration"
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val params = view.layoutParams as GridLayoutManager.LayoutParams
        val position = parent.getChildAdapterPosition(view) // 获取view 在adapter中的位置。
        val column = position % mSpanCount // view 所在的列
        outRect.left = column * mColumnSpacing / mSpanCount // column * (列间距 * (1f / 列数))
        outRect.right =
            mColumnSpacing - (column + 1) * mColumnSpacing / mSpanCount // 列间距 - (column + 1) * (列间距 * (1f /列数))
        outRect.top = mRowSpacing // item top
    }
}