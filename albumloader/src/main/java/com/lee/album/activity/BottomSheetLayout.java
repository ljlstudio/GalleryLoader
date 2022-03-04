package com.lee.album.activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.lee.album.AlbumLoader;
import com.lee.album.AlbumLoaderBuilder;
import com.lee.album.R;
import com.lee.album.adapter.GalleryClassifyAdapter;
import com.lee.album.adapter.GalleryItemAdapter;
import com.lee.album.contract.GalleryCallbackContract;
import com.lee.album.databinding.ActivityBottomSheetBinding;
import com.lee.album.entity.AlbumData;
import com.lee.album.entity.GalleryInfoEntity;
import com.lee.album.router.GalleryParam;
import com.lee.album.utils.Utils;
import com.lee.album.widget.CommonBottomSheetBehavior;
import com.lee.album.widget.CommonGalleryGrideLayoutManager;
import com.lee.album.widget.CommonGalleryLayoutManager;
import com.lee.album.widget.FunctionGridSpaceItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class BottomSheetLayout extends RelativeLayout implements GalleryCallbackContract.GalleryLoaderDataCallBack, View.OnClickListener {

    private ActivityBottomSheetBinding binding;
    private CommonBottomSheetBehavior<RelativeLayout> commonBottomSheetBehavior;
    private AlbumLoader albumLoader;
    private GalleryParam galleryParam;
    private GalleryItemAdapter editorGalleryItemAdapter;
    private GalleryClassifyAdapter editorGalleryListAdapter;
    private Context context;

    public BottomSheetLayout(Context context) {
        super(context);
    }

    public BottomSheetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    public BottomSheetLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        this.context = context;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activity_bottom_sheet, this, true);

        binding.centerBg.setOnClickListener(this);
        binding.ivResizeClose.setOnClickListener(this);
        binding.ivBackLayout.setOnClickListener(this);

        commonBottomSheetBehavior = CommonBottomSheetBehavior.from(binding.layout);

        commonBottomSheetBehavior.addBottomSheetCallback(new CommonBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {
                binding.layout.setVisibility(slideOffset == 0 ? View.GONE : View.VISIBLE);

            }
        });

    }

    public void initData(FragmentActivity activity) {

        galleryParam = GalleryParam.getInstance();
        commonBottomSheetBehavior.setDraggable(galleryParam.canTouchDrag);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.recycler.getLayoutParams();
        layoutParams.leftMargin = galleryParam.listPictureMargin;
        layoutParams.rightMargin = galleryParam.listPictureMargin;
        binding.recycler.setLayoutParams(layoutParams);

        setPeekHeight();
        showExpanded();
        initRecycler();
        initClassifyList();
        setClassifyStatus(false);
        setBackStatus(false);


        albumLoader = new AlbumLoader();
        albumLoader.setAlbumLoaderBuilder(new AlbumLoaderBuilder()
                .setCallBack(this)
                .setPageSize(galleryParam.pageSize)
                .setShowLastModified(true)
                .setShouldLoadPaging(galleryParam.shouldLoadPaging));

        albumLoader.loadAllListData(context);
        albumLoader.loadClassyData(activity);
    }

    /**
     * 列表分类recycler
     */
    private void initClassifyList() {
        CommonGalleryLayoutManager linearLayoutManager = new CommonGalleryLayoutManager(context);

        binding.recyclerList.setLayoutManager(linearLayoutManager);
        binding.recyclerList.setNestedScrollingEnabled(false);
        editorGalleryListAdapter = new GalleryClassifyAdapter(R.layout.item_gallery_list_layout, galleryParam);
        editorGalleryListAdapter.setOnItemClickListener((adapter, view, position) -> {

            AlbumData albumData = (AlbumData) adapter.getData().get(position);
            if (albumData != null) {
                setClassifyStatus(false);
                if (!TextUtils.isEmpty(binding.tvContent.getText()) && binding.tvContent.getText().equals(albumData.getAlbumName())) {
                    return;
                }

                binding.tvContent.setText(albumData.getAlbumName());
                albumLoader.release();
                editorGalleryItemAdapter.setNewInstance(null);
                albumLoader.loadTitleListData(context, albumData.getAlbumName(), albumData.getId());

            }

        });
        binding.recyclerList.setAdapter(editorGalleryListAdapter);
    }


    private void initRecycler() {
        binding.recycler.setHasFixedSize(false);
        CommonGalleryGrideLayoutManager manager = new CommonGalleryGrideLayoutManager(context, 3);
        binding.recycler.setNestedScrollingEnabled(false);
        binding.recycler.setLayoutManager(manager);
        binding.recycler.addItemDecoration(new FunctionGridSpaceItemDecoration(3,
                Utils.dip2px(context, 5),
                Utils.dip2px(context, 5)));

        editorGalleryItemAdapter = new GalleryItemAdapter(R.layout.item_gallery_layout, context, galleryParam);


        editorGalleryItemAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (galleryParam.onGalleryListener != null && adapter.getData().size() > position) {
                GalleryInfoEntity galleryInfoEntity = (GalleryInfoEntity) adapter.getData().get(position);
                if (galleryInfoEntity != null) {

                    //判断图片是否损坏
                    if (TextUtils.isEmpty(galleryInfoEntity.getImgPath()) || !Utils.existsFile(new File(galleryInfoEntity.getImgPath()))) {
                        galleryParam.onGalleryListener.clickBadPicture(galleryInfoEntity.getImgPath(), position);
                        return;
                    }


                    setBackStatus(true);
                    galleryParam.onGalleryListener.clickGallery(galleryInfoEntity.getImgPath(), position);
                    if (galleryParam.shouldClickCloseBottomSheet) {
                        collapsed();
                    }
                }
            }
        });
        binding.recycler.setAdapter(editorGalleryItemAdapter);
    }


    /**
     * 设置分类列表状态
     *
     * @param open
     */
    public void setClassifyStatus(boolean open) {
        binding.rlListLayout.setVisibility(open ? VISIBLE : GONE);
        binding.ivCenter.setSelected(open);

    }

    /**
     * 关闭
     */
    public void collapsed() {
        if (commonBottomSheetBehavior != null && commonBottomSheetBehavior.getState() != CommonBottomSheetBehavior.STATE_SETTLING) {
            commonBottomSheetBehavior.setState(CommonBottomSheetBehavior.STATE_COLLAPSED);
            commonBottomSheetBehavior.setPeekHeight(0);
            binding.ivResize.setBackgroundResource(R.drawable.white_resize_selector);
            if (galleryParam.onGalleryListener != null) {
                galleryParam.onGalleryListener.bottomSheetState(false, false);
            }
        }
    }

    /**
     * 展开状态
     */
    public void expanded() {
        if (commonBottomSheetBehavior != null && commonBottomSheetBehavior.getState() != CommonBottomSheetBehavior.STATE_SETTLING) {
            if (commonBottomSheetBehavior.getState() == CommonBottomSheetBehavior.STATE_EXPANDED) {
                commonBottomSheetBehavior.setState(CommonBottomSheetBehavior.STATE_HALF_EXPANDED);
                binding.ivResize.setBackgroundResource(R.drawable.white_resize_selector);
                if (galleryParam.onGalleryListener != null) {
                    galleryParam.onGalleryListener.bottomSheetState(false, true);
                }
            } else {
                binding.ivResize.setBackgroundResource(R.drawable.white_resize_close_selector);
                commonBottomSheetBehavior.setState(CommonBottomSheetBehavior.STATE_EXPANDED);
                if (galleryParam.onGalleryListener != null) {
                    galleryParam.onGalleryListener.bottomSheetState(true, true);
                }
            }
        }
    }

    /**
     * 设置返回键状态
     *
     * @param b
     */
    public void setBackStatus(boolean b) {
        binding.ivBackLayout.setVisibility(b ? VISIBLE : INVISIBLE);
    }

    /**
     * 展示相册
     */
    public void showExpanded() {

        binding.ivResize.setBackgroundResource(R.drawable.white_resize_selector);
        commonBottomSheetBehavior.setState(CommonBottomSheetBehavior.STATE_HALF_EXPANDED);

    }


    /**
     * 设置初始化高度
     */
    private void setPeekHeight() {
        if (commonBottomSheetBehavior != null) {
            commonBottomSheetBehavior.setPeekHeight(0);
        }

    }

    @Override
    public void loadClassyDataSuccess(List<AlbumData> list) {
//        galleryClassifyAdapter.setNewInstance(list);

        if (list != null && list.size() > 0) {

            editorGalleryListAdapter.setNewInstance(list);
        }
    }


    @Override
    public void loadListDataSuccess(List<GalleryInfoEntity> pageData, List<GalleryInfoEntity> currentAllData) {

        if (editorGalleryItemAdapter != null && pageData != null && pageData.size() > 0) {
            editorGalleryItemAdapter.addData(editorGalleryItemAdapter.getItemCount(), pageData);
        }


    }

    @Override
    public void clearData() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_resize_close) {
            expanded();
        } else if (v.getId() == R.id.iv_back_layout) {
            collapsed();
        } else if (v.getId() == R.id.center_bg) {
            setClassifyStatus(binding.rlListLayout.getVisibility() == GONE);
        }
    }
}