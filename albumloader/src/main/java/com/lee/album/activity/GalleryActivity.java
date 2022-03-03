package com.lee.album.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lee.album.AlbumLoader;
import com.lee.album.AlbumLoaderBuilder;
import com.lee.album.R;
import com.lee.album.adapter.GalleryClassifyAdapter;
import com.lee.album.adapter.GalleryItemAdapter;
import com.lee.album.contract.GalleryCallbackContract;
import com.lee.album.databinding.GalleryLayoutBinding;
import com.lee.album.entity.AlbumData;
import com.lee.album.entity.GalleryInfoEntity;
import com.lee.album.inter.LoaderDataCallBack;
import com.lee.album.permission.PermissionUtils;
import com.lee.album.router.GalleryParam;
import com.lee.album.widget.GalleryGrideLayoutManager;
import com.lee.album.widget.GalleryLayoutManager;
import com.lee.album.widget.GridSpaceItemDecoration;
import com.yanzhenjie.permission.AndPermission;


import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GalleryActivity extends AppCompatActivity implements GalleryCallbackContract.GalleryLoaderDataCallBack, View.OnClickListener {

    private static final String TAG = GalleryActivity.class.getSimpleName();
    private AlbumLoader loader;
    private GalleryItemAdapter galleryItemAdapter;
    private GalleryParam galleryParam;
    private GalleryLayoutBinding binding;
    private GalleryClassifyAdapter galleryClassifyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.gallery_layout, null, false);
        setContentView(binding.getRoot());


        galleryParam = GalleryParam.getInstance();
        loader = new AlbumLoader();
        loader.setAlbumLoaderBuilder(new AlbumLoaderBuilder().
                setCallBack(this).
                setPageSize(50).
                setShowLastModified(true).
                setLoadPaging(false));


        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.recycler.getLayoutParams();
        layoutParams.leftMargin = galleryParam.listPictureMargin;
        layoutParams.rightMargin = galleryParam.listPictureMargin;
        binding.recycler.setLayoutParams(layoutParams);

        loadData();

        setClassifyStatus(false);
        initListRecycler();
        initTitleClassifyRecycler();

        binding.centerBg.setOnClickListener(this);
    }


    /**
     * 初始化标题分类的recycler
     */
    private void initTitleClassifyRecycler() {
        binding.classifyRecycler.setLayoutManager(new GalleryLayoutManager(this));
        galleryClassifyAdapter = new GalleryClassifyAdapter(R.layout.item_gallery_list_layout);

        binding.classifyRecycler.setAdapter(galleryClassifyAdapter);

        galleryClassifyAdapter.setOnItemClickListener((adapter, view, position) -> {

            AlbumData albumData = (AlbumData) adapter.getData().get(position);
            if (albumData == null) {
                return;
            }
            setClassifyStatus(false);
            if (!TextUtils.isEmpty(binding.tvContent.getText()) && binding.tvContent.getText().equals(albumData.getAlbumName())) {
                return;
            }
            binding.tvContent.setText(albumData.getAlbumName());
            galleryItemAdapter.setNewInstance(null);
            loader.loadTitleListData(GalleryActivity.this, albumData.getAlbumName(), albumData.getId());
        });
    }


    /**
     * 初始化列表的recycler
     */
    private void initListRecycler() {
        binding.recycler.setLayoutManager(new GalleryGrideLayoutManager(getApplication(), 3));
        binding.recycler.addItemDecoration(new GridSpaceItemDecoration(3,
                galleryParam.listPictureRowSpace,
                galleryParam.listPictureColumnSpace));


        galleryItemAdapter = new GalleryItemAdapter(R.layout.item_gallery_layout, this, galleryParam);
        galleryItemAdapter.setOnItemClickListener((adapter, view, position) -> {
            GalleryInfoEntity galleryInfoEntity = (GalleryInfoEntity) adapter.getData().get(position);

            if (galleryParam.onGalleryListener != null && galleryInfoEntity != null) {
                galleryParam.onGalleryListener.clickGallery(galleryInfoEntity.getImgPath(), position);
            }
        });
        binding.recycler.setAdapter(galleryItemAdapter);
    }


    /**
     * 加载相册数据
     */
    private void loadData() {
        if (PermissionUtils.permissionsChecking(this, PermissionUtils.CLEAN_STORAGE_PERMISSIONS)) {
            loader.loadClassyData(this);
            loader.loadAllListData(this);
        } else {
            AndPermission.with(this)
                    .runtime()
                    .permission(PermissionUtils.CLEAN_STORAGE_PERMISSIONS)
                    .onGranted(permissions -> {

//                        loader.loadClassyData(this);
                        loader.loadAllListData(this);
                    })
                    .onDenied(permissions -> {


                    })
                    .start();
        }
    }


    /**
     * 实则分类列表状态
     *
     * @param open
     */
    public void setClassifyStatus(boolean open) {

        binding.classifyLayout.setVisibility(open ? View.VISIBLE : View.GONE);
        binding.ivCenter.setSelected(open);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loader != null) {
            loader.destroyLoader();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loader != null) {
            loader.resumeLoader();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (loader != null) {
            loader.pauseLoader();
        }
    }

    @Override
    public void loadClassyDataSuccess(List<AlbumData> list) {
        if (list != null && list.size() > 0) {
            Log.d(TAG, "classy size is" + list.size() + "thread name" + Thread.currentThread());
            galleryClassifyAdapter.setNewInstance(list);
        }
    }

    @Override
    public void loadListDataSuccess(List<GalleryInfoEntity> pageData, List<GalleryInfoEntity> currentAllData) {
        if (pageData != null && pageData.size() > 0) {
            Log.d(TAG, "size is" + pageData.size());
            Log.d(TAG, "all size is" + currentAllData.size());
            galleryItemAdapter.addData(pageData);
        }
    }

    @Override
    public void clearData() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.center_bg) {
            setClassifyStatus(binding.classifyLayout.getVisibility() == View.GONE);
        }
    }
}