package com.lee.galleryloader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lee.album.AlbumLoader
import com.lee.album.AlbumLoaderBuilder
import com.lee.album.entity.AlbumData
import com.lee.album.entity.GalleryInfoEntity
import com.lee.album.inter.LoaderDataCallBack
import com.lee.album.permission.PermissionUtils

import com.yanzhenjie.permission.AndPermission


class LoaderActivity : AppCompatActivity(), LoaderDataCallBack {


    private var loader: AlbumLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loader = AlbumLoader()

        loader?.setAlbumLoaderBuilder(
            AlbumLoaderBuilder().setCallBack(this).setPageSize(10).setShowLastModified(true)
                .setShouldLoadPaging(false)
        )
        loadData()
    }

    /**
     * 加载相册数据
     */
    private fun loadData() {
        if (PermissionUtils.permissionsChecking(this, PermissionUtils.CLEAN_STORAGE_PERMISSIONS)) {
            loader!!.loadClassyData(this)
            loader!!.loadAllListData(this)
        } else {
            AndPermission.with(this)
                .runtime()
                .permission(*PermissionUtils.CLEAN_STORAGE_PERMISSIONS)
                .onGranted {
                    loader!!.loadClassyData(this)
                    loader!!.loadAllListData(this)
                }
                .onDenied { }
                .start()
        }
    }


    override fun loadClassyDataSuccess(list: List<AlbumData?>?) {
        // TODO:  //相册分类数据回调
    }

    override fun loadListDataSuccess(
        pageData: List<GalleryInfoEntity?>?,
        currentAllData: List<GalleryInfoEntity?>?
    ) {
        // TODO:  相册列表数据回调
    }

    override fun clearData() {
        // TODO: 切换分类数据
    }

    override fun onDestroy() {
        super.onDestroy()
        loader?.destroyLoader()
    }

    override fun onResume() {
        super.onResume()
        loader?.resumeLoader()
    }

    override fun onStop() {
        super.onStop()
        loader?.pauseLoader()
    }
}