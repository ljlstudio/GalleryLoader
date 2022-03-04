package com.lee.album.widget;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2021/9/2718:30
 * Package: com.qtcx.picture.gallery
 * desc   :
 */
public class CommonGalleryGrideLayoutManager extends GridLayoutManager {




    public CommonGalleryGrideLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }




    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}