package com.lee.galleryloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.lee.album.inter.OnGalleryListener;
import com.lee.album.router.GalleryBuilder;
import com.lee.album.router.GalleryEngine;
import com.lee.galleryloader.databinding.ActivityGalleryBottomSheetBinding;
import com.lee.galleryloader.databinding.ActivityMainBinding;

public class BottomSheetActivity extends AppCompatActivity {

    private static final String TAG = BottomSheetActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityGalleryBottomSheetBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_gallery_bottom_sheet, null, false);
        setContentView(binding.getRoot());


        GalleryEngine.from(BottomSheetActivity.this)
                .setGalleryBuilder(BottomSheetActivity.this)
                .widthListPictureMargin(5)
                .widthListPictureColumnSpace(5)
                .widthListPictureRowSpace(5)
                .widthListPictureCorner(5)
                .withShouldLoadPaging(false)
                .widthPageSize(10)
                .widthShouldClickCloseBottomSheet(false)
                .withCanTouchDrag(false)
                .widthListPicturePlaceholder(R.color.design_snackbar_background_color)
                .widthOnGalleryListener(new OnGalleryListener() {
                    @Override
                    public void clickGallery(String path, int position) {
                        Log.d(TAG,"------->PATH=" + path + "------->position=" + position);

                    }

                    @Override
                    public void bottomSheetState(boolean isOpen, boolean fromUser) {
                        Log.d(TAG,"抽屉状态" + isOpen);

                    }

                    @Override
                    public void clickBadPicture(String path, int position) {
                        Log.d(TAG,"------->点击了 损坏图片 PATH=" + path + "------->position=" + position);
                    }
                });


        binding.bottomSheet.initData(this);
    }
}