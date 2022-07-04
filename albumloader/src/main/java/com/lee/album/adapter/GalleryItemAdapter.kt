package com.lee.album.adapter


import com.lee.album.router.GalleryParam
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lee.album.entity.GalleryInfoEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lee.album.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.lee.album.utils.Utils

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2021/2/2416:35
 * Package: com.shyz.picture.gallery
 * desc   :
 */
class GalleryItemAdapter(
    layoutResId: Int, context: Context?,
    private val galleryParam: GalleryParam?
) :
    BaseQuickAdapter<GalleryInfoEntity, BaseViewHolder>(layoutResId) {
    private val height: Int =
        ((Utils.getScreenWidth(context) - (galleryParam?.listPictureColumnSpace?.times(
            2
        ) ?: 0) - (galleryParam?.listPictureMargin?.times(2) ?: 0)) / 3f).toInt()

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val layoutParams = holder.getView<View>(R.id.layout).layoutParams
        layoutParams.height = height
        layoutParams.width = height
        holder.getView<View>(R.id.layout).layoutParams = layoutParams
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun convert(holder: BaseViewHolder, galleryInfoEntity: GalleryInfoEntity) {
        val iv = holder.getView<ShapeableImageView>(R.id.img)
        Glide.with(context)
            .load(galleryInfoEntity.imgPath)
            .placeholder(
                if (galleryParam?.listPicturePlaceholder == -1) iv.drawable else galleryParam?.listPicturePlaceholder?.let {
                    context.resources.getDrawable(
                        it
                    )
                }
            )
            .into(iv)
        iv.shapeAppearanceModel =
            iv.shapeAppearanceModel.withCornerSize(galleryParam?.listPictureCorner?.toFloat() ?: 1.0f)
    }

}