package com.lee.album.contract

import com.lee.album.inter.LoaderDataCallBack
import com.lee.album.entity.AlbumData
import com.lee.album.entity.GalleryInfoEntity

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/3/310:25
 * Package: com.lee.album.contract
 * desc   : 接口管理类,方便查询管理
 */
class GalleryCallbackContract {
    interface GalleryLoaderDataCallBack : LoaderDataCallBack {
        override fun loadClassyDataSuccess(list: List<AlbumData?>?)
        override fun loadListDataSuccess(
            pageData: List<GalleryInfoEntity?>?,
            currentAllData: List<GalleryInfoEntity?>?
        )

        override fun clearData()
    }
}