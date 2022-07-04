package com.lee.album

import com.lee.album.inter.LoaderDataCallBack

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/514:23
 * Package: com.lee.album
 * desc   :
 */
class AlbumLoaderBuilder {
    /**
     * 分页加载个数
     */
    var pageSize = 0
        private set

    /**
     * 是否显示最近项目
     */
    var isShowLastModified = false
        private set
    /**
     * 数据回调
     *
     * @return
     */
    /**
     * 数据请求成功回调
     */
    var callBack: LoaderDataCallBack? = null
        private set

    /**
     * 是否分页
     */
    var isShouldLoadPaging = false
        private set

    fun setShouldLoadPaging(shouldLoadPaging: Boolean): AlbumLoaderBuilder {
        isShouldLoadPaging = shouldLoadPaging
        return this
    }

    fun setCallBack(callBack: LoaderDataCallBack?): AlbumLoaderBuilder {
        this.callBack = callBack
        return this
    }

    fun setShowLastModified(showLastModified: Boolean): AlbumLoaderBuilder {
        isShowLastModified = showLastModified
        return this
    }

    fun setPageSize(pageSize: Int?): AlbumLoaderBuilder {
        pageSize?.let {
            this.pageSize = pageSize
        }
        return this
    }

    /**
     * 初始化时，设置默认值
     */
    init {
        setPageSize(50)
        setShowLastModified(false)
    }
}