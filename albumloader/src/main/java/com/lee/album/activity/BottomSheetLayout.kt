package com.lee.album.activity

import android.content.Context
import android.widget.RelativeLayout
import com.lee.album.contract.GalleryCallbackContract.GalleryLoaderDataCallBack
import com.lee.album.widget.CommonBottomSheetBehavior
import com.lee.album.AlbumLoader
import com.lee.album.router.GalleryParam
import com.lee.album.adapter.GalleryItemAdapter
import com.lee.album.adapter.GalleryClassifyAdapter
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import com.lee.album.R
import androidx.fragment.app.FragmentActivity
import com.lee.album.AlbumLoaderBuilder
import com.lee.album.widget.CommonGalleryLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lee.album.entity.AlbumData
import android.util.AttributeSet
import android.view.View
import com.lee.album.databinding.ActivityBottomSheetBinding
import com.lee.album.widget.CommonGalleryGridLayoutManager
import com.lee.album.widget.FunctionGridSpaceItemDecoration
import com.lee.album.entity.GalleryInfoEntity
import com.lee.album.utils.Utils
import java.io.File

open class BottomSheetLayout : RelativeLayout, GalleryLoaderDataCallBack, View.OnClickListener {
    private var binding: ActivityBottomSheetBinding? = null
    private var commonBottomSheetBehavior: CommonBottomSheetBehavior<RelativeLayout>? = null
    private var albumLoader: AlbumLoader? = null
    private var galleryParam: GalleryParam? = null
    private var editorGalleryItemAdapter: GalleryItemAdapter? = null
    private var editorGalleryListAdapter: GalleryClassifyAdapter? = null


    constructor(context: Context?) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context?) {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.activity_bottom_sheet,
            this,
            true
        )
        binding?.centerBg?.setOnClickListener(this)
        binding?.ivResizeClose?.setOnClickListener(this)
        binding?.ivBackLayout?.setOnClickListener(this)
        commonBottomSheetBehavior = CommonBottomSheetBehavior.from(binding!!.layout)
        commonBottomSheetBehavior?.addBottomSheetCallback(object :
            CommonBottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding?.layout?.visibility = if (slideOffset == 0f) GONE else VISIBLE
            }
        })
    }

    fun initData(activity: FragmentActivity?) {
        galleryParam = GalleryParam.instance
        commonBottomSheetBehavior?.isDraggable = galleryParam?.canTouchDrag == true
        val layoutParams = binding?.recycler?.layoutParams as LayoutParams
        layoutParams.leftMargin = galleryParam!!.listPictureMargin
        layoutParams.rightMargin = galleryParam!!.listPictureMargin
        binding?.recycler?.layoutParams = layoutParams
        setPeekHeight()
        showExpanded()
        initRecycler()
        initClassifyList()
        setClassifyStatus(false)
        setBackStatus(false)
        albumLoader = AlbumLoader()
        albumLoader?.setAlbumLoaderBuilder(
            AlbumLoaderBuilder()
                .setCallBack(this)
                .setPageSize(galleryParam?.pageSize)
                .setShowLastModified(true)
                .setShouldLoadPaging(galleryParam?.shouldLoadPaging == true)
        )
        albumLoader?.loadAllListData(context)
        albumLoader?.loadClassyData(activity)
    }

    /**
     * 列表分类recycler
     */
    private fun initClassifyList() {
        val linearLayoutManager = CommonGalleryLayoutManager(context)
        binding?.recyclerList?.layoutManager = linearLayoutManager
        binding?.recyclerList?.isNestedScrollingEnabled = false
        editorGalleryListAdapter =
            GalleryClassifyAdapter(R.layout.item_gallery_list_layout, galleryParam)
        editorGalleryListAdapter?.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>, _: View?, position: Int ->
            val albumData = adapter.data[position] as AlbumData?

            albumData?.let {
                setClassifyStatus(false)
                if (binding?.tvContent?.text?.isNotEmpty() == true && binding?.tvContent?.text == albumData.albumName) {
                    return@setOnItemClickListener
                }
                binding?.tvContent?.text = albumData.albumName
                albumLoader?.release()
                editorGalleryItemAdapter?.setNewInstance(null)
                albumLoader?.loadTitleListData(context, albumData.albumName.toString(), albumData.id)

            }


        }
        binding?.recyclerList?.adapter = editorGalleryListAdapter
    }

    private fun initRecycler() {
        binding?.recycler?.setHasFixedSize(false)
        val manager = CommonGalleryGridLayoutManager(context, 3)
        binding?.recycler?.isNestedScrollingEnabled = false
        binding?.recycler?.layoutManager = manager
        binding?.recycler?.addItemDecoration(
            FunctionGridSpaceItemDecoration(
                3,
                Utils.dip2px(context, 5f),
                Utils.dip2px(context, 5f)
            )
        )
        editorGalleryItemAdapter =
            GalleryItemAdapter(R.layout.item_gallery_layout, context, galleryParam)
        editorGalleryItemAdapter?.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>, _: View?, position: Int ->
            if (galleryParam?.onGalleryListener != null && adapter.data.size > position) {
                val galleryInfoEntity = adapter.data[position] as GalleryInfoEntity?

                galleryInfoEntity?.let {


                    //判断图片是否损坏
                    if (galleryInfoEntity.imgPath?.isEmpty() == true || !Utils.existsFile(
                            File(
                                galleryInfoEntity.imgPath
                            )
                        )
                    ) {
                        galleryParam?.onGalleryListener?.clickBadPicture(
                            galleryInfoEntity.imgPath,
                            position
                        )
                        return@setOnItemClickListener
                    }
                    setBackStatus(true)
                    galleryParam?.onGalleryListener?.clickGallery(
                        galleryInfoEntity.imgPath,
                        position
                    )
                    if (galleryParam?.shouldClickCloseBottomSheet == true) {
                        collapsed()
                    }
                }

            }
        }
        binding?.recycler?.adapter = editorGalleryItemAdapter
    }

    /**
     * 设置分类列表状态
     *
     * @param open
     */
    private fun setClassifyStatus(open: Boolean) {
        binding?.rlListLayout?.visibility =
            if (open) VISIBLE else GONE
        binding?.ivCenter?.isSelected = open
    }

    /**
     * 关闭
     */
    private fun collapsed() {
        if (commonBottomSheetBehavior != null && commonBottomSheetBehavior?.state != CommonBottomSheetBehavior.STATE_SETTLING) {
            commonBottomSheetBehavior?.state = CommonBottomSheetBehavior.STATE_COLLAPSED
            commonBottomSheetBehavior?.peekHeight = 0
            binding?.ivResize?.setBackgroundResource(R.drawable.white_resize_selector)
            galleryParam?.onGalleryListener?.bottomSheetState(false, false)

        }
    }

    /**
     * 展开状态
     */
    private fun expanded() {
        if (commonBottomSheetBehavior != null && commonBottomSheetBehavior?.state != CommonBottomSheetBehavior.STATE_SETTLING) {
            if (commonBottomSheetBehavior?.state == CommonBottomSheetBehavior.STATE_EXPANDED) {
                commonBottomSheetBehavior?.state = CommonBottomSheetBehavior.STATE_HALF_EXPANDED
                binding?.ivResize?.setBackgroundResource(R.drawable.white_resize_selector)
                galleryParam?.onGalleryListener?.bottomSheetState(false, true)

            } else {
                binding?.ivResize?.setBackgroundResource(R.drawable.white_resize_close_selector)
                commonBottomSheetBehavior?.state = CommonBottomSheetBehavior.STATE_EXPANDED
                galleryParam?.onGalleryListener?.bottomSheetState(true, true)
            }
        }
    }

    /**
     * 设置返回键状态
     *
     * @param b
     */
    private fun setBackStatus(b: Boolean) {
        binding?.ivBackLayout?.visibility =
            if (b) VISIBLE else INVISIBLE
    }

    /**
     * 展示相册
     */
    private fun showExpanded() {
        binding?.ivResize?.setBackgroundResource(R.drawable.white_resize_selector)
        commonBottomSheetBehavior?.state = CommonBottomSheetBehavior.STATE_HALF_EXPANDED
    }

    /**
     * 设置初始化高度
     */
    private fun setPeekHeight() {
        if (commonBottomSheetBehavior != null) {
            commonBottomSheetBehavior?.peekHeight = 0
        }
    }

    override fun loadClassyDataSuccess(list: List<AlbumData?>?) {

        list?.let {
            editorGalleryListAdapter?.setNewInstance(list as MutableList<AlbumData>)
        }

    }

    override fun loadListDataSuccess(
        pageData: List<GalleryInfoEntity?>?,
        currentAllData: List<GalleryInfoEntity?>?
    ) {

        if (pageData?.isNotEmpty() == true) {
            editorGalleryItemAdapter?.itemCount?.let {
                editorGalleryItemAdapter?.addData(
                    it,
                    pageData as MutableList<GalleryInfoEntity>
                )
            }
        }


    }

    override fun clearData() {}
    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_resize_close -> {
                expanded()
            }
            R.id.iv_back_layout -> {
                collapsed()
            }
            R.id.center_bg -> {
                setClassifyStatus(binding?.rlListLayout?.visibility == GONE)
            }
        }
    }
}