package com.lee.album.adapter;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.imageview.ShapeableImageView;
import com.lee.album.R;
import com.lee.album.entity.AlbumData;
import com.lee.album.router.GalleryParam;

import org.jetbrains.annotations.NotNull;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2021/3/2217:33
 * Package: com.qtcx.picture.gallery.list
 * desc   :
 */
public class GalleryClassifyAdapter extends BaseQuickAdapter<AlbumData, BaseViewHolder> {


    public GalleryClassifyAdapter(int layoutResId, GalleryParam galleryParam) {
        super(layoutResId);

    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, AlbumData albumInfoEntity) {


        try {

            holder.setText(R.id.tv_name, albumInfoEntity.getAlbumName())
                    .setText(R.id.tv_count, albumInfoEntity.getCount() + "张");

            ShapeableImageView imageView = holder.getView(R.id.iv_head);

            Glide.with(getContext())
                    .load(albumInfoEntity.getCoverUri())
                    .centerCrop()
                    .placeholder(imageView.getDrawable())
                    .into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}