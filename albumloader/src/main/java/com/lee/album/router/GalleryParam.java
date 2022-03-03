package com.lee.album.router;

import com.lee.album.inter.OnGalleryListener;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/2514:00
 * Package: com.lee.album.router
 * desc   :
 */
public class GalleryParam {
    private static final GalleryParam mInstance = new GalleryParam();

    /**
     * 列表图片圆角
     */
    public int listPictureCorner;

    /**
     * 列表图片列间距（图片之间）
     */
    public int listPictureColumnSpace;


    /**
     * 列表图片行间距（图片之间）
     */
    public int listPictureRowSpace;

    /**
     * 列表图片左右距离屏幕间距
     */
    public int listPictureMargin;

    /**
     * 列表图片占位
     */
    public int listPicturePlaceholder = -1;


    /**
     * 相册接口回调
     */
    public OnGalleryListener onGalleryListener;

    /**
     * 是否分页
     */
    public boolean shouldLoadPaging;

    /**
     * 分页加载一次每次返回条数 建议10-30之间
     */
    public int pageSize;

    /**
     * 获取相机配置参数
     *
     * @return
     */
    public static GalleryParam getInstance() {
        return mInstance;
    }


    private GalleryParam() {
        reset();
    }

    /**
     * 重置数据
     */
    public void reset() {
        listPictureCorner = 0;
        listPictureColumnSpace = 0;
        listPictureMargin = 0;
        listPictureRowSpace = 0;
        listPicturePlaceholder = -1;
        shouldLoadPaging = false;
        pageSize = 10;
    }


}