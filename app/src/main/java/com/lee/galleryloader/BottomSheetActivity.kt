package com.lee.galleryloader

import com.lee.album.router.GalleryEngine.Companion.from
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import com.lee.galleryloader.R
import com.lee.album.router.GalleryEngine
import com.lee.album.inter.OnGalleryListener
import com.lee.galleryloader.BottomSheetActivity
import com.lee.galleryloader.databinding.ActivityGalleryBottomSheetBinding

class BottomSheetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityGalleryBottomSheetBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.activity_gallery_bottom_sheet,
            null,
            false
        )
        setContentView(binding.root)
        from(this@BottomSheetActivity)
            .setGalleryBuilder(this@BottomSheetActivity)
            .widthListPictureMargin(5)
            .widthListPictureColumnSpace(5)
            .widthListPictureRowSpace(5)
            .widthListPictureCorner(5)
            .withShouldLoadPaging(false)
            .widthPageSize(10)
            .widthShouldClickCloseBottomSheet(false)
            .withCanTouchDrag(false)
            .widthListPicturePlaceholder(R.color.design_snackbar_background_color)
            .widthOnGalleryListener(object : OnGalleryListener {
                override fun clickGallery(path: String?, position: Int) {
                    Log.d(TAG, "------->PATH=$path------->position=$position")
                }

                override fun bottomSheetState(isOpen: Boolean, fromUser: Boolean) {
                    Log.d(TAG, "抽屉状态$isOpen")
                }

                override fun clickBadPicture(path: String?, position: Int) {
                    Log.d(TAG, "------->点击了 损坏图片 PATH=$path------->position=$position")
                }
            })
        binding.bottomSheet.initData(this)
    }

    companion object {
        private val TAG = BottomSheetActivity::class.java.simpleName
    }
}