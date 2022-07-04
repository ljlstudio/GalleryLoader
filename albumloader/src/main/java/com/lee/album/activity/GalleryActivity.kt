package com.lee.album.activity

import com.lee.album.router.GalleryParam.Companion.instance
import androidx.appcompat.app.AppCompatActivity
import com.lee.album.contract.GalleryCallbackContract.GalleryLoaderDataCallBack
import com.lee.album.AlbumLoader
import com.lee.album.adapter.GalleryItemAdapter
import com.lee.album.router.GalleryParam
import com.lee.album.adapter.GalleryClassifyAdapter
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import com.lee.album.R
import com.lee.album.AlbumLoaderBuilder
import android.widget.RelativeLayout
import com.lee.album.widget.GalleryLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lee.album.entity.AlbumData
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.lee.album.widget.GalleryGridLayoutManager
import com.lee.album.widget.GridSpaceItemDecoration
import com.lee.album.entity.GalleryInfoEntity
import com.lee.album.permission.PermissionUtils
import com.yanzhenjie.permission.AndPermission
import com.lee.album.databinding.GalleryLayoutBinding

class GalleryActivity : AppCompatActivity(), GalleryLoaderDataCallBack, View.OnClickListener {
    private var loader: AlbumLoader? = null
    private var galleryItemAdapter: GalleryItemAdapter? = null
    private var galleryParam: GalleryParam? = null
    private var binding: GalleryLayoutBinding? = null
    private var galleryClassifyAdapter: GalleryClassifyAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.gallery_layout, null, false)
        setContentView(binding?.root)
        galleryParam = instance
        loader = AlbumLoader()
        loader!!.setAlbumLoaderBuilder(
            AlbumLoaderBuilder().setCallBack(this).setPageSize(
                galleryParam!!.pageSize
            ).setShowLastModified(true).setShouldLoadPaging(galleryParam!!.shouldLoadPaging)
        )
        val layoutParams = binding?.recycler?.layoutParams as RelativeLayout.LayoutParams
        layoutParams.leftMargin = galleryParam!!.listPictureMargin
        layoutParams.rightMargin = galleryParam!!.listPictureMargin
        binding?.recycler?.layoutParams = layoutParams
        loadData()
        setClassifyStatus(false)
        initListRecycler()
        initTitleClassifyRecycler()
        binding?.centerBg?.setOnClickListener(this)
    }

    /**
     * 初始化标题分类的recycler
     */
    private fun initTitleClassifyRecycler() {
        binding!!.classifyRecycler.layoutManager = GalleryLayoutManager(this)
        galleryClassifyAdapter =
            GalleryClassifyAdapter(R.layout.item_gallery_list_layout, galleryParam)
        binding!!.classifyRecycler.adapter = galleryClassifyAdapter
        galleryClassifyAdapter!!.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>, view: View?, position: Int ->
            val albumData = adapter.data[position] as AlbumData? ?: return@setOnItemClickListener
            setClassifyStatus(false)
            if (!TextUtils.isEmpty(binding!!.tvContent.text) && binding!!.tvContent.text == albumData.albumName) {
                return@setOnItemClickListener
            }
            binding!!.tvContent.text = albumData.albumName
            galleryItemAdapter!!.setNewInstance(null)
            loader!!.loadTitleListData(this@GalleryActivity, albumData.albumName!!, albumData.id)
        }
    }

    /**
     * 初始化列表的recycler
     */
    private fun initListRecycler() {
        binding!!.recycler.layoutManager = GalleryGridLayoutManager(application, 3)
        binding!!.recycler.addItemDecoration(
            GridSpaceItemDecoration(
                3,
                galleryParam!!.listPictureRowSpace,
                galleryParam!!.listPictureColumnSpace
            )
        )
        galleryItemAdapter = GalleryItemAdapter(R.layout.item_gallery_layout, this, galleryParam)
        galleryItemAdapter!!.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>, view: View?, position: Int ->
            val galleryInfoEntity = adapter.data[position] as GalleryInfoEntity?
            if (galleryParam!!.onGalleryListener != null && galleryInfoEntity != null) {
                galleryParam!!.onGalleryListener!!.clickGallery(galleryInfoEntity.imgPath, position)
            }
        }
        binding!!.recycler.adapter = galleryItemAdapter
    }

    /**
     * 加载相册数据
     */
    private fun loadData() {
        if (PermissionUtils.permissionsChecking(
                this,
                PermissionUtils.CLEAN_STORAGE_PERMISSIONS
            )
        ) {
            loader?.loadClassyData(this)
            loader?.loadAllListData(this)
        } else {
            AndPermission.with(this)
                .runtime()
                .permission(*PermissionUtils.CLEAN_STORAGE_PERMISSIONS)
                .onGranted {
                    loader?.loadClassyData(this)
                    loader?.loadAllListData(this)
                }
                .onDenied {

                }
                .start()
        }
    }

    /**
     * 实则分类列表状态
     *
     * @param open
     */
    private fun setClassifyStatus(open: Boolean) {
        binding!!.classifyLayout.visibility =
            if (open) View.VISIBLE else View.GONE
        binding!!.ivCenter.isSelected = open
    }

    override fun onDestroy() {
        super.onDestroy()
        if (loader != null) {
            loader!!.destroyLoader()
        }
    }

    override fun onResume() {
        super.onResume()
        if (loader != null) {
            loader!!.resumeLoader()
        }
    }

    override fun onStop() {
        super.onStop()
        if (loader != null) {
            loader!!.pauseLoader()
        }
    }

    override fun loadClassyDataSuccess(list: List<AlbumData?>?) {
        if (list != null && list.size > 0) {
            Log.d(TAG, "classy size is" + list.size + "thread name" + Thread.currentThread())
            galleryClassifyAdapter?.setNewInstance(list as MutableList<AlbumData>)
        }
    }

    override fun loadListDataSuccess(
        pageData: List<GalleryInfoEntity?>?,
        currentAllData: List<GalleryInfoEntity?>?
    ) {
        if (pageData != null && pageData.isNotEmpty()) {
            Log.d(TAG, "size is" + pageData.size)
            Log.d(TAG, "all size is" + currentAllData!!.size)
            galleryItemAdapter?.addData(pageData as MutableList<GalleryInfoEntity>)
        }
    }

    override fun clearData() {}
    override fun onClick(v: View) {
        if (v.id == R.id.center_bg) {
            setClassifyStatus(binding!!.classifyLayout.visibility == View.GONE)
        }
    }

    companion object {
        private val TAG = GalleryActivity::class.java.simpleName
    }
}