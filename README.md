# GalleryLoader
相册加载器、gallery、图片选择

1.基于RxJava 实现的相册加载器，能更好更快的加载本地大量相册图片并展示
2.内部提供相册展示页，也可以直接使用加载器回调数据供上层使用

# 集成步骤

**Step1:** 


项目根目录build文件中配置jitpack maven
```
 repositories {
        maven { url 'https://jitpack.io' }
    }
```


**Step2:**
app目录build文件中配置引用

```
     implementation 'com.github.ljlstudio:GalleryLoader:tag' ..tag为最新release 版本（1.0.4）
```


## 代码中使用

使用loader 有两种方式:

**1.使用自带相册样式：**


![device-2022-03-04-143816](https://user-images.githubusercontent.com/70507884/156712790-7de2b04c-7a42-48e9-b5ba-94ec55215681.gif)



* **样式1：全屏模式**

```
        binding.insertGallery.setOnClickListener(v -> 
        GalleryEngine.from(MainActivity.this)
                .setGalleryBuilder(MainActivity.this)
                .widthListPictureMargin(5)
                .widthListPictureColumnSpace(5)
                .widthListPictureRowSpace(5)
                .widthListPictureCorner(5)
                .withShouldLoadPaging(false)
                .widthPageSize(10)
                .widthListPicturePlaceholder(R.color.design_snackbar_background_color)
                .widthOnGalleryListener((path, position) -> {
                    Toast.makeText(MainActivity.this, "------->PATH=" + path + "------->position=" + position, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "PATH" + path + "position" + position);
                })
                .startGallery());

```

* **样式2：BottomSheet 抽屉模式**

```

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
```


对应参数介绍：

字段     |   含义
-------- | ---
listPictureMargin       |   设置图片距离屏幕两边距离
listPictureColumnSpace  |   列表图片列间距（图片之间）
listPictureRowSpace     |   列表图片行间距（图片之间）
listPictureCorner       |   设置图片圆角
shouldLoadPaging        |   是否分页
pageSize                |   分页加载一次每次返回条数 建议10-30之间
listPicturePlaceholder  |   占位图或者颜色
onGalleryListener       |   相册点击回调
shouldClickCloseBottomSheet | 是否点击图片关闭bottomSheet
canTouchDrag            |   是否可以拖拽上滑



 **2.使用数据加载器回调数据源：**




```
1.初始化加载器，设置 builder
loader = new AlbumLoader();

loader.setAlbumLoaderBuilder(new AlbumLoaderBuilder().
                setCallBack(this).
                setPageSize(10).
                setShowLastModified(true).
                setShouldLoadPaging(false);
                
                
                
                
                
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
                        loader.loadClassyData(this);
                        loader.loadAllListData(this);
                    })
                    .onDenied(permissions -> {


                    })
                    .start();
        }
    }
                
//监听数据回调

    @Override
    public void loadClassyDataSuccess(List<AlbumData> list) {
        if (list != null && list.size() > 0) {
        //相册分类数据回调  
        }
    }

    @Override
    public void loadListDataSuccess(List<GalleryInfoEntity> pageData, List<GalleryInfoEntity> currentAllData) {
        if (pageData != null && pageData.size() > 0) {
        //相册列表数据回调
        }
    }

    @Override
    public void clearData() {
    //切换分类数据
    }
    
    
    //生命周期管理
    
    
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
    
                
```








