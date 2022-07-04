package com.lee.album.router

import com.lee.album.router.GalleryParam.Companion.instance
import com.lee.album.router.GalleryEngine
import com.lee.album.router.GalleryParam
import com.lee.album.router.GalleryBuilder
import com.lee.album.inter.OnGalleryListener
import android.app.Activity
import android.content.Context
import android.content.Intent
import com.lee.album.activity.GalleryActivity
import com.lee.album.utils.Utils

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/1014:09
 * Package: com.lee.album.router
 * desc   :
 */
class GalleryBuilder(private val context: Context, private val galleryEngine: GalleryEngine) {
    private val galleryParam: GalleryParam = instance

    /**
     * 设置图片圆角
     *
     * @param corner
     */
    fun widthListPictureCorner(corner: Int): GalleryBuilder {
        galleryParam.listPictureCorner = Utils.dip2px(context, corner.toFloat())
        return this
    }

    /**
     * 设置图片之间列距离
     *
     * @param space
     */
    fun widthListPictureColumnSpace(space: Int): GalleryBuilder {
        galleryParam.listPictureColumnSpace = Utils.dip2px(context, space.toFloat())
        return this
    }

    /**
     * 设置图片之间行间距
     *
     * @param row
     * @return
     */
    fun widthListPictureRowSpace(row: Int): GalleryBuilder {
        galleryParam.listPictureRowSpace = Utils.dip2px(context, row.toFloat())
        return this
    }

    /**
     * 设置图片距离屏幕两边距离
     *
     * @param margin
     */
    fun widthListPictureMargin(margin: Int): GalleryBuilder {
        galleryParam.listPictureMargin = Utils.dip2px(context, margin.toFloat())
        return this
    }

    /**
     * 占位
     *
     * @param res
     * @return
     */
    fun widthListPicturePlaceholder(res: Int): GalleryBuilder {
        galleryParam.listPicturePlaceholder = res
        return this
    }

    /**
     * 相册回到
     *
     * @param listener
     * @return
     */
    fun widthOnGalleryListener(listener: OnGalleryListener?): GalleryBuilder {
        galleryParam.onGalleryListener = listener
        return this
    }

    /**
     * 是否分页
     */
    fun withShouldLoadPaging(noLoadPaging: Boolean): GalleryBuilder {
        galleryParam.shouldLoadPaging = noLoadPaging
        return this
    }

    /**
     * 分页数据
     *
     * @param pageSize
     * @return
     */
    fun widthPageSize(pageSize: Int): GalleryBuilder {
        galleryParam.pageSize = pageSize
        return this
    }

    /**
     * 是否点击图片关闭bottomSheet
     *
     * @param shouldClickCloseBottomSheet
     * @return
     */
    fun widthShouldClickCloseBottomSheet(shouldClickCloseBottomSheet: Boolean): GalleryBuilder {
        galleryParam.shouldClickCloseBottomSheet = shouldClickCloseBottomSheet
        return this
    }

    /**
     * 是否可以拖拽上滑
     * @param canTouchDrag
     * @return
     */
    fun withCanTouchDrag(canTouchDrag: Boolean): GalleryBuilder {
        galleryParam.canTouchDrag = canTouchDrag
        return this
    }

    /**
     * 打开相册预览
     */
    fun startGallery() {
        val activity = galleryEngine.activity ?: return
        val intent = Intent(activity, GalleryActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val fragment = galleryEngine.fragment
        if (fragment != null) {
            fragment.startActivity(intent)
        } else {
            activity.startActivity(intent)
            //            activity.overridePendingTransition(R.anim.fade, R.anim.hold);
        }
    }

}