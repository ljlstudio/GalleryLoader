package com.lee.album.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.lee.album.activity.GalleryActivity;
import com.lee.album.inter.OnGalleryListener;
import com.lee.album.utils.Utils;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/1014:09
 * Package: com.lee.album.router
 * desc   :
 */
public class GalleryBuilder {
    private Context context;
    private GalleryEngine galleryEngine;
    private GalleryParam galleryParam;

    public GalleryBuilder(Context context, GalleryEngine galleryEngine) {
        this.galleryEngine = galleryEngine;
        this.context = context;
        galleryParam = GalleryParam.getInstance();
    }

    /**
     * 设置图片圆角
     *
     * @param corner
     */
    public GalleryBuilder widthListPictureCorner(int corner) {
        galleryParam.listPictureCorner = Utils.dip2px(context, corner);
        return this;
    }

    /**
     * 设置图片之间列距离
     *
     * @param space
     */
    public GalleryBuilder widthListPictureColumnSpace(int space) {
        galleryParam.listPictureColumnSpace = Utils.dip2px(context, space);
        return this;
    }

    /**
     * 设置图片之间行间距
     *
     * @param row
     * @return
     */
    public GalleryBuilder widthListPictureRowSpace(int row) {
        galleryParam.listPictureRowSpace = Utils.dip2px(context, row);
        return this;
    }


    /**
     * 设置图片距离屏幕两边距离
     *
     * @param margin
     */
    public GalleryBuilder widthListPictureMargin(int margin) {
        galleryParam.listPictureMargin = Utils.dip2px(context, margin);
        return this;
    }

    /**
     * 占位
     *
     * @param res
     * @return
     */
    public GalleryBuilder widthListPicturePlaceholder(int res) {
        galleryParam.listPicturePlaceholder = res;
        return this;
    }


    /**
     * 相册回到
     *
     * @param listener
     * @return
     */
    public GalleryBuilder widthOnGalleryListener(OnGalleryListener listener) {
        galleryParam.onGalleryListener = listener;
        return this;
    }

    /**
     * 是否分页
     */
    public GalleryBuilder withShouldLoadPaging(boolean noLoadPaging) {
        galleryParam.shouldLoadPaging = noLoadPaging;
        return this;
    }


    /**
     * 分页数据
     *
     * @param pageSize
     * @return
     */
    public GalleryBuilder widthPageSize(int pageSize) {
        galleryParam.pageSize = pageSize;
        return this;
    }

    /**
     * 打开相册预览
     */
    public void startGallery() {
        Activity activity = galleryEngine.getActivity();
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Fragment fragment = galleryEngine.getFragment();
        if (fragment != null) {
            fragment.startActivity(intent);
        } else {
            activity.startActivity(intent);
//            activity.overridePendingTransition(R.anim.fade, R.anim.hold);
        }
    }
}