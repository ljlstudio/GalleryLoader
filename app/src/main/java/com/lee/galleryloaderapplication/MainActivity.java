package com.lee.galleryloaderapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.lee.album.inter.OnGalleryListener;
import com.lee.album.router.GalleryEngine;
import com.lee.galleryloaderapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityMainBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_main, null, false);
        setContentView(binding.getRoot());

        binding.insertGallery.setOnClickListener(v -> GalleryEngine.from(MainActivity.this)
                .setGalleryBuilder(MainActivity.this)
                .widthListPictureMargin(5)
                .widthListPictureColumnSpace(5)
                .widthListPictureRowSpace(5)
                .widthListPictureCorner(5)
                .widthListPicturePlaceholder(R.color.design_snackbar_background_color)
                .widthOnGalleryListener((path, position) -> {
                    Toast.makeText(MainActivity.this, "------->PATH=" + path + "------->position=" + position, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "PATH" + path + "position" + position);
                })
                .startGallery());


    }


}