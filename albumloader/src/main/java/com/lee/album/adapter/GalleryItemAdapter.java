package com.lee.album.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.imageview.ShapeableImageView;
import com.lee.album.R;
import com.lee.album.entity.GalleryInfoEntity;
import com.lee.album.router.GalleryParam;
import com.lee.album.utils.Utils;


import org.jetbrains.annotations.NotNull;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2021/2/2416:35
 * Package: com.shyz.picture.gallery
 * desc   :
 */
public class GalleryItemAdapter extends BaseQuickAdapter<GalleryInfoEntity, BaseViewHolder> {


    private GalleryParam galleryParam;
    private int height;

    public GalleryItemAdapter(int layoutResId, Context context, GalleryParam galleryParam) {
        super(layoutResId);
        height = (int) ((Utils.getScreenWidth(context)
                - galleryParam.listPictureColumnSpace * 2 - galleryParam.listPictureMargin * 2) / 3f);
        this.galleryParam = galleryParam;
    }


    @Override
    public void onBindViewHolder(@NotNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewGroup.LayoutParams layoutParams = holder.getView(R.id.layout).getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = height;
        holder.getView(R.id.layout).setLayoutParams(layoutParams);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, GalleryInfoEntity galleryInfoEntity) {

        ShapeableImageView iv = holder.getView(R.id.img);

        Glide.with(getContext())
                .load(galleryInfoEntity.getImgPath())
                .placeholder(galleryParam.listPicturePlaceholder == -1 ?
                        iv.getDrawable() :
                        getContext().getResources().getDrawable(galleryParam.listPicturePlaceholder))
                .into(iv);

        iv.setShapeAppearanceModel(iv.getShapeAppearanceModel().withCornerSize(galleryParam.listPictureCorner));

    }


}