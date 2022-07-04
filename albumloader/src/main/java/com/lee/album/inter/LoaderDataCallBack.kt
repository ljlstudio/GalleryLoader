package com.lee.album.inter

import com.lee.album.entity.AlbumData
import com.lee.album.entity.GalleryInfoEntity

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/515:11
 * Package: com.lee.album.inter
 * desc   :
 */
interface LoaderDataCallBack {
    /**
     * 加载标题列表数据回调
     *
     * @param list
     */
    fun loadClassyDataSuccess(list: List<AlbumData?>?)

    /**
     * 加载列表数据回调
     *
     * @param pageData
     * @param currentAllData
     */
    fun loadListDataSuccess(
        pageData: List<GalleryInfoEntity?>?,
        currentAllData: List<GalleryInfoEntity?>?
    )

    /**
     * 切换不同标题时 清空数据回调
     */
    fun clearData()
}