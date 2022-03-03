package com.lee.album.inter;

import com.lee.album.entity.AlbumData;
import com.lee.album.entity.GalleryInfoEntity;

import java.util.List;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/515:11
 * Package: com.lee.album.inter
 * desc   :
 */
public interface LoaderDataCallBack {

    /**
     * 加载标题列表数据回调
     *
     * @param list
     */
    void loadClassyDataSuccess(List<AlbumData> list);

    /**
     * 加载列表数据回调
     *
     * @param pageData
     * @param currentAllData
     */
    void loadListDataSuccess(List<GalleryInfoEntity> pageData, List<GalleryInfoEntity> currentAllData);

    /**
     * 切换不同标题时 清空数据回调
     */
    void clearData();
}
