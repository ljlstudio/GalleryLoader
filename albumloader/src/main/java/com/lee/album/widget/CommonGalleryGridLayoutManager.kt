package com.lee.album.widget

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView
import java.lang.IndexOutOfBoundsException

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2021/9/2718:30
 * Package: com.qtcx.picture.gallery
 * desc   :
 */
open class CommonGalleryGridLayoutManager(context: Context?, spanCount: Int) :
    GridLayoutManager(context, spanCount) {
    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }
}