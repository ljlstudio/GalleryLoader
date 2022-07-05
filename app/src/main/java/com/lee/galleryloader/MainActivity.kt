package com.lee.galleryloader

import com.lee.album.router.GalleryEngine.Companion.from
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import com.lee.album.inter.OnGalleryListener
import android.widget.Toast
import android.content.Intent
import android.view.View
import com.lee.galleryloader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_main, null, false)
        setContentView(binding.root)
        binding.insertGallery.setOnClickListener {
            from(this@MainActivity)
                .setGalleryBuilder(this@MainActivity)
                .widthListPictureMargin(5)
                .widthListPictureColumnSpace(5)
                .widthListPictureRowSpace(5)
                .widthListPictureCorner(5)
                .withShouldLoadPaging(false)
                .widthPageSize(10)
                .widthListPicturePlaceholder(R.color.design_snackbar_background_color)
                .widthOnGalleryListener(object : OnGalleryListener {
                    override fun clickGallery(path: String?, position: Int) {
                        Toast.makeText(
                            this@MainActivity,
                            "------->PATH=$path------->position=$position",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun bottomSheetState(isOpen: Boolean, fromUser: Boolean) {
                        Toast.makeText(this@MainActivity, "抽屉状态$isOpen", Toast.LENGTH_SHORT).show()
                    }

                    override fun clickBadPicture(path: String?, position: Int) {
                        Toast.makeText(
                            this@MainActivity,
                            "------->点击了 损坏图片 PATH=$path------->position=$position",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                .startGallery()
        }
        binding.insertGallery2.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    BottomSheetActivity::class.java
                )
            )
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}